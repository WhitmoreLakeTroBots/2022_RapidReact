package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.subsystems.SubClimber;

public class Agrp_Transverse extends SequentialCommandGroup {
    public Agrp_Transverse() {

        //addCommands(new CmdMoveExtender());
        //addCommands(new CmdDelay(0.45));
        addCommands(new CmdGrabTransverse());
        addCommands(new CmdDelay(0.2));
        addCommands(new CmdReleaseClimb(0.2));
        addCommands(new CmdDelay(0.05));
        addCommands(new CmdMoveExtender(0));
        addCommands(new CmdReleaseTransverse());
        addCommands(new CmdDelay(0.05));
        addCommands(new CmdGrabClimb(0.7));
        addCommands(new CmdDelay(0.05));
        addCommands(new CmdPassClimb(0.7));
        addCommands(new CmdDelay(0.05));
        addCommands(new CmdBackTransverse());
        addCommands(new CmdDelay(0.05));
        addCommands(new CmdRetractClimb(0.7));

    }
}
