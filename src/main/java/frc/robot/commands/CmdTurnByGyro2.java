package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.RobotMath;
import frc.robot.hardware.WL_Spark;
import frc.robot.subsystems.SubDriveTrain;
import frc.robot.subsystems.SubGyro;
import frc.robot.CommonLogic;
import frc.robot.Constants;
import frc.robot.hardware.WL_Spark;

public class CmdTurnByGyro2 extends CommandBase {

    // private final SubDriveTrain subDriveTrain;
    private boolean bDone = false;
    private SubDriveTrain subDriveTrain = null;
    private SubGyro subGyro = null;

    private double _requestedHeading = 0.0;
    private double _currHeading = 0.0;
    private double _leftTargetThrottle = 0.0;
    private double _rightTargetThrottle = 0.0;
    private final double TOL = 1.0;
    private final double MIN_THROTTLE = Constants.robotPysicalProperties.minTurnSpeed;
    private double _KPLeft = 0.0;
    private double _KPRight = 0.0;
    private double _minLeftThrottle = 0.0;
    private double _minRightThrottle = 0.0;
    private WL_Spark.IdleMode idleMode = WL_Spark.IdleMode.kBrake;

    public CmdTurnByGyro2(double heading_deg, double left_throttle, double right_throttle) {
        _requestedHeading = heading_deg;
        _leftTargetThrottle = left_throttle;
        _rightTargetThrottle = right_throttle;
    }


    public CmdTurnByGyro2(double heading_deg, double left_throttle, double right_throttle,
         WL_Spark.IdleMode brakeMode) {
        _requestedHeading = heading_deg;
        _leftTargetThrottle = left_throttle;
        _rightTargetThrottle = right_throttle;
        idleMode = brakeMode;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        bDone = false;
        subDriveTrain = RobotContainer.getInstance().subDriveTrain;
        subDriveTrain.SetBrakeMode(idleMode);
        subGyro = RobotContainer.getInstance().subGyro;
        _currHeading = subGyro.getNormaliziedNavxAngle();
        _minLeftThrottle = calcMinThrottle(_leftTargetThrottle, MIN_THROTTLE);
        _minRightThrottle = calcMinThrottle(_rightTargetThrottle, MIN_THROTTLE);

        double headingDelta = RobotMath.headingDelta(_currHeading, _requestedHeading);
        _KPLeft = calcKP(_leftTargetThrottle, _minLeftThrottle, headingDelta);
        _KPRight = calcKP(_rightTargetThrottle, _minRightThrottle, headingDelta);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        System.err.println("cmdTurnByGyro");
        double currHeading = subGyro.getNormaliziedNavxAngle();
        double headingDelta = RobotMath.headingDelta(currHeading, _requestedHeading);
        double powerLeft = calcMotorPower(_leftTargetThrottle, _minLeftThrottle, _KPLeft, headingDelta);
        double powerRight = calcMotorPower(_rightTargetThrottle, _minRightThrottle, _KPRight, headingDelta);
        subDriveTrain.Drive(powerLeft, powerRight);

        // Should we stop ?
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
        return false;
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
