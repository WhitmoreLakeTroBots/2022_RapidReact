package frc.robot.commands;

import java.lang.module.ModuleDescriptor.Requires;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.RobotMath;
import frc.robot.subsystems.SubDriveTrain;
import frc.robot.subsystems.SubLimelight;
import frc.robot.Constants.limelightConstants.cameras;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class CmdAutoDriveStraghtByLime extends CommandBase {

  private double targetPosinch = 36;
  private double speedPercent = .15;
  private double Heading = 0;
  private boolean bDone = false;
  private final double overShoot = 4;
  private SubDriveTrain subDriveTrain = null;
  private cameras camera2lookthrough = null;
  private int camPipeline = 0;
  private SubLimelight camera = null;
  private int targetCounter = 0;

  private enum TARGET_STATUS{
    NeverSeen,            // We have yet to see the target
    CurrentlyTracking,    // We are currently able to see it
    Lost                  // We were tracking it but it has been lost
  }

  private final int MINTARGETHITS = 5;  // We have to see the target 5 times to say we can really see it

  public CmdAutoDriveStraghtByLime(double distanceIn, double headingDeg, double powerPer, cameras cam, int pipeline) {

    targetPosinch = distanceIn;
    Heading = headingDeg;
    speedPercent = powerPer;
    camera2lookthrough = cam;
    camPipeline = pipeline;

    // addRequirements(RobotContainer.getInstance().subDriveTrain);

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.err.println("initialize");
    bDone = false;

    subDriveTrain = RobotContainer.getInstance().subDriveTrain;
    subDriveTrain.resetBothEncoders();
    subDriveTrain.Drive(speedPercent, speedPercent);


    if(camera2lookthrough == cameras.limelight_high){
        //RobotContainer.getInstance().subLimelightHigh.setPipeline(camPipeline);
        //RobotContainer.getInstance().subLimelightHigh.setCamMode(SubLimelight.CAM_MODE.VISION_PROCESSING);
        //Get the Limelight table within that instance that contains the data. There can
        //camera = RobotContainer.getInstance().subLimelightHigh;
    }
    else if (camera2lookthrough == cameras.limelight_low){
        //RobotContainer.getInstance().subLimelightLow.setPipeline(camPipeline);
        //RobotContainer.getInstance().subLimelightLow.setCamMode(SubLimelight.CAM_MODE.VISION_PROCESSING);
        //camera = RobotContainer.getInstance().subLimelightLow;
    }

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    System.err.println("execute");

    //If target detected count it as being seen
    

    if ((int) camera.getTV() > 0) {
      targetCounter = targetCounter + 1;
    }

    if (targetCounter > MINTARGETHITS)  and {

    }

    // We have not yet seen the target drive by gyro only
    if (targetCounter < MINTARGETHITS) {
      double headingDelta = calcTurnRate(RobotContainer.getInstance().subGyro.getNormaliziedNavxAngle());
      subDriveTrain.Drive(speedPercent + headingDelta, speedPercent - headingDelta);
      System.err.println(
        "Drive " + RobotContainer.getInstance().subDriveTrain.getEncoderPosLeft_Inches() + " of " + targetPosinch);
      System.err.println("power " + RobotContainer.getInstance().subDriveTrain.getpower());
    }
    else if () {

    }




    // If the camera has a target then drive towards the target.

    // else if the camera does not have a target then drive by gyro incase of the following
    //  1) We are too far away from the target to reconize it.
    //  2) we are now too close to the target and it has gone out of frame.


    if (Math.abs(subDriveTrain.getEncoder_Inches_LR()) >= Math.abs(targetPosinch - overShoot)) {
      bDone = true;
      end(false);
    }

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // RobotContainer.getInstance().subDriveTrain.Drive(0, 0);
    System.err.println("end");
    System.err.println(
        "Drive " + subDriveTrain.getEncoderPosLeft_Inches() + " of " + targetPosinch);
    subDriveTrain.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    System.err.println("isFinished");
    return bDone;
  }

  @Override
  public boolean runsWhenDisabled() {
    System.err.println("runsWhenDisabled");
    return false;
  }

  public void stop() {
    subDriveTrain.Drive(0, 0);
    System.err.println("stop");

  }

  protected double calcTurnRate(double currentHeading) {
    double turnRate = RobotMath.calcTurnRate(currentHeading, Heading, subDriveTrain.kp_DriveStraightGyro);
    return turnRate;
  }

}
