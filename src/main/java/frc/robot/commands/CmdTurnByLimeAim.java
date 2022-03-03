package frc.robot.commands;

import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.RobotMath;
import frc.robot.subsystems.SubDriveTrain;
import frc.robot.subsystems.SubGyro;
import frc.robot.CommonLogic;
import frc.robot.Constants;
import frc.robot.subsystems.SubLimelight;
import frc.robot.subsystems.SubLimelight.CAM_MODE;
import frc.robot.Constants.limelightConstants.cameras;

public class CmdTurnByLimeAim extends CommandBase {

    // private final SubDriveTrain subDriveTrain;
    private boolean bDone = false;
    private SubDriveTrain subDriveTrain = null;
    private SubGyro subGyro = null;

    private int NoTargetCounter = 0;
    private double _leftTargetThrottle = 0.0;
    private double _rightTargetThrottle = 0.0;
    private double TOL = 2.0;
    private final double MIN_THROTTLE = 0.04;
    private double _KPLeft = 0.0;
    private double _KPRight = 0.0;
    private double _minLeftThrottle = 0.0;
    private double _minRightThrottle = 0.0;
    private int camPipeline = 0;
    private cameras camera2lookthrough = null;
    private SubLimelight camera = null;

    public CmdTurnByLimeAim(cameras cam, int pipeline) {
        camPipeline = pipeline;
        camera2lookthrough = cam;

    }

    public CmdTurnByLimeAim(double left_throttle, double right_throttle, cameras cam, int pipeline,
            double tol_degrees) {

        _leftTargetThrottle = left_throttle;
        _rightTargetThrottle = right_throttle;
        camPipeline = pipeline;
        camera2lookthrough = cam;
        TOL = tol_degrees;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        bDone = false;
        subDriveTrain = RobotContainer.getInstance().subDriveTrain;
        subGyro = RobotContainer.getInstance().subGyro;

        if (camera2lookthrough == cameras.limelight_high) {
            camera = RobotContainer.getInstance().subLimelightHigh;
        } else if (camera2lookthrough == cameras.limelight_low) {
            camera = RobotContainer.getInstance().subLimelightLow;
        }

        camera.setPipeline(camPipeline);
        camera.setCamMode(SubLimelight.CAM_MODE.VISION_PROCESSING);
        _minLeftThrottle = calcMinThrottle(_leftTargetThrottle, MIN_THROTTLE);
        _minRightThrottle = calcMinThrottle(_rightTargetThrottle, MIN_THROTTLE);

        _KPLeft = calcKP(_leftTargetThrottle, _minLeftThrottle);
        _KPRight = calcKP(_rightTargetThrottle, _minRightThrottle);

    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        System.err.println("cmdTurnByLime");

        double cameraAngle = 0;
        if (camera.hasTarget()) {
            // https://docs.limelightvision.io/en/latest/getting_started.html
            cameraAngle = camera.getTX();
            NoTargetCounter = 0;

            // Might need to pub logic here to cause the robot to spin in the other
            // direction
            // AKA reverse the direction of spin if the new _requested heading in getting
            // larger...

            if (cameraAngle > 0) {
                _leftTargetThrottle = .2;
                _minLeftThrottle = Constants.robotPysicalProperties.minTurnSpeed;
                _rightTargetThrottle = -_leftTargetThrottle;
                _minRightThrottle = -_minLeftThrottle;
            } else if (cameraAngle < 0) {
                _leftTargetThrottle = -.2;
                _minLeftThrottle = -Constants.robotPysicalProperties.minTurnSpeed;
                _rightTargetThrottle = -_leftTargetThrottle;
                _minRightThrottle = -_minLeftThrottle;
            } else {
                bDone = true;
            }

            double powerLeft = calcMotorPower(_leftTargetThrottle, _minLeftThrottle, _KPLeft, cameraAngle);
            double powerRight = calcMotorPower(_rightTargetThrottle, _minRightThrottle, _KPRight, cameraAngle);
            subDriveTrain.Drive(powerLeft, powerRight);

            if (CommonLogic.isInRange(cameraAngle, 0, TOL)) {
                bDone = true;
                end(false);
            }
        } else {
            NoTargetCounter = NoTargetCounter + 1;
        }
        // If we fail to see the target in 25 scans that is 1/2 second.   We may have 
        // lost the aim we might have had.
        if (NoTargetCounter > 25){
            end(false);
        }

    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        subDriveTrain.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return bDone;
    }

    @Override
    public boolean runsWhenDisabled() {
        // This command can cause robot motion.
        // this always returns false;
        return false;
    }

    // converts everything to a min throttle keeping the original sign
    private double calcMinThrottle(double throttle, double minThrottle) {
        return Math.signum(throttle) * minThrottle;
    }

    // calculates a KP based on (throttle - min throttle) / heading delta
    private double calcKP(double throttle, double minThrottle) {
        // Math Time.... Based on 1 second for 360 degree turns.

        // Assume robot has 14ft/sec = (14*12)in/sec = 168 inch/sec
        // Assume robot has track width of 24 inches
        // circumference of the circle = Math.pi * 24 = 75.4 inches
        // 75.4 / 168 means that motor power of .448 should give us a 1 second 360
        // degree turn.

        double full360turndist = Constants.robotPysicalProperties.robotTrackWidth * Math.PI;
        double full360throttle = full360turndist / Constants.robotPysicalProperties.theoreticalMaxSpeedInches;

        double retValue = Math.signum(throttle) * Math.abs(full360throttle / 360);

        // System.out.println("retValue = " + retValue);
        return retValue;
    }

    // calculates the motor power and scales it based on the heading delta
    private double calcMotorPower(double targetThrottle, double minThrottle, double KP, double headingDelta) {
        double sigNum = Math.signum(targetThrottle);

        double retValue = sigNum * (Math.abs(minThrottle) + (Math.abs(KP) * Math.abs(headingDelta)));

        if (sigNum < 0) {
            retValue = CommonLogic.CapMotorPower(retValue, targetThrottle, 0.0);
        }

        else if (sigNum > 0) {
            retValue = CommonLogic.CapMotorPower(retValue, 0.0, targetThrottle);
        } else {
            retValue = 0.0;
        }
        return retValue;
    }

}
