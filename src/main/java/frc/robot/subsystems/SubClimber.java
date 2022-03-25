package frc.robot.subsystems;

//import frc.robot.commands.*;
//import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS

// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS

import frc.robot.CommonLogic;

import frc.robot.hardware.WL_Spark;
import frc.robot.Constants.CAN_ID_Constants;

//import com.revrobotics.SparkMaxPIDController;

/**
 *
 */
public class SubClimber extends SubsystemBase {

    private WL_Spark CanSpark_Climber_1;
    private WL_Spark CanSpark_Climber_2;

    private WL_Spark CanSpark_Transverse;

    private double Climb_MaxPos = 106;
    private double Climb_MinPos = 0;
    private double Climb_Grab = 84;
    private double Climb_Release = 18;
    private double Climb_Pass = 31;

    private double Climb_RetractPOS = 0;
    private double Climb_ExtendPos = 102;

    private double Climb_TargetPos = 0;
    private double Climb_Tol = 3;
    private double Climb_hold_power = -.03;
    private double Climb_power = 0.4;
    private boolean bClimb = false;

    private double Transverse_MaxPos = 133;
    private double Transverse_MinPos = -117;
    
    private double Transverse_Grab = -85;
    private double Transverse_Release = 132;
    private double Transverse_RetractPOS = 0;
    private double Transverse_ExtendPos = 118;

    private double Transverse_TargetPos = 0;
    private double Transverse_Tol = 3;
    private double Transverse_power = 0.7;
    private boolean bTransverse = false;

