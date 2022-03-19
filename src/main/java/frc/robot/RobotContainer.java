package frc.robot;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import frc.robot.commands.*;
//import frc.robot.commands.CmdTeleDrive;
//import frc.robot.commands.CmdTurnByGyro;
//import frc.robot.commands.AutoDriveProfileGyro;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ScheduleCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import com.ctre.phoenix.schedulers.SequentialScheduler;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.BillController;
import frc.robot.Constants.ControllerConstants.RobotMode;
import frc.robot.Constants.limelightConstants.cameras;
import frc.robot.Constants;
import frc.robot.LaunchValues;
import frc.robot.BillController.HAND;
import frc.robot.subsystems.SubClimber;
import frc.robot.subsystems.SubDriveTrain;
import frc.robot.subsystems.SubGyro;
import frc.robot.subsystems.SubIndexer;
import frc.robot.subsystems.SubIntake;
import frc.robot.subsystems.SubLauncher;
import frc.robot.subsystems.WL_SubVibration;
import frc.robot.subsystems.WL_SubVibration.VibType;
//import frc.robot.subsystems.SubLauncher;
import frc.robot.subsystems.SubLimelight;

// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot
 * (including subsystems, commands, and button mappings) should be declared
 * here.
 */
public class RobotContainer {

  public static RobotContainer m_robotContainer = new RobotContainer();

  // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
  // The robot's subsystems
  // public final SubDriveTrain subDriveTrain = new SubDriveTrain();
  public final SubDriveTrain subDriveTrain;
  public final SubGyro subGyro;
  public final SubLauncher subLauncher;
  public final SubIndexer subIndexer;
  public final SubIntake subIntake;
  public final SubClimber subClimber;
  public final SubLimelight subLimelightHigh;
  public final SubLimelight subLimelightLow;
  public final WL_SubVibration wl_SubVibration;
  // Joysticks
  // public final Joystick joy3 = new Joystick(2);

  public final Joystick joy2 = new Joystick(2);
  public final Joystick joyRc = new Joystick(0);
  public final BillController Xbox = new BillController(1);

  public static Boolean bLogging = false;
  public boolean bTargetSeen = false;
  public boolean bTargetLock = false;
  public boolean bLauncherVelocityInTol = false;
  
  public static ShuffleboardTab camTab;
  public static ShuffleboardTab Work_Data;
  // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

  // *****uncomment for full robot*******/
  // public final SubClimber subClimber = new SubClimber();
  // public final SubLauncher subLauncher = new SubLauncher();
  // public final SubLimelight subLimeLight = new SubLimelight();
  // ******************** seee above **********/

