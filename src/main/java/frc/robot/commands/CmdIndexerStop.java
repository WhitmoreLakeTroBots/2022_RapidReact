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
import frc.robot.Robot;
import frc.robot.RobotContainer;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import frc.robot.subsystems.SubLauncher;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS

/**
 *
 */
public class CmdIndexerStop extends CommandBase {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
        //private SubLauncher subLauncher;

        public boolean bdone = false;
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS


    //************** uncomment for full robot*********** */
    public CmdIndexerStop() {
       // flyWeelSpeedRPM = RPM;
        
       
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDERz ID=CONSTRUCTORS
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    

//********************* uncomment for full robot */
       // subLauncher = RobotContainer.getInstance().subLauncher;
    //addRequirements(subLauncher);
//******************  see above************* */



    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        bdone = false;
        System.err.println("IndexerStop"); 

        addRequirements(RobotContainer.getInstance().subIndexer);
        RobotContainer.getInstance().subIndexer.stopTrack();
        RobotContainer.getInstance().subIndexer.FeederStop();
        bdone = true;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        //System.err.println(String.format("launcherExecute flyWeelSpeedRPMz:%1$.3d",flyWeelSpeedRPM));
        System.err.println("IndexerStop - Execute");
        
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
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DISABLED
        return false;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DISABLED
    }
}
