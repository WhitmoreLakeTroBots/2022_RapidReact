package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.subsystems.SubLauncher;

public class CmdLauncherRun extends CommandBase {

    private double flyWeelSpeedRPM = 0;

    public boolean bdone = false;
    public CmdLauncherRun(Double RPM) {
        flyWeelSpeedRPM = RPM;

    }

    public CmdLauncherRun (int RPM){
        flyWeelSpeedRPM = (double) RPM;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        bdone = false;
        System.err.println("LaunchInit");
        addRequirements(RobotContainer.getInstance().subLauncher);
        RobotContainer.getInstance().subLauncher.AutoRPM_set(false);
        RobotContainer.getInstance().subLauncher.setTargetRPM(flyWeelSpeedRPM);
        
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        System.err.println("launcherExecute");
        bdone = true;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        bdone = true;
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return bdone;
    }

    @Override
    public boolean runsWhenDisabled() {
        return false;
    }
}
