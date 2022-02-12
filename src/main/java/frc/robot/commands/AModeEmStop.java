package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.RobotContainer;

public class AModeEmStop extends ParallelCommandGroup {
    public AModeEmStop() {
        //addCommands(new CmdRobotInit());

        //set retractor conditon
        addCommands(new CmdStopRoller());

        //set indexer condition
        addCommands(new CmdIndexerStop());

        //set launcher condition
        addCommands(new CmdLauncherStop());

        
        //set CLimb condition



       // addRequirements(RobotContainer.getInstance().subIntake);
       // addRequirements(RobotContainer.getInstance().subIndexer);
       // addRequirements(RobotContainer.getInstance().subLauncher);
        //addRequirements(RobotContainer.getInstance().SubClimb);  // for future use
    }
}
