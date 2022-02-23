package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.RobotMath;
import frc.robot.subsystems.SubDriveTrain;
import frc.robot.subsystems.SubGyro;

public class CmdTurnByGyro2 extends CommandBase {

    // private final SubDriveTrain subDriveTrain;
    private boolean bDone = false;
    private SubDriveTrain subDriveTrain = null;
    private SubGyro subGyro = null;

    private double _requestedHeading = 0.0;
    private double _currHeading = 0.0;
    private double _left_throttle = 0.0;
    private double _right_throttle = 0.0;
    private final double TOL = 1.0;
    private final double MIN_THROTTLE = 0.075;
    private double _KPLeft = 0.0;
    private double _KPRight = 0.0;
    private double _minLeftThrottle = 0.0;
    private double _minRightThrottle = 0.0;

    public CmdTurnByGyro2(double heading_deg, double left_throttle, double right_throttle) {
        _requestedHeading = heading_deg;
        _left_throttle = left_throttle;
        _right_throttle = right_throttle;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        bDone = false;
        subDriveTrain = RobotContainer.getInstance().subDriveTrain;
        subGyro = RobotContainer.getInstance().subGyro;
        _currHeading = subGyro.getNormaliziedNavxAngle();
        _minLeftThrottle = calcMinThrottle(_left_throttle);
        _minRightThrottle = calcMinThrottle(_right_throttle);

        double headingDelta = RobotMath.headingDelta(_currHeading, _requestedHeading);
        _KPLeft = calcKP(_left_throttle, _minLeftThrottle, headingDelta);
        _KPRight = calcKP(_right_throttle, _minRightThrottle, headingDelta);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        System.err.println("cmdTurnByGyro");
        double currHeading = subGyro.getNormaliziedNavxAngle();
        double headingDelta = RobotMath.headingDelta(currHeading, _requestedHeading);
        double powerLeft = calcMotorPower(_minLeftThrottle, _KPLeft, headingDelta);
        double powerRight = calcMotorPower(_minRightThrottle, _KPRight, headingDelta);
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
    private double calcMinThrottle(double throttle) {
        return Math.signum(throttle) * MIN_THROTTLE;
    }

    // calculates a KP based on (throttle - min throttle) / heading delta
    private double calcKP(double throttle, double minThrottle, double deltaHeading) {
        return Math.signum(throttle) * (Math.abs(throttle) - Math.abs(minThrottle) / deltaHeading);
    }

    // calculates the motor power and scales it based on the heading delta
    private double calcMotorPower(double minThrottle, double KP, double headingDelta) {
        double sigNum = Math.signum(minThrottle);
        return sigNum * (Math.abs(minThrottle) + (Math.abs(KP) * Math.abs(headingDelta)));
    }

}
