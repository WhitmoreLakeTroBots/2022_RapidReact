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


public class AutoGrp_3BallsV2 extends SequentialCommandGroup {
    public final double TURN_SPEED = 0.175;
    public final double STRAIGHT_SPEED = 0.325;
    
    public AutoGrp_3BallsV2() {
        addCommands(new CmdRobotInit());
        //Start flywheel
        

        //get the first ball
        addCommands(new CmdIndexerStartCollecting());
        addCommands(new CmdLauncherRun(2870));
        addCommands(new CmdMoveExtender());
        addCommands(new CmdAutoDriveStraght(60, 0, STRAIGHT_SPEED));
        addCommands(new CmdMoveExtender(16));
        addCommands(new CmdTurnByGyro2(172,-TURN_SPEED, TURN_SPEED));   
       
        addCommands(new CmdDelay(.25));
        addCommands(new Agrp_Launch());
        
        //move and get more balls
        //addCommands(new CmdTurnByGyro2(-110,-TURN_SPEED, TURN_SPEED));
        addCommands(new CmdLauncherRun(2675));
        addCommands(new CmdAutoDriveStraght(108, -110, .2));
        addCommands(new CmdMoveExtender());
        addCommands(new CmdMoveExtender(0));
        addCommands(new CmdTurnByGyro2(125, -TURN_SPEED, TURN_SPEED));
        addCommands(new CmdAutoDriveStraght(20, 125, STRAIGHT_SPEED));

        addCommands(new CmdDelay(.25));
        addCommands(new Agrp_Launch());

        //stop
        addCommands(new CmdLauncherStop());
        addCommands(new CmdMoveExtender(0));
        addCommands(new CmdIndexerStop());
        
    }
}
