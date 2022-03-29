package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.RobotMath;
import frc.robot.hardware.WL_Spark;
import frc.robot.subsystems.SubDriveTrain;
import frc.robot.subsystems.SubGyro;
import frc.robot.CommonLogic;
import frc.robot.Constants;

public class CmdTurnByGyro2 extends CommandBase {

    // private final SubDriveTrain subDriveTrain;
    private boolean bDone = false;
    private SubDriveTrain subDriveTrain = null;
    private SubGyro subGyro = null;

    private double _requestedHeading = 0.0;
    private double _currHeading = 0.0;
    private double _leftTargetThrottle = 0.0;
    private double _rightTargetThrottle = 0.0;
    private final double TOL = Constants.robotPysicalProperties.turnTolGyro;
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
        _minLeftThrottle = RobotMath.calcMinThrottle(_leftTargetThrottle, MIN_THROTTLE);
        _minRightThrottle = RobotMath.calcMinThrottle(_rightTargetThrottle, MIN_THROTTLE);

        //double headingDelta = RobotMath.headingDelta(_currHeading, _requestedHeading);
        _KPLeft = RobotMath.calcKP(_leftTargetThrottle, _minLeftThrottle);
        _KPRight = RobotMath.calcKP(_rightTargetThrottle, _minRightThrottle);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        System.err.println("cmdTurnByGyro");
        double currHeading = subGyro.getNormaliziedNavxAngle();
        double headingDelta = RobotMath.headingDelta(currHeading, _requestedHeading);
        double powerLeft = RobotMath.calcMotorPower(_leftTargetThrottle, _minLeftThrottle, _KPLeft, headingDelta);
        double powerRight = RobotMath.calcMotorPower(_rightTargetThrottle, _minRightThrottle, _KPRight, headingDelta);
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

}
