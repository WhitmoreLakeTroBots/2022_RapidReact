
package frc.robot;

import edu.wpi.first.hal.FRCNetComm.tInstances;
import edu.wpi.first.hal.FRCNetComm.tResourceType;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.ScheduleCommand;
import frc.robot.commands.CmdTeleDrive;
import frc.robot.commands.CmdVib;
import frc.robot.commands.CmdMoveExtender;
import frc.robot.commands.CmdRemapController;
import frc.robot.hardware.WL_Spark;
import frc.robot.subsystems.SubLimelight;
import frc.robot.subsystems.SubLimelight.LED_MODE;
import frc.robot.subsystems.WL_SubVibration.VibType;
import edu.wpi.first.util.net.PortForwarder;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in
 * the project.
 */
public class Robot extends TimedRobot {

    private Command m_autonomousCommand;
    private Command m_teleCommand;

    private RobotContainer m_robotContainer;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
        // Instantiate our RobotContainer. This will perform all our button bindings,
        // and put our
        // autonomous chooser on the dashboard.
        m_robotContainer = RobotContainer.getInstance();
        HAL.report(tResourceType.kResourceType_Framework, tInstances.kFramework_RobotBuilder);

        // Make sure you only configure port forwarding once in your robot code.
        // Do not place these function calls in any periodic functions
        //PortForwarder.add(5800, "10.36.68.11", 5800);
        //PortForwarder.add(5801, "10.36.68.11", 5801);
        //PortForwarder.add(5802, "10.36.68.11", 5802);
        //PortForwarder.add(5803, "10.36.68.11", 5803);
        //PortForwarder.add(5804, "10.36.68.11", 5804);
        //PortForwarder.add(5805, "10.36.68.11", 5805);

        PortForwarder.add(5800, "10.36.68.12", 5800);
        PortForwarder.add(5801, "10.36.68.12", 5801);
        PortForwarder.add(5802, "10.36.68.12", 5802);
        PortForwarder.add(5803, "10.36.68.12", 5803);
        PortForwarder.add(5804, "10.36.68.12", 5804);
        PortForwarder.add(5805, "10.36.68.12", 5805);
    }

    /**
     * This function is called every robot packet, no matter the mode. Use this for
     * items like
     * diagnostics that you want ran during disabled, autonomous, teleoperated and
     * test.
     *
     * <p>
     * This runs after the mode specific periodic functions, but before
     * LiveWindow and SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {
        // Runs the Scheduler. This is responsible for polling buttons, adding
        // newly-scheduled
        // commands, running already-scheduled commands, removing finished or
        // interrupted commands,
        // and running subsystem periodic() methods. This must be called from the
        // robot's periodic
        // block in order for anything in the Command-based framework to work.
        CommandScheduler.getInstance().run();
    }

    /**
     * This function is called once each time the robot enters Disabled mode.
     */
    @Override
    public void disabledInit() {

        // stop things so they do not take off again when enabled next time
        RobotContainer.getInstance().subLauncher.AutoRPM_set(false);
        RobotContainer.getInstance().subLauncher.stop();
        RobotContainer.getInstance().subDriveTrain.SetBrakeMode(WL_Spark.IdleMode.kBrake);
        RobotContainer.getInstance().subDriveTrain.stop();
        RobotContainer.getInstance().subIndexer.stopTrack();
        RobotContainer.getInstance().subIndexer.FeederStop();
        RobotContainer.getInstance().subIntake.stopRoller();
        //RobotContainer.getInstance().subClimber.zeroHoldPower();
        //RobotContainer.getInstance().subLimelightHigh.setLEDMode(LED_MODE.OFF);
        //RobotContainer.getInstance().subLimelightLow.setLEDMode(LED_MODE.OFF);
    }

    @Override
    public void disabledPeriodic() {
        RobotContainer.getInstance().updateSmartDash();
    }

    /**
     * This autonomous runs the autonomous command selected by your
     * {@link RobotContainer} class.
     */
    @Override
    public void autonomousInit() {
        m_autonomousCommand = m_robotContainer.getAutonomousCommand();
        m_teleCommand = m_robotContainer.getteleCommand();
        RobotContainer.getInstance().subLimelightHigh.setLEDMode(LED_MODE.ON);
        // schedule the autonomous command (example)
        if (m_autonomousCommand != null) {
            m_autonomousCommand.schedule();
        }
    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
        RobotContainer.getInstance().updateSmartDash();
    }

    @Override
    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (m_autonomousCommand != null) {
            m_autonomousCommand.cancel();
        }
        RobotContainer.getInstance().subLimelightHigh.setLEDMode(LED_MODE.ON);

    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
        // System.err.println("***TeleopPeriodic");
        RobotContainer.getInstance().updateSmartDash();
        double cameraAngle = RobotContainer.getInstance().subLimelightHigh.getTX();
        double aimTrigger = RobotContainer.getInstance().joyRc.getZ();

        if ((aimTrigger > .5) && RobotContainer.getInstance().bTargetSeen) {
            RobotContainer.getInstance().subDriveTrain.aimLaunchger(cameraAngle);
        } else {
            RobotContainer.getInstance().subDriveTrain.Drive(RobotContainer.getInstance().joyRc);
        }
        //RobotContainer.getInstance().subClimber.ClimbIntake(RobotContainer.getInstance().Xbox.rightStick.getY());
        RobotContainer.getInstance().subClimber.climbMan(RobotContainer.getInstance().Xbox.leftStick.getY());
        //RobotContainer.getInstance().subClimber.climbMan2(RobotContainer.getInstance().Xbox.leftStick.getX());
        // RobotContainer.getInstance().subClimber.transverseMan(RobotContainer.getInstance().Xbox.rightStick.getX());
        //RobotContainer.getInstance().subClimber.transverseMan2(RobotContainer.getInstance().Xbox.rightStick.getY());
        
        

        if(RobotContainer.getInstance().bLauncherVelocityInTol == true){
            RobotContainer.getInstance().wl_SubVibration.SetVib(VibType.LauncherSpeedSet);
         }

         if(RobotContainer.getInstance().bTargetLock == true){
            RobotContainer.getInstance().wl_SubVibration.SetVib(VibType.TargetLock);
         }

         if(RobotContainer.getInstance().subIntake.RollerAmps >= 8 && RobotContainer.getInstance().subIntake.RollerAmps <= 15 ){
            RobotContainer.getInstance().wl_SubVibration.SetVib(VibType.BallCaptured);
         }

    }

    @Override
    public void testInit() {
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll();
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
        System.err.println("testMode");
    }

}
