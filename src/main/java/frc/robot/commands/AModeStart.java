package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.subsystems.*;

public class AModeStart extends ParallelCommandGroup {
    public AModeStart() {
        //addCommands(new CmdRobotInit());

        //set retractor conditon
        addCommands(new CmdMoveExtender(0));
        
        //set indexer condition
        addCommands(new CmdIndexerStop());

        //set launcher condition
        addCommands(new CmdLauncherStop());

        
        //set CLimb condition


        //addRequirements(RobotContainer.getInstance().subIntake);
        //addRequirements(RobotContainer.getInstance().subIndexer);
        //addRequirements(RobotContainer.getInstance().subLauncher);
        //addRequirements(RobotContainer.getInstance().SubClimb);  // for future use
    }
}
