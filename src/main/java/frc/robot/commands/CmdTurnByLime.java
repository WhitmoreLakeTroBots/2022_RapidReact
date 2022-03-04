package frc.robot.commands;

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

public class CmdTurnByLime extends CommandBase {

    // private final SubDriveTrain subDriveTrain;
    private boolean bDone = false;
    private SubDriveTrain subDriveTrain = null;
    private SubGyro subGyro = null;

    private double _requestedHeading = 0.0;
    private double _currHeading = 0.0;
    private double _leftTargetThrottle = 0.0;
    private double _rightTargetThrottle = 0.0;
    private double TOL = 1.0;
    private final double MIN_THROTTLE = Constants.robotPysicalProperties.minTurnSpeed;
    private double _KPLeft = 0.0;
    private double _KPRight = 0.0;
    private double _minLeftThrottle = 0.0;
    private double _minRightThrottle = 0.0;
    private int camPipeline = 0;
    private cameras camera2lookthrough = null;
    private SubLimelight camera = null;

    public CmdTurnByLime(double heading_deg, double left_throttle, double right_throttle, cameras cam, int pipeline) {

        _requestedHeading = heading_deg;
        _leftTargetThrottle = left_throttle;
        _rightTargetThrottle = right_throttle;
        camPipeline = pipeline;
        camera2lookthrough = cam;

    }

    public CmdTurnByLime(double heading_deg, double left_throttle, double right_throttle, cameras cam, int pipeline,
            double tol_degrees) {

        _requestedHeading = heading_deg;
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

        double headingDelta = RobotMath.headingDelta(_currHeading, _requestedHeading);
        _KPLeft = calcKP(_leftTargetThrottle, _minLeftThrottle, headingDelta);
        _KPRight = calcKP(_rightTargetThrottle, _minRightThrottle, headingDelta);


    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        System.err.println("cmdTurnByLime");

        double currHeading = subGyro.getNormaliziedNavxAngle();
        //https://docs.limelightvision.io/en/latest/networktables_api.html
        // limelight 2 has +-29 degrees limelight1 is +-27 degrees
        // +-18 degrees should mean that our target is in the middle 2/3 or so of the screen
        if (subGyro.gyroInTol(currHeading, _requestedHeading, 18)) {
            if (camera.hasTarget()) {
                // https://docs.limelightvision.io/en/latest/getting_started.html
                _requestedHeading = subGyro.gyroNormalize(addGyroAngles (currHeading, camera.getTX()));
            }
        }
        
        double headingDelta = RobotMath.headingDelta(currHeading, _requestedHeading);
        double powerLeft = calcMotorPower(_leftTargetThrottle, _minLeftThrottle, _KPLeft, headingDelta);
        double powerRight = calcMotorPower(_rightTargetThrottle, _minRightThrottle, _KPRight, headingDelta);
        subDriveTrain.Drive(powerLeft, powerRight);

        if (subGyro.gyroInTol(currHeading, _requestedHeading, TOL)) {
            bDone = true;
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


    // when adding normalized angles funny things happen at 180 degrees
    // assume that 100 full revs is out of scope for real robots
	public double addGyroAngles (double angle1, double angle2){
		double a1 = angle1 + (36000);
		double a2 = angle2 + (36000);
		double retValue = a1 + a2;
		return (retValue - 36000);
	}

    // converts everything to a min throttle keeping the original sign
    private double calcMinThrottle(double throttle, double minThrottle) {
        return Math.signum(throttle) * minThrottle;
    }

    // calculates a KP based on (throttle - min throttle) / heading delta
    private double calcKP(double throttle, double minThrottle, double deltaHeading) {
        // Math Time.... Based on 1 second for 360 degree turns.

        // Assume robot has 14ft/sec = (14*12)in/sec = 168 inch/sec
        // Assume robot has track width of 24 inches
        // circumference of the circle = Math.pi * 24 = 75.4 inches
        // 75.4 / 168 means that motor power of .448 should give us a 1 second 360
        // degree turn.

        double theroyMaxSpeed = 14 * 12;
        double trackwidth = 24;
        double full360turndist = trackwidth * Math.PI;
        double full360throttle = full360turndist / theroyMaxSpeed;

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
