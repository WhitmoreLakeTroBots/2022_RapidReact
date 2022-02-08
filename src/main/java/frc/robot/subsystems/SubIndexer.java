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
public class SubIndexer extends SubsystemBase {
    //****** Cfeate constants */
    // the track motor
    private WL_Spark CanSpark_TrackMotor;

    // the feeder motor
    private WL_Spark CanSpark_FeederMotor;

    // feeder collection power
    private double FeederCollectionPower = 0.4;

    // feeder launcher power - this should be reverse of collection power
    private double FeederLauncherPower = -1*FeederCollectionPower;

    // track power
    private double TrackMotorPower = 0.4;


    public SubIndexer() {
  //**** set constants   */
        // mapping the track motor to the Spark
        CanSpark_TrackMotor = new WL_Spark(CAN_ID_Constants.kCanID_Track, WL_Spark.MotorType.kBrushless);
    
        // mapping the feeder motor to the spark
        CanSpark_FeederMotor = new WL_Spark(CAN_ID_Constants.kCanID_Feeder, WL_Spark.MotorType.kBrushless);

        // restore to factory default
        CanSpark_TrackMotor.restoreFactoryDefaults();
        CanSpark_FeederMotor.restoreFactoryDefaults();

        // set the current limit in Amps
        CanSpark_TrackMotor.setSmartCurrentLimit(25);
        CanSpark_FeederMotor.setSmartCurrentLimit(25);

        // set the idle modes
        CanSpark_TrackMotor.setIdleMode(WL_Spark.IdleMode.kCoast);
        CanSpark_FeederMotor.setIdleMode(WL_Spark.IdleMode.kCoast);
        
        // set inverted to false
        CanSpark_TrackMotor.setInverted(false);
        CanSpark_FeederMotor.setInverted(false);

        // finish intialization by burning the flash
        CanSpark_FeederMotor.burnFlash();
        CanSpark_TrackMotor.burnFlash();
    }

    @Override
    public void periodic() {
    }

    public void starttrack() {
        //start Track motor at TrackPower
        CanSpark_TrackMotor.set(TrackMotorPower);
    }

    
    public void stopTrack() {
        //stop track motor
        CanSpark_TrackMotor.set(0);
    }

    public void FeederHolding(){
        //run Launch motor backwork at feederCollectionPower
        CanSpark_FeederMotor.set(FeederCollectionPower);
    }

    public void FeederLaunch(){
        //run Feeder forward for a short duration
        CanSpark_FeederMotor.set(FeederLauncherPower);
    } 

    public void FeederStop(){
        //stop Feeder
        CanSpark_FeederMotor.set(0);
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run when in simulation

    }

}