  // A chooser for autonomous commands
  SendableChooser<Command> m_chooser = new SendableChooser<>();

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {

    // Smartdashboard Subsystems
    subLauncher = new SubLauncher();
    subDriveTrain = new SubDriveTrain();
    subGyro = new SubGyro();
    subGyro.resetNavx();
    subIndexer = new SubIndexer();
    subIntake = new SubIntake();
    subClimber = new SubClimber();
    subLimelightHigh = new SubLimelight(cameras.limelight_high);
    subLimelightLow = new SubLimelight(cameras.limelight_low);
    subLauncher.setCamera(subLimelightHigh);
    wl_SubVibration = new WL_SubVibration();
    

    ShuffleboardTab camTab = Shuffleboard.getTab("Camera");
    ShuffleboardTab Work_Data = Shuffleboard.getTab("Work_Data");
    // SmartDashboard Buttons

    //camTab.addCamera("LimeLight", "limelight-high", "http://10.36.68.12:5800");
    //camTab.add(title, video)
  
    //camTab.getComponents().remove(index);


    // **uncomment for full robot */
    // SmartDashboard.putData("CmdTurnByGyro", new CmdTurnByGyro(0.0,.25, .25));
    // SmartDashboard.putData("AutoDriveProvileByGyro", new
    // AutoDriveProfileGyro(72.0, 72.0, 0.0, 72));

    // SmartDashboard.putData("CmdDriveStraightByGyro", defaultCmd);
    // SmartDashboard.putData("DriveStraght", new CmdAutoDriveStraght(36, 0, 0.15));

    // SmartDashboard.putData("Turn Left", new CmdTurnByGyro(-45.0, -.15, .15));
    // SmartDashboard.putData("Turn Right", new CmdTurnByGyro(45.0, .15, -.15));
    // SmartDashboard.putData("aGrpDrive180", new aGrpDrive180());

    // *****uncomment for full robot*******/
    // SmartDashboard.putData("CmdLauncherStop", new CmdLauncherStop());
    // SmartDashboard.putData("CmdLauncherRun", new CmdLauncherRun( 3000 ));
    // *************** see above****//

    CommandBase defaultCmd = new CmdCommandStart();
    SmartDashboard.setDefaultBoolean("TargetSeen", false);
    SmartDashboard.setDefaultBoolean("TargetLock", false);
    SmartDashboard.setDefaultBoolean("LauncherVelocity",false);
    SmartDashboard.setDefaultNumber("LimeRPM",0.0);
    SmartDashboard.setDefaultNumber("LimeDist", 0.0);

    SmartDashboard.putData("CamHigh Driver",new LChCamPipline(cameras.limelight_high, 0, SubLimelight.CAM_MODE.DRIVERSTATION_FEEDBACK));
    SmartDashboard.putData("CamHigh Target",new LChCamPipline(cameras.limelight_high, 0, SubLimelight.CAM_MODE.VISION_PROCESSING));

    SmartDashboard.putData("CamLow Blue", new LChCamPipline(cameras.limelight_low, 0, SubLimelight.CAM_MODE.VISION_PROCESSING));
    SmartDashboard.putData("CamLow Red",new LChCamPipline(cameras.limelight_low, 1, SubLimelight.CAM_MODE.VISION_PROCESSING));
    SmartDashboard.putData("CamLow Driver",new LChCamPipline(cameras.limelight_low, 1, SubLimelight.CAM_MODE.DRIVERSTATION_FEEDBACK));

    //SmartDashboard.putData("CmdRobotInit", new CmdRobotInit());
    //SmartDashboard.putData("autogrouptestdrive", new autoGroupTestDrive());
    //SmartDashboard.putData("AutoGrp_2Balls", new AutoGrp_2Balls());
    //SmartDashboard.putData("CmdTeleDrive", new CmdTeleDrive());
    // SmartDashboard.putData(getInstance().subLauncher);
    // SmartDashboard.putData(CommandScheduler.getInstance());
    // RobotContainer.camTab.add("PID2", subLauncher.getPIDv());

    // Configure the button bindings
    configureButtonBindings();

    
   

    // Add Auton's to Selection dropdown.
    m_chooser.addOption("V1AutoGrp_2Balls", new AutoGrp_2BallsV1());
    m_chooser.addOption("V2AutoGrp_2Balls", new AutoGrp_2BallsV2());
    m_chooser.addOption("V3AutoGrp_2Balls", new AutoGrp_2BallsV3());
    m_chooser.addOption("4Ball", new AutoGrp_4BallsV2());

    m_chooser.setDefaultOption(defaultCmd.getName().replace(' ', '_'), defaultCmd);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS

    SmartDashboard.putData("Auto Mode", m_chooser);
  }

  public static RobotContainer getInstance() {
    return m_robotContainer;
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing
   * it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=BUTTONS
    // Create some buttons
    // final JoystickButton BtnHitMe = new JoystickButton(joy2, 3);
    // BtnHitMe.whenPressed(new CmdHitMe(),true);

    /**** test xbox controller buttons */
    Xbox.y.whenPressed(new AModeStart());
    Xbox.x.whenPressed(new AModeLaunch());
    Xbox.a.whenPressed(new AModeIntake());
    Xbox.b.whenPressed(new AModeCarry());
    Xbox.start.whenPressed(new AModeClimb());
    // Xbox.start.whenPressed(new CmdClimbEnable());

    Xbox.rb.whenPressed(new CmdIndexerLaunchv2());
    Xbox.rt.whenPressed(new CmdIndexerLaunch());

    Xbox.rightStick.whenPressed(new AModeEmStop());

    // Xbox.lt.whenHeld(new CmdReverseRoller());
    // Xbox.lt.whenReleased(new CmdForwardRoller());

    Xbox.lt.whenPressed(new CmdStopRoller());

    // Xbox.lb.whenPressed(new CmdStopRoller());
    Xbox.lb.whenHeld(new CmdReverseRoller());
    Xbox.lb.whenReleased(new CmdForwardRoller());

    Xbox.dPad.up.whenPressed(new CmdLauncherRun(2000.0));
    Xbox.dPad.left.whenPressed(new CmdLauncherRun(1500.0));
    Xbox.dPad.right.whenPressed(new CmdLauncherRun(3000.0));
    Xbox.dPad.down.whenPressed(new CmdLauncherRun(3500.0));

    /**
     * 
     * final JoystickButton btnLowFLaunch = new JoystickButton(joy2, 5);
     * btnLowFLaunch.whenPressed(new CmdLauncherRun(2300.0));
     * 
     * final JoystickButton btnHighLaunch = new JoystickButton(joy2, 3);
     * btnHighLaunch.whenPressed(new CmdLauncherRun(2750.0));
     * final JoystickButton btnLaunchStop = new JoystickButton(joy2, 4);
     * btnLaunchStop.whenPressed(new CmdLauncherStop());
     * 
     * final JoystickButton btnExstend = new JoystickButton(joy2, 7);
     * btnExstend.whenPressed(new CmdMoveExtender());
     * 
     * final JoystickButton btnExstenderIn = new JoystickButton(joy2, 9);
     * btnExstenderIn.whenPressed(new
     * CmdMoveExtender(subIntake.GetRetractedPosition()));
     * 
     * final JoystickButton btnExtenderHold = new JoystickButton(joy2, 8);
     * btnExtenderHold.whenPressed(new CmdMoveExtender(16));
     * 
     * 
     * final JoystickButton btnReverseRoller = new JoystickButton(joy2, 10);
     * btnReverseRoller.whenHeld(new CmdReverseRoller());
     * btnReverseRoller.whenReleased(new CmdForwardRoller());
     * 
     */
    // final JoystickButton btnRorwardRoller = new JoystickButton(joy2, 8);
    // btnRorwardRoller.whenPressed(new CmdForwardRoller());

    // final JoystickButton btnRollerStop = new JoystickButton(joy2, 11);
    // btnRollerStop.whenPressed(new CmdForwardRoller());

    // final JoystickButton btnIndexerStart = new JoystickButton(joy2, 11);
    // btnIndexerStart.whenPressed(new CmdIndexerStartCollecting());

    // final JoystickButton btmIndexerLaunch = new JoystickButton(joy2, 12);
    // btmIndexerLaunch.whenPressed(new CmdIndexerLaunch());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=BUTTONS
  }

  // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS
  public Joystick getJoyRc() {
    return joyRc;
  }

  public Joystick getJoy2() {
    return joy2;
  }

  /*
   * public Joystick getJoy3() {
   * return joy3;
   * }
   */


  // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getteleCommand() {
    // The selected command will be run in tele
    return m_chooser.getSelected();
  }

  public Command getAutonomousCommand() {
    // The selected command will be run in autonomous
    return m_chooser.getSelected();
  }

  public void updateSmartDash() {
    // get the RPM and Range from the camera 
    double cameraAngle =  50;
    if (subLimelightHigh.hasTarget()){
      cameraAngle = subLimelightHigh.getTY();
      SmartDashboard.putNumber("LimeRPM", LaunchValues.getRPM(cameraAngle));
      SmartDashboard.putNumber("LimeDist", LaunchValues.getRange(cameraAngle));
    }
    else {
      SmartDashboard.putNumber("LimeRPM", LaunchValues.getRPM(cameraAngle));
      SmartDashboard.putNumber("LimeDist", LaunchValues.getRange(cameraAngle));
    }
    SmartDashboard.putBoolean("TargetSeen", bTargetSeen);
    SmartDashboard.putBoolean("TargetLock", bTargetLock);
    bLauncherVelocityInTol = subLauncher.IsVelocityInTol();
    SmartDashboard.putBoolean("LauncherVelocity", bLauncherVelocityInTol);



    //Target Locked to crosshair
    if (bTargetLock ) {
      //Xbox.setRumble(HAND.LEFT, 1.0); 
      wl_SubVibration.SetVib(VibType.TargetLock);
    }
    else{
      //Xbox.setRumble(HAND.LEFT,0);
      wl_SubVibration.SetVib(VibType.STOP);
    }

    // Launcher is at velocity
    if (bLauncherVelocityInTol) {
      Xbox.setRumble(HAND.RIGHT, 1.0);  
    }
    else{
      Xbox.setRumble(HAND.RIGHT, 0);
    }

    SmartDashboard.putNumber("launcher RPM", subLauncher.CanSpark_launcher.getVelocity());   
    SmartDashboard.putNumber("launcher Power",
    subLauncher.CanSpark_launcher.get());

   // SmartDashboard.putNumber(("Transverse Pos"), subClimber.getTransverseCurPos());
    
    
    //update Camera tab
    //Shuffleboard.getTab("Camera").add("PID1", subLauncher.getPIDv());
  
    // SmartDashboard.putNumber("Norm Angle", subGyro.getNormaliziedNavxAngle());

   // Work_Data.add("Left Encoder", subDriveTrain.getEncoderPosLeft());
   // Work_Data.add("Right Encoder", subDriveTrain.getEncoderPosRight());
   // Work_Data.add("launcher Power", subLauncher.CanSpark_launcher.get());
   // Work_Data.add("PID", subLauncher.getPIDv());
   // Work_Data.add("launcher RPM", subLauncher.CanSpark_launcher.getVelocity());
   // Work_Data.add("Norm Angle", subGyro.getNormaliziedNavxAngle());

    // SmartDashboard.putNumber("Left Encoder", subDriveTrain.getEncoderPosLeft());
    // SmartDashboard.putNumber("Right Encoder",
    // subDriveTrain.getEncoderPosRight());
         // SmartDashboard.putNumber("PID", subLauncher.getPIDv());
    // SmartDashboard.putNumber("climbTarPos", subClimber.getClimbTarPos());
     SmartDashboard.putNumber("climbPos", subClimber.getClimbCurPos());
    // SmartDashboard.putBoolean("climbEnable", subClimber.getbclimb());

  }

}