    public SubClimber() {

        // for full chassis uncomment other motors
        CanSpark_Climber_1 = new WL_Spark(CAN_ID_Constants.kCanID_Climber_1, WL_Spark.MotorType.kBrushless);
        CanSpark_Climber_2 = new WL_Spark(CAN_ID_Constants.kCanID_Climber_2, WL_Spark.MotorType.kBrushless);
        CanSpark_Transverse = new WL_Spark(CAN_ID_Constants.kCanID_Climb_Transversal, WL_Spark.MotorType.kBrushless);

        CanSpark_Climber_1.restoreFactoryDefaults();
        CanSpark_Climber_2.restoreFactoryDefaults();
        CanSpark_Transverse.restoreFactoryDefaults();

        CanSpark_Climber_1.setInverted(false);
        CanSpark_Climber_2.setInverted(true);
        CanSpark_Transverse.setInverted(false);

        int maxCurrent = 50;
        CanSpark_Climber_1.setSmartCurrentLimit(maxCurrent);
        CanSpark_Climber_2.setSmartCurrentLimit(maxCurrent);
        CanSpark_Transverse.setSmartCurrentLimit(maxCurrent);

        CanSpark_Climber_1.setIdleMode(WL_Spark.IdleMode.kBrake);
        CanSpark_Climber_2.setIdleMode(WL_Spark.IdleMode.kBrake);

        CanSpark_Transverse.setIdleMode(WL_Spark.IdleMode.kBrake);

        // CanSpark_Climber_2.follow(CanSpark_Climber_1);

        // burn new settings in to survive a brownout
        CanSpark_Climber_1.burnFlash();
        CanSpark_Climber_2.burnFlash();
        CanSpark_Transverse.burnFlash();

        // addChild("CanSpark_Climber_1", CanSpark_Climber_1);
        // addChild("CanSpark_Climber_2", CanSpark_Climber_2);

    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        if (bClimb) {
            gotoPositonClimb();
        } else {
            Climb_TargetPos = CanSpark_Climber_1.getPosition();
            CanSpark_Climber_1.set(0);
            CanSpark_Climber_2.set(0);
        }
        if (bTransverse) {
            gotoPositonTraversal();
        } else {
            Transverse_TargetPos = CanSpark_Transverse.getPosition();
            CanSpark_Transverse.set(0);
        }

    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run when in simulation

    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void EnableClimb() {
        bClimb = true;
        bTransverse = true;
    }

    public void DisableClimb() {
        bClimb = false;
        bTransverse = false;
    }

    public double getClimbCurPos() {
        return CanSpark_Climber_1.getPosition();
    }

    public double getClimbTarPos() {
        return Climb_TargetPos;
    }

    public double getClimbExtenPos() {
        return Climb_ExtendPos;
    }

    public double getClimbRetractPos() {
        return Climb_RetractPOS;
    }

    public double getClimbTolPos() {
        return Climb_Tol;
    }

    public double getClimbGrabPos(){
        return Climb_Grab;
    }

    public double getClimbReleasePos(){
        return Climb_Release;
    }

    public double getClimbPass(){
        return Climb_Pass;
    }

    public double getTransverseGrabPos(){
        return Transverse_Grab;
    }

    public double getTransverseReleasePos(){
        return Transverse_Release;
    }

    public boolean getbclimb() {
        return bClimb;
    }

    public double getTransverseCurPos() {
        return CanSpark_Transverse.getPosition();
    }

    public double getTransverseTarPos() {
        return Transverse_TargetPos;
    }

    public double getTransverseExtenPos() {
        return Transverse_ExtendPos;
    }

    public double getTransverseRetractPos() {
        return Transverse_RetractPOS;
    }
    

    public double getTransverseTolPos() {
        return Transverse_Tol;
    }

    public void SetClimbPos(double newTargetPosition) {
        Climb_TargetPos = CommonLogic.CapMotorPower(newTargetPosition, Climb_MinPos, Climb_MaxPos);

    }

    public void SetTransversePos(double newTargetPosition) {
        Transverse_TargetPos = CommonLogic.CapMotorPower(newTargetPosition, Transverse_MinPos, Transverse_MaxPos);

    }

    private void gotoPositonClimb() {
        // if current position is less than targetPostion
        if (CommonLogic.isInRange(CanSpark_Climber_1.getPosition(), Climb_TargetPos, Climb_Tol)) {
            // than stop
            CanSpark_Climber_1.set(Climb_hold_power);
            CanSpark_Climber_2.set(Climb_hold_power);
        }

        // else if current positon is greater than target postion
        else if (CanSpark_Climber_1.getPosition() > Climb_TargetPos + Climb_Tol) {
            // then apply (-) RetractorPower
            CanSpark_Climber_1.set(-1 * Climb_power);
            CanSpark_Climber_2.set(-1 * Climb_power);
        }
        // else if if current position is commonlogic inRange
        else if (CanSpark_Climber_1.getPosition() < Climb_TargetPos - Climb_Tol) {
            // then apply RetractorPower
            CanSpark_Climber_1.set(Climb_power);
            CanSpark_Climber_2.set(Climb_power);
            System.err.print("add power");
        }
    }

    private void gotoPositonTraversal() {
        // if current position is less than targetPostion
        if (CommonLogic.isInRange(CanSpark_Transverse.getPosition(), Transverse_TargetPos, Transverse_Tol)) {
            // than stop
            CanSpark_Transverse.set(0);
        }

        // else if current positon is greater than target postion
        else if (CanSpark_Transverse.getPosition() > Transverse_TargetPos + Transverse_Tol) {
            // then apply (-) RetractorPower
            CanSpark_Transverse.set(-1 * Transverse_power);
        }
        // else if if current position is commonlogic inRange
        else if (CanSpark_Transverse.getPosition() < Transverse_TargetPos - Transverse_Tol) {
            // then apply RetractorPower
            CanSpark_Transverse.set(Transverse_power);
        }
    }

    public void climbMan(double direction) {

        if (direction >= 0.1) {

            // SetClimbPos(getClimbTarPos() + (direction ));
            SetClimbPos(Climb_ExtendPos);
        } else if (direction <= -0.1) {

            // SetClimbPos(getClimbTarPos() + (direction ));
            SetClimbPos(Climb_RetractPOS);
        }
    }

    public void climbMan2(double direction) {

        if (direction >= 0.1) {

            // SetClimbPos(getClimbTarPos() + (direction ));
            SetClimbPos(Climb_Grab);
        } else if (direction <= -0.1) {

            // SetClimbPos(getClimbTarPos() + (direction ));
            SetClimbPos(Climb_Release);
        }
    }

    public void transverseMan(double direction) {
        System.err.print("trans " + Transverse_TargetPos);
        if (direction >= 0.1) {

            SetTransversePos(getTransverseTarPos() + (direction));
        } else if (direction <= -0.1) {

            SetTransversePos(getTransverseTarPos() + (direction));
        }

    }

    public void zeroHoldPower() {
        CanSpark_Climber_1.set(0);
        CanSpark_Climber_2.set(0);
    }
}
