package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class Agrp_climnt extends SequentialCommandGroup {
    public Agrp_climnt() {

        addCommands(new CmdExstendClimb());
        addCommands(new CmdDelay(1.0));
        addCommands(new CmdRetractClimb());
    }
}
