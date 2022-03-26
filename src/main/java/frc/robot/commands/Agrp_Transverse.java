package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.subsystems.SubClimber;

public class Agrp_Transverse extends SequentialCommandGroup {
    public Agrp_Transverse() {

        addCommands(new CmdMoveExtender());
        addCommands(new CmdDelay(1));
        addCommands(new CmdGrabTransverse());
        addCommands(new CmdDelay(1));
        addCommands(new CmdReleaseClimb());
        addCommands(new CmdDelay(1));
        addCommands(new CmdMoveExtender(0));
        addCommands(new CmdReleaseTransverse());
        addCommands(new CmdDelay(1));
        addCommands(new CmdGrabClimb());
        addCommands(new CmdDelay(1));
        addCommands(new CmdPassClimb());
        addCommands(new CmdDelay(1));
        addCommands(new CmdBackTransverse());
        addCommands(new CmdDelay(1));
        addCommands(new CmdRetractClimb());

    }
}
