package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.Constants.limelightConstants.cameras;
import frc.robot.commands.CmdAutoDriveStraght;
import frc.robot.commands.CmdLauncherRun;
import frc.robot.commands.CmdTurnByGyro;
import frc.robot.commands.CmdIndexerLaunch;
import frc.robot.subsystems.SubIntake;


public class AutoGrp_2BallsV2 extends SequentialCommandGroup {
    public final double TURN_SPEED = 0.2;
    public final double STRAIGHT_SPEED = 0.3;
    
    public AutoGrp_2BallsV2() {
        addCommands(new CmdRobotInit());
        //Start flywheel
        

        
        addCommands(new CmdIndexerStartCollecting());
        addCommands(new CmdLauncherRun(2850));
        addCommands(new CmdMoveExtender());
        addCommands(new CmdAutoDriveStraght(60, 0, STRAIGHT_SPEED));
        addCommands(new CmdMoveExtender(16));
        addCommands(new CmdTurnByLime(158,-TURN_SPEED, TURN_SPEED, cameras.limelight_high, 0 ));   
       
        addCommands(new CmdDelay(4.0));
        addCommands(new Agrp_Launch());

        addCommands(new CmdLauncherStop());
        addCommands(new CmdMoveExtender(0));
        addCommands(new CmdIndexerStop());
        
    }
}
