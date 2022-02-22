package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.RobotMath;
import frc.robot.subsystems.SubDriveTrain;
import frc.robot.subsystems.SubGyro;
import frc.robot.subsystems.SubLimelight;
import frc.robot.Constants.limelightConstants.cameras;

public class CmdAutoDriveStraghtByLime extends CommandBase {

  private double targetPosinch = 36;
  private double speedPercent = .15;
  private double desiredHeadingDeg = 0;
  private boolean bDone = false;
  private final double overShoot = 4;
  private SubDriveTrain subDriveTrain = null;
  private SubGyro subGyro = null;
  private cameras camera2lookthrough = null;
  private int camPipeline = 0;
  private SubLimelight camera = null;

  public CmdAutoDriveStraghtByLime(double distanceIn, double headingDeg, double powerPer, cameras cam, int pipeline) {

    targetPosinch = distanceIn;
    desiredHeadingDeg = headingDeg;
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
    subGyro = RobotContainer.getInstance().subGyro;
    subDriveTrain.resetBothEncoders();
    subDriveTrain.Drive(speedPercent, speedPercent);

    if (camera2lookthrough == cameras.limelight_high) {
      camera = RobotContainer.getInstance().subLimelightHigh;
    } else if (camera2lookthrough == cameras.limelight_low) {
      camera = RobotContainer.getInstance().subLimelightLow;
    }

    camera.setPipeline(camPipeline);
    camera.setCamMode(SubLimelight.CAM_MODE.VISION_PROCESSING);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    System.err.println("execute");

    if (camera.hasTarget()) {
      //https://docs.limelightvision.io/en/latest/getting_started.html
      desiredHeadingDeg = subGyro.gyroNormalize(subGyro.getNormaliziedNavxAngle() + camera.getTX());
    }

    double headingDelta = calcTurnRate(subGyro.getNormaliziedNavxAngle());
    subDriveTrain.Drive(speedPercent + headingDelta, speedPercent - headingDelta);
    // Stop if we are done driving the required distance.
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
    stop();
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
    double turnRate = RobotMath.calcTurnRate(currentHeading, desiredHeadingDeg, subDriveTrain.kp_DriveStraightGyro);
    return turnRate;
  }

}
