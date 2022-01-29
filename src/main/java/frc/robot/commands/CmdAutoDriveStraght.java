package frc.robot.commands;

import java.lang.module.ModuleDescriptor.Requires;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.RobotMath;
import frc.robot.subsystems.SubDriveTrain;
public class CmdAutoDriveStraght extends CommandBase {

    private double targetPosinch = 36;
    private double speed = .15;
    private double Heading = 0;
    private boolean bDone = false;
    private final double overShoot = 4;

    public CmdAutoDriveStraght(double distanceIn, double headingInDeg, double powerPer) {
    
  targetPosinch = distanceIn;
  Heading = headingInDeg;
  speed = powerPer;

        //  addRequirements(RobotContainer.getInstance().subDriveTrain);
      // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
          // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
  
          // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
          // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
  
          // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
      }
          
  
      
  
      



      // Called when the command is initially scheduled.
      @Override
      public void initialize() {
        System.err.println("initialize");
        RobotContainer.getInstance().subDriveTrain.resetBothEncoders();
        RobotContainer.getInstance().subDriveTrain.Drive(speed, speed);

      }
  
      // Called every time the scheduler runs while the command is scheduled.
      @Override
      public void execute() {
         System.err.println("execute");
        //RobotContainer.getInstance().subDriveTrain.Drive(speed, speed);

        double headingDelta = calcTurnRate(RobotContainer.getInstance().subGyro.getNormaliziedNavxAngle());

RobotContainer.getInstance().subDriveTrain.Drive(speed + headingDelta, speed - headingDelta);


          System.err.println("Drive " + RobotContainer.getInstance().subDriveTrain.getEncoderPosLeft_Inches() + " of " + targetPosinch );
          System.err.println("power " + RobotContainer.getInstance().subDriveTrain.getpower());
if(RobotContainer.getInstance().subDriveTrain.getEncoder_Inches_LR() >= targetPosinch - overShoot){

  bDone = true;
stop();
end(false);

}

      }
  
      // Called once the command ends or is interrupted.
      @Override
      public void end(boolean interrupted) {
        //RobotContainer.getInstance().subDriveTrain.Drive(0, 0);
        System.err.println("end");
        System.err.println("Drive " + RobotContainer.getInstance().subDriveTrain.getEncoderPosLeft_Inches() + " of " + targetPosinch );
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
        
          // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DISABLED
          return false;
  
          // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DISABLED
      }

      public void stop(){
        RobotContainer.getInstance().subDriveTrain.Drive(0, 0);
        System.err.println("stop");

      }

      protected double calcTurnRate(double currentHeading) {
        double turnRate = RobotMath.calcTurnRate(currentHeading, Heading, RobotContainer.getInstance().subDriveTrain.kp_DriveStraightGyro);
        //if(currentHeading > Heading) {
        //	turnRate = turnRate * -1;
        //}
        return turnRate;
      }
 
}
