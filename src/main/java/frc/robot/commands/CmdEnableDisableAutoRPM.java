package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.subsystems.SubIntake;
import frc.robot.RobotMath; 

public class CmdEnableDisableAutoRPM extends CommandBase {

        public boolean bDone = false;
        public boolean autoRPMEnabled = false;
        public boolean performToggle = false;

    public CmdEnableDisableAutoRPM (boolean autoRPM){
        autoRPMEnabled = autoRPM;
        performToggle = false;
    }

    public CmdEnableDisableAutoRPM (){
        
        performToggle = true;
    }
    
    @Override
    public void initialize() {
        if (performToggle) {
            RobotContainer.getInstance().subLauncher.AutoRPM_toggle();
        }
        else {
            RobotContainer.getInstance().subLauncher.AutoRPM_set(autoRPMEnabled);
        }
        bDone = true;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
       
        bDone = true;
        
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        bDone = true;
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
