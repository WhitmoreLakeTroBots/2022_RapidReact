package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class Agrp_climnt extends SequentialCommandGroup {
    public Agrp_climnt() {


        System.err.print("*BAD*");
        addCommands(new CmdExstendClimb(0.7));
        addCommands(new CmdDelay(1.0));
        addCommands(new CmdRetractClimb(0.7));
    }
}
