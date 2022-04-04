package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.Constants.limelightConstants.cameras;
import frc.robot.commands.CmdAutoDriveStraght;
import frc.robot.commands.CmdLauncherRun;
import frc.robot.commands.CmdTurnByGyro;
import frc.robot.commands.CmdIndexerLaunch;
import frc.robot.subsystems.SubIntake;


public class AutoGrp_2BallsV3 extends SequentialCommandGroup {
    public final double TURN_SPEED = 0.175;
    public final double STRAIGHT_SPEED = 0.275;
    
    public AutoGrp_2BallsV3() {
        addCommands(new CmdRobotInit());
        //Start flywheel
        addCommands(new CmdLauncherRun(2625));
        addCommands(new CmdIndexerStartCollecting());  
        addCommands(new CmdMoveExtender());
        addCommands(new CmdAutoDriveStraght(48, 0, STRAIGHT_SPEED));
        addCommands(new CmdMoveExtender(16));
        //addCommands(new CmdAutoDriveStraght(20, 0, -STRAIGHT_SPEED));
        addCommands(new CmdTurnByGyro2(-167,TURN_SPEED, -TURN_SPEED));
        addCommands(new CmdAutoDriveStraght(28, -167, STRAIGHT_SPEED));

        addCommands(new CmdDelay(.5));
        addCommands(new Agrp_Launch());

        addCommands(new CmdLauncherStop());
        addCommands(new CmdMoveExtender(0));
        addCommands(new CmdIndexerStop());
    }
}
