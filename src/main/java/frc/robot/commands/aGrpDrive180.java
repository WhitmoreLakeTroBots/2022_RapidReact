package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class aGrpDrive180 extends SequentialCommandGroup {
    public aGrpDrive180() {
        // init robot
        addCommands(new CmdRobotInit());
        // Drive straight 36inches
        addCommands(new CmdAutoDriveStraght(36, 0, 0.15));
        // turn 180
        addCommands(new CmdTurnByGyro(180, -.08, .08));
        // Drive back
        addCommands(new CmdAutoDriveStraght(36, 180, 0.15));
        // Turn back to 0
        addCommands(new CmdTurnByGyro(0, .08, -.08));
    }
}
