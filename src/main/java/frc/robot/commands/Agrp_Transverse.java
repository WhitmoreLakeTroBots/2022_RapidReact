package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.subsystems.SubClimber;

public class Agrp_Transverse extends SequentialCommandGroup {
    public Agrp_Transverse() {

        addCommands(new CmdSetTravs(RobotContainer.getInstance().subClimber.getTransverseGrabPos()));
        addCommands(new CmdSetClimb(RobotContainer.getInstance().subClimber.getClimbReleasePos()));
        addCommands(new CmdSetTravs(RobotContainer.getInstance().subClimber.getTransverseReleasePos()));
        addCommands(new CmdSetClimb(RobotContainer.getInstance().subClimber.getTransverseGrabPos()));
        addCommands(new CmdSetClimb(RobotContainer.getInstance().subClimber.getClimbReleasePos()));
        addCommands(new CmdSetTravs(-119));
        addCommands(new CmdSetClimb(RobotContainer.getInstance().subClimber.getClimbRetractPos()));
        
    }
}
