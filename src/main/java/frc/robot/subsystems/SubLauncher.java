package frc.robot.subsystems;

import frc.robot.LaunchValues;
import frc.robot.CommonLogic;
import frc.robot.Constants.*;
import frc.robot.hardware.WL_Spark;
import frc.robot.PID;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SubLauncher extends SubsystemBase {
    public double kP, kI, kD, kIz, kFF, maxRPM, minVEL;
    public final double kMaxOutput = 1.0;
    public final double kMinOutput = -1.0;
    public PID PIDcalc;
    private SubLimelight subLimeLight = null;
    private double iTargetRPM = 0;
    private double iActualRPM = 0;
    private boolean bAutoRPMEnabled = false;
    public WL_Spark CanSpark_launcher;
    private double currRequestedPower = 0.0; // current power requests
    private double currActualPower = 0.0; 
    private double currPowerStep = 0;        // how large of steps to take for ramping
    private double PIDv = 0;
    private final double LAUNCHER_MAX_RPM = 5676;
    private final int RAMP_STEPS = 25;
    private final double STEP_RANGE = 2.0 / RAMP_STEPS;

    public enum LauncherModes {
        RAMPING,
        RUNNING,
        STOPPED
    }

    private LauncherModes currLauncherMode = LauncherModes.STOPPED;

    public SubLauncher() {
        kP = 1.2;
        kI = 5e-3;
        kD = 0.0;
        kIz = 0;
        kFF = 0;

        CanSpark_launcher = new WL_Spark(CAN_ID_Constants.kCanID_Launcher_1, WL_Spark.MotorType.kBrushless);
        PIDcalc = new PID(kP, kI, kD);
        CanSpark_launcher.restoreFactoryDefaults();
        CanSpark_launcher.setInverted(false);
        CanSpark_launcher.setSmartCurrentLimit(80);
        CanSpark_launcher.setIdleMode(WL_Spark.IdleMode.kCoast);

    }

    // use this camera to control the launcher rpm
    public void setCamera(SubLimelight limeCamera) {
        subLimeLight = limeCamera;
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run when in simulation

        //make sure we have set a camera
        if (subLimeLight != null) {
            // make sure we are under limelight control
            double cameraAngle = 0.0;
            if (bAutoRPMEnabled) {
                cameraAngle = subLimeLight.getTY();
                setTargetRPM(LaunchValues.getRPM(cameraAngle));
            }
        }

        iActualRPM = CanSpark_launcher.getVelocity();
        switch (currLauncherMode) {
            case RAMPING:
                // we are ramping to curret Speed
                // Are we within %5 ?
                //if (IsVelocityInTol(0.025)){
                if (CommonLogic.isInRange(currActualPower, currRequestedPower, STEP_RANGE)) {
                    // We are close to running speed go to pid control
                    setpower(currRequestedPower);
                    currLauncherMode = LauncherModes.RUNNING;
                    PIDcalc.resetErrors();
                } else {
                    // We are not close to requested velocity keep ramping it up
                    setpower(currActualPower + currPowerStep);
                }
                break;
            case RUNNING:
                // we are running under PID Control
                PIDv = calcpowrdiff(iActualRPM, iTargetRPM);
                setpower(PIDv + currRequestedPower);
                break;
            case STOPPED:
                // We are stopped and waiting for new RPM to run to
                iTargetRPM = 0;
                setpower(0.0);
                PIDcalc.resetErrors();
                break;
            default:
                // This should never happen
                currLauncherMode = LauncherModes.STOPPED;
        }
    }

    public void setTargetRPM(Double newTargetRPM) {

        // Saftey check if setting to 0 then just call stop and quit
        if (CommonLogic.isInRange(newTargetRPM, 0.0, 5)) {
            stop();
            return;
        }
        iTargetRPM = newTargetRPM;
        currRequestedPower = iTargetRPM / LAUNCHER_MAX_RPM;
        currLauncherMode = LauncherModes.RAMPING;
        currPowerStep = (currRequestedPower - currActualPower) / RAMP_STEPS;
    }

    public double getTargetRPM() {
        return iTargetRPM;
    }

    public double getActualRPM() {
        return iActualRPM;
    }

    private void setpower(double power) {
        // be sure to cap the power between 0.0 (stopped) and 1.0;
        currActualPower = CommonLogic.CapMotorPower(power, kMinOutput, kMaxOutput);
        CanSpark_launcher.set(currActualPower);
    }

    // Stop the flywheel
    public void stop() {
        iTargetRPM = 0.0;
        AutoRPM_set(false);
        currRequestedPower = 0.0;
        setpower(currRequestedPower);
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

    // IS the launcher RPM in a small tight range of values
    public boolean IsVelocityInTol(double percent) {
        double pcnt = CommonLogic.CapMotorPower(percent, 0.0, 1.0);
        return CommonLogic.isInRange(iActualRPM, iTargetRPM, (iTargetRPM * pcnt));
    }


    // Enable/Disable the AutoRPM Logic
    public boolean AutoRPM_get() {
        return bAutoRPMEnabled;
    }

    public void AutoRPM_set(boolean newValue) {
        bAutoRPMEnabled = newValue;
    }

    public void AutoRPM_toggle() {
        bAutoRPMEnabled = !bAutoRPMEnabled;
    }

}
