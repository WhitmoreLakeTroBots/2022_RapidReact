package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.subsystems.SubClimber;

public class Agrp_TransverseFull extends SequentialCommandGroup {
    public Agrp_TransverseFull() {

        addCommands(new CmdMoveExtender());
        addCommands(new CmdDelay(0.25));
        addCommands(new CmdGrabTransverse());
        addCommands(new CmdDelay(0.25));
        addCommands(new CmdReleaseClimb(0.4));
        addCommands(new CmdDelay(0.1));
        addCommands(new CmdMoveExtender(0));
        addCommands(new CmdReleaseTransverse());
        addCommands(new CmdDelay(0.1));
        addCommands(new CmdGrabClimb(0.7));
        addCommands(new CmdDelay(0.1));
        addCommands(new CmdPassClimb(0.7));
        addCommands(new CmdDelay(0.1));
        addCommands(new CmdBackTransverse());
        addCommands(new CmdDelay(0.1));
        addCommands(new CmdRetractClimb(0.7));

        addCommands(new CmdDelay(1));
        addCommands(new CmdMoveExtender());
        addCommands(new CmdDelay(0.25));
        addCommands(new CmdGrabTransverse());
        addCommands(new CmdDelay(0.25));
        addCommands(new CmdReleaseClimb(0.4));
        addCommands(new CmdDelay(0.1));
        addCommands(new CmdMoveExtender(0));
        addCommands(new CmdReleaseTransverse());
        addCommands(new CmdDelay(0.1));
        addCommands(new CmdGrabClimb(0.7));
        addCommands(new CmdDelay(0.1));
       /* addCommands(new CmdPassClimb(0.7));
        addCommands(new CmdDelay(0.1));
        addCommands(new CmdBackTransverse());
        addCommands(new CmdDelay(0.1));
        addCommands(new CmdRetractClimb(0.7));
*/
    }
}
