package frc.robot.commands;

import java.lang.module.ModuleDescriptor.Requires;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.CommonLogic;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.subsystems.SubClimber;

public class CmdBackTransverse extends CommandBase {

    private boolean bdone = false;


    public CmdBackTransverse() {


        


        // addRequirements(RobotContainer.getInstance().subDriveTrain);
        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
 
        bdone = false;
        addRequirements(RobotContainer.getInstance().subClimber);

    //extend climb 
        RobotContainer.getInstance().subClimber.SetTransversePos(-119);
        //RobotContainer.getInstance().subClimber.Climb_power = .4;

    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
    
        

        if (CommonLogic.isInRange(RobotContainer.getInstance().subClimber.getTransverseCurPos(), RobotContainer.getInstance().subClimber.getTransverseTarPos(), RobotContainer.getInstance().subClimber.getTransverseTolPos())) {
            bdone = true;
        }

    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return bdone;
    }

    @Override
    public boolean runsWhenDisabled() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DISABLED
        return false;

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DISABLED
    }

}
