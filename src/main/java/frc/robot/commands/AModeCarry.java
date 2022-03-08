package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.RobotContainer;

public class AModeCarry extends ParallelCommandGroup {
    public AModeCarry() {
        //addCommands(new CmdRobotInit());

        //set retractor conditon
        addCommands(new CmdMoveExtender(22));
        addCommands(new CmdIntakeModeSet(false));
        
        //set indexer condition
        addCommands(new CmdIndexerStartCollecting());

        //set launcher condition
       // addCommands(new CmdLauncherStop());

        
        //set CLimb condition
        addCommands(new CmdClimbDisable());


        //addRequirements(RobotContainer.getInstance().subIntake);
        //addRequirements(RobotContainer.getInstance().subIndexer);
        //addRequirements(RobotContainer.getInstance().subLauncher);
        //addRequirements(RobotContainer.getInstance().SubClimb);  // for future use
    }
}
