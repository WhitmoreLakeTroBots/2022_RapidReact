package frc.robot.subsystems;

import frc.robot.Constants.CAN_ID_Constants;
import frc.robot.Constants.JoyStick_Constants;
import frc.robot.hardware.WL_Spark;
import frc.robot.Constants;
import frc.robot.RobotMath;
import frc.robot.CommonLogic;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.commands.CmdTeleDrive;
public class SubDriveTrain extends SubsystemBase {

    private WL_Spark CanSpark_driveLeft_1;
    private WL_Spark CanSpark_driveLeft_2;
    private WL_Spark CanSpark_driveRight_1;
    private WL_Spark CanSpark_driveRight_2;


    private final double kDT_powerRightScaler = 1.0;
    private final double kDT_powerLeftScaler = 1.0;
    private final double kMinThrottle = -1.0;
    private final double kMaxThrottle = 1.0;

    public final double KWeelDiamiter = 6;
    public final double kWheelCircumference = 6.0*Math.PI;
    public final double kGearBoxRatio = 8.45;
    //8.45 12.75 10.71 normal gearboxs ratios
    public final double kMaxInchesPerSecond = 13.88 * 12;    //  inches  / per second

    public final double kp_DriveStraightGyro = 0.006;
    public final double ki_DriveStraightGyro = 0.0; 
    public final double kd_DriveStraightGyro = 0.0; 
    

    
    public SubDriveTrain() {

       // setDefaultCommand(new CmdTeleDrive());
        
        //for full chassis uncomment other motors
        CanSpark_driveLeft_1 = new WL_Spark(CAN_ID_Constants.kCanID_DriveTrain_left_1, WL_Spark .MotorType.kBrushless);
        CanSpark_driveLeft_2 = new WL_Spark(CAN_ID_Constants.kCanID_DriveTrain_left_2, WL_Spark .MotorType.kBrushless);
        CanSpark_driveRight_1 = new WL_Spark(CAN_ID_Constants.kCanID_DriveTrain_right_1, WL_Spark .MotorType.kBrushless);
        CanSpark_driveRight_2 = new WL_Spark(CAN_ID_Constants.kCanID_DriveTrain_right_2, WL_Spark .MotorType.kBrushless);

        
        CanSpark_driveLeft_1.restoreFactoryDefaults();
        CanSpark_driveLeft_2.restoreFactoryDefaults();
        CanSpark_driveRight_1.restoreFactoryDefaults();
        CanSpark_driveRight_2.restoreFactoryDefaults();

        CanSpark_driveLeft_1.setInverted(false);
        CanSpark_driveLeft_2.setInverted(false);
        CanSpark_driveRight_1.setInverted(true);
        CanSpark_driveRight_2.setInverted(true);

        int maxCurrent = 40;
        CanSpark_driveLeft_1.setSmartCurrentLimit(maxCurrent);
        CanSpark_driveLeft_2.setSmartCurrentLimit(maxCurrent);
        CanSpark_driveRight_1.setSmartCurrentLimit(maxCurrent);
        CanSpark_driveRight_2.setSmartCurrentLimit(maxCurrent);

        // Set Brake Mode for the motors so we stop when controls are let go of
        CanSpark_driveLeft_1.setIdleMode(WL_Spark.IdleMode.kBrake);
        CanSpark_driveLeft_2.setIdleMode(WL_Spark.IdleMode.kBrake);
        CanSpark_driveRight_1.setIdleMode(WL_Spark.IdleMode.kBrake);
        CanSpark_driveRight_2.setIdleMode(WL_Spark.IdleMode.kBrake);


        // Set the 2's to follow the 1's
        CanSpark_driveLeft_2.follow(CanSpark_driveLeft_1);
        CanSpark_driveRight_2.follow(CanSpark_driveRight_1);

        //https://codedocs.revrobotics.com/java/com/revrobotics/cansparkmax#setOpenLoopRampRate(double)
        double ramprate = 0.40;
        CanSpark_driveLeft_1.setOpenLoopRampRate(ramprate);
        CanSpark_driveLeft_2.setOpenLoopRampRate(ramprate);
        CanSpark_driveRight_1.setOpenLoopRampRate(ramprate);
        CanSpark_driveRight_2.setOpenLoopRampRate(ramprate);

        // burn new settings in to survive a brownout
        CanSpark_driveLeft_1.burnFlash();
        CanSpark_driveLeft_2.burnFlash();
        CanSpark_driveRight_1.burnFlash();
        CanSpark_driveRight_2.burnFlash();

    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run

    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run when in simulation
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private void setPower_LeftDrive(double pwrPercent) {
        double power = CommonLogic.CapMotorPower(pwrPercent * kDT_powerLeftScaler, kMinThrottle, kMaxThrottle);
        CanSpark_driveLeft_1.set(power);
         CanSpark_driveLeft_2.set(power);
    }

    private void setPower_RightDrive(double pwrPercent) {
        double power = CommonLogic.CapMotorPower(pwrPercent * kDT_powerRightScaler, kMinThrottle, kMaxThrottle);
        CanSpark_driveRight_1.set(power);
         CanSpark_driveRight_2.set(power);
    }

    /**
     * Accepting a joystick it will deadband and square the values
     * and pass them off to the the auton method to be used to control the
     * chassis wheel motors
     * 
     * @param stick joyStick that currently has control of the wheels
     */
    public void Drive(Joystick stick) {
         double joyX = CommonLogic.joyDeadBand(stick.getX(), JoyStick_Constants.DriveDeadband);
         double joyY = CommonLogic.joyDeadBand(-stick.getY(), JoyStick_Constants.DriveDeadband);
        
        //car turn go burr
        if(joyY <= -0.05){
            joyX = joyX * -1;
        }
         
         Drive((joyY - joyX), (joyY + joyX));
        
        
    }

    /**
     * Accepting a percenage of the motor velocities for left and right
     * sides of the robot to allow command to steer the robot
     * 
     * @param leftRPM  -- percentage of max RPM for the motors
     * @param rightRPM -- percentage of max RPM for the motors
     */
    public void Drive(double powerLeft, double powerRight) {

        setPower_RightDrive(powerRight);
        setPower_LeftDrive(powerLeft);
    }

    public void aimLaunchger(double cameraAngle){
        
        double _minLeftThrottle = 0.0;
        double _minRightThrottle = 0.0;

        double _leftTargetThrottle = 0.0;
        double _rightTargetThrottle = 0.0;
        double _KPLeft = 0.0;
        double _KPRight = 0.0;

        if (cameraAngle > 0) {
            _leftTargetThrottle = .2;
            _minLeftThrottle = Constants.robotPysicalProperties.minTurnSpeed;
            _rightTargetThrottle = -_leftTargetThrottle;
            _minRightThrottle = -_minLeftThrottle;
        } else if (cameraAngle < 0) {
            _leftTargetThrottle = -.2;
            _minLeftThrottle = -Constants.robotPysicalProperties.minTurnSpeed;
            _rightTargetThrottle = -_leftTargetThrottle;
            _minRightThrottle = -_minLeftThrottle;
        } 

        //looking for cameraAngle=0 with tol=2 degrees
        if (CommonLogic.isInRange(cameraAngle, 0, 2)){
            stop();
        }
        else{
            _KPLeft = RobotMath.calcKP(_leftTargetThrottle, _minLeftThrottle);
            _KPRight = RobotMath.calcKP(_rightTargetThrottle, _minRightThrottle);
            double powerLeft = RobotMath.calcMotorPower(_leftTargetThrottle, _minLeftThrottle, _KPLeft, cameraAngle);
            double powerRight = RobotMath.calcMotorPower(_rightTargetThrottle, _minRightThrottle, _KPRight, cameraAngle);
            Drive(powerLeft, powerRight);
        }
    }


    public double getEncoderPos_LR() {
        // average the 2 encoders to get the real robot position
        return ((getEncoderPosRight() + getEncoderPosLeft()) / 2);
    }

    public double getEncoderPosLeft() {
        // get the left side of the robot position
        return CanSpark_driveLeft_1.getPosition();
    }

    public double getEncoderPosLeft_Inches() {
        return revs2Inches(getEncoderPosLeft());
    }

    public double getEncoderPosRight() {
        // get the right side of the robot position
        return CanSpark_driveRight_1.getPosition();
    }

    public double getEncoderPosRight_Inches() {
        return revs2Inches(getEncoderPosRight());
    }

    public void resetEncoder_LeftDrive() {
        CanSpark_driveLeft_1.resetEncoder();
    }

    public void resetEncoder_RightDrive() {
        CanSpark_driveRight_1.resetEncoder();
    }

    public void resetBothEncoders (){
        resetEncoder_LeftDrive();
        resetEncoder_RightDrive();
    }

    public double getEncoder_Inches_LR() {

        return revs2Inches(getEncoderPos_LR());
    }

    public double inches2MotorRevs(double inches) {
        // convert inches to motor Revs
        return (inches / kWheelCircumference / Math.PI / kGearBoxRatio);
        // 72 / 6 / Math.PI * 8.45 = 32.2766224
    }

    public void winMatch() {

    }

    public double inches_sec2RPM(double inches_sec) {
        // converts inches/sec to Revs/minute
        return inches2MotorRevs(inches_sec) * 60;
    }

    public double revs2Inches(double Revs) {
        return (Revs / kGearBoxRatio * kWheelCircumference);
    }

    public void stop() {

        CanSpark_driveRight_1.set(0);
        CanSpark_driveRight_2.set(0);
        CanSpark_driveLeft_1.set(0);
        CanSpark_driveLeft_2.set(0);
    }
    //@Override
  //public void initDefaultCommand() {
   // setDefaultCommand(new cmdTeleDrive());

  //}
  public double getpower() {
     return CanSpark_driveLeft_1.get();
  }

  public void SetBrakeMode (WL_Spark.IdleMode newMode){
      
      // Set Brake Mode for the motors so we stop when controls are let go of
      CanSpark_driveLeft_1.setIdleMode(newMode);
      CanSpark_driveLeft_2.setIdleMode(newMode);
      CanSpark_driveRight_1.setIdleMode(newMode);
      CanSpark_driveRight_2.setIdleMode(newMode);

  }


}
