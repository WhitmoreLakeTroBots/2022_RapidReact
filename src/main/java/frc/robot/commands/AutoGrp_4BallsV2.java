package frc.robot.commands;

import javax.swing.text.Highlighter.Highlight;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.Constants.limelightConstants.cameras;
import frc.robot.commands.CmdAutoDriveStraght;
import frc.robot.commands.CmdLauncherRun;
import frc.robot.commands.CmdTurnByGyro;
import frc.robot.commands.CmdIndexerLaunch;
import frc.robot.subsystems.SubIntake;


public class AutoGrp_4BallsV2 extends SequentialCommandGroup {
    public final double TURN_SPEED = 0.2;
    public final double STRAIGHT_SPEED = 0.3;
    
    public AutoGrp_4BallsV2() {
        addCommands(new CmdRobotInit());
        //Start flywheel
        

        //get the first ball
        addCommands(new CmdIndexerStartCollecting());
        addCommands(new CmdLauncherRun(2800));
        addCommands(new CmdMoveExtender());
        addCommands(new CmdAutoDriveStraght(60, 0, STRAIGHT_SPEED));
        addCommands(new CmdMoveExtender(16));
        addCommands(new CmdTurnByLime(158,TURN_SPEED, -TURN_SPEED, cameras.limelight_high, 0 ));   
       
        //shoot
        addCommands(new Agrp_Launch());
        
        //move and get two more balls
        addCommands(new CmdTurnByGyro2(16,-TURN_SPEED, TURN_SPEED));
        addCommands(new CmdAutoDriveStraght(120, 16, STRAIGHT_SPEED));
        addCommands(new CmdMoveExtender());
        addCommands(new CmdAutoDriveStraght(30, -29, STRAIGHT_SPEED));
        //addCommands(new CmdTurnByGyro2(-29,-TURN_SPEED, TURN_SPEED));
        addCommands(new CmdAutoDriveStraght(30, -29, STRAIGHT_SPEED));
        addCommands(new CmdLauncherRun(2900));
        addCommands(new CmdDelay(0.5));
        addCommands(new CmdMoveExtender(16));
        addCommands(new CmdAutoDriveStraght(20, -29, -STRAIGHT_SPEED));
        //addCommands(new CmdTurnByGyro2(180,TURN_SPEED, -TURN_SPEED));
       //addCommands(new CmdAutoDriveStraght(140, 180, .65));
        //addCommands(new CmdTurnByLime(180,-TURN_SPEED, TURN_SPEED, cameras.limelight_high, 0 ));

        //shoot
        //addCommands(new CmdDelay(.25));
        //addCommands(new Agrp_Launch());

        //stop
        addCommands(new CmdLauncherStop());
        addCommands(new CmdMoveExtender(0));
        addCommands(new CmdIndexerStop());
        
    }
}
