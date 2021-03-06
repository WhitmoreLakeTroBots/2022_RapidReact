// RobotBuilder Version: 4.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

// ROBOTBUILDER TYPE: Subsystem.

package frc.robot.subsystems;

import frc.robot.CommonLogic;
import frc.robot.RobotContainer;
import frc.robot.Constants.*;
import frc.robot.hardware.WL_Spark;

import java.lang.annotation.Target;
import java.util.concurrent.BrokenBarrierException;

import com.revrobotics.SparkMaxPIDController;
import frc.robot.PID;
//import frc.robot.commands.*;
//import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS

/**
 *
 */
public class SubIntake extends SubsystemBase {
    // *****Add Constants here.....
    // need a doube for rollerPower
    private double RollerSpeed = 1.0;
    private boolean bRollerClimb = false;
    // need double maxPostion set to max value
    private float MaxPostion = 75;
    // need double Tolerance
    public  final double Tolerance = 3;
    // need double MinPosition set to 0
    private float MinPosition = 0;
    // Need double fullExtentedPos set to fully extended position (need to test)
    private double FullExtendedPos = 73;
    // need double retractedPos set to 250
    private double RetractedPos = 0;
    // need double retractorSpeed set 0.4
    private double RetractorSpeed = 0.75;
    // set targetPosition = RetractedPos // make sure this is a private
    private double TargetPosition = MinPosition;
    // bEnabled
    private boolean bEnabled = true;
    // Need to define objects (Motor) for the roller
    private WL_Spark CanSpark_Roller;
    // Need to define object (Motor) for the retractor
    private WL_Spark CanSpark_Retractor;

    private double AutoStartRollerPos = 40;

    public double RollerAmps;

    public SubIntake() {
 //*****Initialize constants here*****

//Map Roller to Spark here
   CanSpark_Roller = new WL_Spark(CAN_ID_Constants.kCanID_Roller, WL_Spark.MotorType.kBrushless);
// Map retractor  to spark here
  CanSpark_Retractor = new WL_Spark(CAN_ID_Constants.kCanID_Retractor, WL_Spark.MotorType.kBrushless);


// set roller and retractor to RestoreFactoryDefaults
CanSpark_Retractor.restoreFactoryDefaults();
CanSpark_Roller.restoreFactoryDefaults();
// set roller and retractor .setSmartCurrentLimits(25);
CanSpark_Roller.setSmartCurrentLimit(25);
CanSpark_Retractor.setSmartCurrentLimit(25);
// set roller to coast on idlemode
CanSpark_Roller.setIdleMode(WL_Spark.IdleMode.kCoast);
// set retractor to brake on idlemode 
CanSpark_Retractor.setIdleMode(WL_Spark.IdleMode.kBrake);
// set roller inversion here  
//***This should be set to true to reverse it   
CanSpark_Roller.setInverted(true);
// set retractor inverstion here  
// we don't know if this need to be true or false until testing 
CanSpark_Retractor.setInverted(true);

//set softlimits
CanSpark_Retractor.setSoftLimit(WL_Spark.SoftLimitDirection.kForward, MaxPostion);
CanSpark_Retractor.setSoftLimit(WL_Spark.SoftLimitDirection.kReverse, MinPosition);

//  Do this last in initialization set roller and retrator .burnFlash
CanSpark_Roller.burnFlash();
CanSpark_Retractor.burnFlash();
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        if (bEnabled) {
            gotoPositon();
            autoRoller();
        } else {
            stopRoller();
            TargetPosition = CanSpark_Roller.getPosition();
        }

        //Need to process Roller actions.  
        RollerAmps = CanSpark_Roller.getOutputCurrent();

    }

    public void startRoller() {
        // run roller at RollerPower
        CanSpark_Roller.set(RollerSpeed);
    }

    public void ReverseRoller() {
        // run roller at (-) roller power
        CanSpark_Roller.set(-1 * RollerSpeed);
    }

    public void stopRoller() {
        // run Roller at 0
        CanSpark_Roller.set(0);
    }

    public void climbMode(){
        bRollerClimb = true;
    }

    public void driveMode(){
        bRollerClimb = false;
    }

    private void gotoPositon() {
        // if current position is less than targetPostion
        if (CommonLogic.isInRange(CanSpark_Retractor.getPosition(), TargetPosition, Tolerance)) {
            // than stop
            CanSpark_Retractor.set(0);
        }

        // else if current positon is greater than target postion
        else if (CanSpark_Retractor.getPosition() > TargetPosition + Tolerance) {
            // then apply (-) RetractorPower
            CanSpark_Retractor.set(-1 * RetractorSpeed);
        }
        // else if if current position is commonlogic inRange
        else if (CanSpark_Retractor.getPosition() < TargetPosition - Tolerance) {
            // then apply RetractorPower
            CanSpark_Retractor.set(RetractorSpeed);
        }
    }

    public void retract() {
        // this should close the retractor to RetractedPos
        // hint this should call extent with the RetractorPos
        setExtend(RetractedPos);
    }

    public void setExtend(double newTargetPosition){
        TargetPosition = CommonLogic.CapMotorPower(newTargetPosition, MinPosition, MaxPostion );

    }

    public double GetRetractorPosition() {
        // return RetractorPostion not 0
        return CanSpark_Retractor.getPosition();
    }

    public double GetRetractedPosition(){
        return RetractedPos;
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run when in simulation

    }

    private void autoRoller() {
        if(!bRollerClimb){
        
        double currentPosition = RobotContainer.getInstance().subIntake.GetRetractorPosition();
        if (currentPosition > AutoStartRollerPos) {
            RobotContainer.getInstance().subIntake.startRoller();
        } else if (currentPosition <= AutoStartRollerPos) {
            RobotContainer.getInstance().subIntake.stopRoller();
        }
        
    }

    }

    public double getFullExtendedPos() {
        return FullExtendedPos;
    }

}
