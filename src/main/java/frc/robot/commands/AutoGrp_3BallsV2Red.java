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


public class AutoGrp_3BallsV2Red extends SequentialCommandGroup {
    public final double TURN_SPEED = 0.2;
    public final double STRAIGHT_SPEED = 0.35;
    
    public AutoGrp_3BallsV2Red() {
        addCommands(new CmdRobotInit());
        //Start flywheel
        

        //get the first ball
        addCommands(new CmdIndexerStartCollecting());
        addCommands(new CmdLauncherRun(2885));
        addCommands(new CmdMoveExtender());
        addCommands(new CmdAutoDriveStraght(60, 0, STRAIGHT_SPEED));
        addCommands(new CmdMoveExtender(16));
        addCommands(new CmdTurnByGyro2(172,-TURN_SPEED, TURN_SPEED));   
       
         
        addCommands(new Agrp_Launch());
        
        //move and get more balls
        //addCommands(new CmdTurnByGyro2(-110,-TURN_SPEED, TURN_SPEED));
        addCommands(new CmdLauncherRun(2675));
        addCommands(new CmdMoveExtender());
        addCommands(new CmdAutoDriveStraght(112, -115, STRAIGHT_SPEED));
        addCommands(new CmdMoveExtender(0));
        addCommands(new CmdTurnByGyro2(130, -TURN_SPEED, TURN_SPEED));
        addCommands(new CmdAutoDriveStraght(25, 130, STRAIGHT_SPEED));
        
        addCommands(new CmdTurnByLimeAim(cameras.limelight_high,0));
        addCommands(new Agrp_Launch());

        //stop
        addCommands(new CmdLauncherStop());
        addCommands(new CmdMoveExtender(0));
        addCommands(new CmdIndexerStop());
        
    }
}
