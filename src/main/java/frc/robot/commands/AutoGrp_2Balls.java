package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.commands.CmdAutoDriveStraght;
import frc.robot.commands.CmdLauncherRun;
import frc.robot.commands.CmdTurnByGyro;
import frc.robot.commands.CmdIndexerLaunch;
import frc.robot.subsystems.SubIntake;


public class AutoGrp_2Balls extends SequentialCommandGroup {
    public final double TURN_SPEED = .25;
    public final double STRAIGHT_SPEED = .5;
    
    public AutoGrp_2Balls() {
        addCommands(new CmdRobotInit());
        //Start flywheel
        addCommands(new CmdLauncherRun(2600));
        addCommands(new CmdDelay(1.0));
        addCommands(new CmdIndexerStartCollecting());
        addCommands(new CmdDelay(.25));
        addCommands(new CmdIndexerLaunch());
        addCommands(new CmdDelay(.75));
        addCommands(new CmdTurnByGyro(180, TURN_SPEED, -TURN_SPEED));   
        addCommands(new CmdMoveExtender());
        addCommands(new CmdAutoDriveStraght(38, 180, STRAIGHT_SPEED));
        addCommands(new CmdTurnByGyro(180,-TURN_SPEED, TURN_SPEED));
        addCommands(new CmdDelay(0.5));
        addCommands(new CmdIndexerLaunch());
        addCommands(new CmdDelay(.5));
        addCommands(new CmdLauncherStop());
        addCommands(new CmdMoveExtender(0));
    }
}
