package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.limelightConstants.cameras;

public class Agrp_Launch extends SequentialCommandGroup {
    public Agrp_Launch() {
        
        addCommands(new CmdTurnByLimeAim(cameras.limelight_high,0));
        addCommands(new CmdIndexerLaunch());
        addCommands(new CmdDelay(.5));
        addCommands(new CmdIndexerLaunch());
        addCommands(new CmdDelay(.5));

    }
}
