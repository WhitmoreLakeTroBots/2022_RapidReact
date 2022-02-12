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

public class SubLauncher extends SubsystemBase {
    public double kP, kI, kD, kIz, kFF,  maxRPM, minVEL, maxACC;
    public final double kMaxOutput = 1.0;
    public final double kMinOutput = -1.0;
    public PID PIDcalc;

    private double iTargetRPM = 0;
    
    public WL_Spark CanSpark_launcher;
    private double currRequestedPower = 0;  // current power requests
    private double currPowerStep = 0;  // how large of steps to take for ramping
    private double PIDv = 0;
    private final double LAUNCHER_MAX_RPM = 5676;
    private final int RAMP_STEPS = 25;
    private final double STEP_RANGE = 2.0/ RAMP_STEPS;
    public enum LauncherModes {
        RAMPING,
        RUNNING,
        STOPPED
    }

    private LauncherModes currLauncherMode = LauncherModes.STOPPED;

    public SubLauncher() {
        kP = 7e-3;
        // kI = 3e-7;
        kI = 0.0;
        //kD = 3e-8;
        kD = 0.0;
        kIz = 0;
        kFF = 0;
        
        
        maxACC = 3000;

        // ****For full chassis uncomment
        CanSpark_launcher = new WL_Spark(CAN_ID_Constants.kCanID_Launcher_1, WL_Spark.MotorType.kBrushless);
        PIDcalc = new PID(kP, kI, kD);

        // CanSpark_launcher_follower = new
        // WL_Spark(Constants.CAN_ID_Constants.kCanID_launcher_2,CANSparkMax.MotorType.kBrushless);

        // Incase we have swapped one that has an inverted or current limit set in
        // memory
        CanSpark_launcher.restoreFactoryDefaults();
        CanSpark_launcher.setInverted(false);
        CanSpark_launcher.setSmartCurrentLimit(100);
        CanSpark_launcher.setIdleMode(WL_Spark.IdleMode.kCoast);
      
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        // This method will be called once per scheduler run when in simulation
        switch (currLauncherMode) {
            case RAMPING:
                // we are ramping to curret Speed
                double currActualPower = CanSpark_launcher.get();
                if (CommonLogic.isInRange(currActualPower, currRequestedPower, STEP_RANGE) ){
                    //We are close to running speed go to pid control
                    currLauncherMode = LauncherModes.RUNNING;
                    PIDcalc.resetErrors();
                }
                else {
                    //We are not close to requested speed keep ramping it up
                    CanSpark_launcher.set(currActualPower + currPowerStep);
                }
                break;
            case RUNNING:
                // we are running under PID Control
                PIDv = calcpowrdiff(CanSpark_launcher.getVelocity(), iTargetRPM);
                setpower(CommonLogic.CapMotorPower(PIDv + currRequestedPower, kMinOutput, kMaxOutput));
                break;
            case STOPPED:
                // We are stopped and waiting for new RPM to run to
                iTargetRPM = 0;
                setpower(0.0);
                PIDcalc.resetErrors();
                break;
            default:
                //This should never happen
                currLauncherMode = LauncherModes.STOPPED;
        }
    }

    public void setTargetRPM(Double newTargetRPM) {
        
        // Saftey check if setting to 0 then just call stop and quit
        if (CommonLogic.isInRange(newTargetRPM,0.0,5 )) {
            stop();
            return;
        }
        iTargetRPM = newTargetRPM;
        currRequestedPower = iTargetRPM / LAUNCHER_MAX_RPM;
        currLauncherMode = LauncherModes.RAMPING;
        currPowerStep = (currRequestedPower - CanSpark_launcher.get()) / RAMP_STEPS;
    }

    private void setpower(double power) {
        CanSpark_launcher.set(power);
    }

    // Stop the flywheel
    public void stop() {
        iTargetRPM = 0.0;
        currRequestedPower = 0.0;
        CanSpark_launcher.set(currRequestedPower);
        currLauncherMode = LauncherModes.STOPPED;
        
    }

    @Override
    public void simulationPeriodic() {

    }

    public double getPIDv() {
        return PIDv;
    }

    private double calcpowrdiff(Double curentSpeed, double targetSpeed) {
        return PIDcalc.calcPID(targetSpeed, curentSpeed) / LAUNCHER_MAX_RPM;

    }
}
