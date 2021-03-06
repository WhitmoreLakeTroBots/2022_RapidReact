// RobotBuilder Version: 4.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

// ROBOTBUILDER TYPE: Command.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import frc.robot.subsystems.SubDriveTrain;
import frc.robot.subsystems.SubGyro;

// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS

/**
 *
 */
public class CmdTurnByGyro extends CommandBase {

    // private final SubDriveTrain subDriveTrain;
    private boolean BDone = false;

    private double _requestedHeading = 0.0;
    private double _left_throttle = 0.0;
    private double _right_throttle = 0.0;
    private double tol = 5.0;

    public CmdTurnByGyro(double heading_deg, double left_throttle, double right_throttle) {

        _requestedHeading = heading_deg;
        _left_throttle = left_throttle;
        _right_throttle = right_throttle;
        // RobotContainer.getInstance().subDriveTrain =
        // RobotContainer.getInstance().subDriveTrain;
        // addRequirements(RobotContainer.getInstance().subDriveTrain);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        BDone = false;
        RobotContainer.getInstance().subDriveTrain.Drive(_left_throttle, _right_throttle);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        System.err.println("cmdTurnByGyro");
        if (RobotContainer.getInstance().subGyro
                .gyroInTol(RobotContainer.getInstance().subGyro.getNormaliziedNavxAngle(), _requestedHeading, tol)) {
            BDone = true;
            // subDriveTrain.stop();
            end(false);
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        RobotContainer.getInstance().subDriveTrain.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return BDone;
    }

    @Override
    public boolean runsWhenDisabled() {
        // This command can cause robot motion.
        // this always returns false;
        return false;
    }
}
