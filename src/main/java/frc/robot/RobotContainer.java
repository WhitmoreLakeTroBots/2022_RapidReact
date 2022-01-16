// RobotBuilder Version: 4.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

// ROBOTBUILDER TYPE: RobotContainer.

package frc.robot;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.commands.*;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.subsystems.SubClimber;
import frc.robot.subsystems.SubDriveTrain;
import frc.robot.subsystems.SubGyro;
import frc.robot.subsystems.SubLauncher;
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

  private static RobotContainer m_robotContainer = new RobotContainer();

  // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
  // The robot's subsystems
  

  // Joysticks
  private final Joystick joy3 = new Joystick(2);
  private final Joystick joy2 = new Joystick(1);
  private final Joystick joyRc = new Joystick(0);

  // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

  public final SubClimber subClimber = new SubClimber();
  public final SubLauncher subLauncher = new SubLauncher();
  public final SubDriveTrain subDriveTrain = new SubDriveTrain();
  public final SubGyro subGyro = new SubGyro();
  public final SubLimelight subLimeLight = new SubLimelight();

  // A chooser for autonomous commands
  SendableChooser<Command> m_chooser = new SendableChooser<>();

  /**
  * The container for the robot.  Contains subsystems, OI devices, and commands.
  */
  private RobotContainer() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=SMARTDASHBOARD
    // Smartdashboard Subsystems


    // SmartDashboard Buttons
    SmartDashboard.putData("CmdTurnByGyro", new CmdTurnByGyro( subDriveTrain ));
    CommandBase defaultCmd = new AutoDriveProfileGyro( subDriveTrain, subGyro );
    SmartDashboard.putData("CmdDriveStraightByGyro", defaultCmd);
    SmartDashboard.putData("CmdLauncherStop", new CmdLauncherStop( subLauncher ));
    SmartDashboard.putData("CmdLauncherStart", new CmdLauncherStart( subLauncher ));
	  SmartDashboard.putData("CmdTeleOp", new CmdTeleOp( subDriveTrain ));
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=SMARTDASHBOARD
    // Configure the button bindings
    configureButtonBindings();

    // Configure default commands
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=SUBSYSTEM_DEFAULT_COMMAND


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=SUBSYSTEM_DEFAULT_COMMAND

    // Configure autonomous sendable chooser
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS

    // This m_chooser line is the issue from Robot Builder that is causing issues.    
    //m_chooser.setDefaultOption(defaultCmd.getName(), new String (defaultCmd.getName().replace(' ', '')),( m_${name.substring(0,1).toLowerCase()}${name.substring(1).replace(' ', '')} ));
    
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

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=BUTTONS
  }

  // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS
  public Joystick getJoyRc() {
    return joyRc;
  }

  public Joystick getJoy2() {
    return joy2;
  }

  public Joystick getJoy3() {
    return joy3;
  }

  // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // The selected command will be run in autonomous
    return m_chooser.getSelected();
  }

}
