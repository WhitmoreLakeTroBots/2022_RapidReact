package frc.robot.commands;

import java.lang.module.ModuleDescriptor.Requires;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.hardware.WL_Spark;


public class CmdSetDriveTrainBrakeMode extends CommandBase {

    private WL_Spark.IdleMode newBrakeMode;
    private Boolean bDone = false;

    public CmdSetDriveTrainBrakeMode(WL_Spark.IdleMode kBrakeMode) {
        newBrakeMode = kBrakeMode;

    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        
        bDone = false;

    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        System.err.println("CmdSetDriveTrainBrakeMode");
        RobotContainer.getInstance().subDriveTrain.SetBrakeMode(newBrakeMode);
        bDone = true;

    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {

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
