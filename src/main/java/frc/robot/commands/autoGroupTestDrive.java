package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class autoGroupTestDrive extends SequentialCommandGroup {
    public autoGroupTestDrive() {
        addCommands(new CmdRobotInit());
        addCommands(new CmdAutoDriveStraght(36, 0, 0.15));
        addCommands(new CmdAutoDriveStraght(36, 0, -0.15));
    }
}
