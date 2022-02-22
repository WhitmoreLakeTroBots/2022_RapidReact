package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

import frc.robot.subsystems.SubDriveTrain;
import frc.robot.subsystems.SubGyro;
import frc.robot.subsystems.SubLimelight;
import frc.robot.Constants.limelightConstants.cameras;

public class CmdTurnByLime extends CommandBase {

    
    private boolean bDone = false;

    private double _requestedHeading = 0.0;
    private double _left_throttle = 0.0;
    private double _right_throttle = 0.0;
    private double tol = 5.0;

    private SubDriveTrain subDriveTrain = null;
    private SubGyro subGyro = null;
    private cameras camera2lookthrough = null;
    private int camPipeline = 0;
    private SubLimelight camera = null;

    public CmdTurnByLime(double heading_deg, double left_throttle, double right_throttle, cameras cam, int pipeline) {

        _requestedHeading = heading_deg;
        _left_throttle = left_throttle;
        _right_throttle = right_throttle;
        camPipeline = pipeline;
        camera2lookthrough = cam;

    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        bDone = false;
        subDriveTrain = RobotContainer.getInstance().subDriveTrain;

        subGyro = RobotContainer.getInstance().subGyro;
        subDriveTrain.resetBothEncoders();
        subDriveTrain.Drive(_left_throttle, _right_throttle);

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
        System.err.println("cmdTurnByLime");

        if (camera.hasTarget()) {
            // https://docs.limelightvision.io/en/latest/getting_started.html
            _requestedHeading = subGyro.gyroNormalize(subGyro.getNormaliziedNavxAngle() + camera.getTX());
        }

        if (subGyro.gyroInTol(subGyro.getNormaliziedNavxAngle(), _requestedHeading, tol)) {
            bDone = true;
            end(false);
        }

    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        subDriveTrain.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return bDone;
    }

    @Override
    public boolean runsWhenDisabled() {
        // This command can cause robot motion.
        // this always returns false;
        return false;
    }
}
