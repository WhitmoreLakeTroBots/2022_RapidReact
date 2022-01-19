package frc.robot.commands;

//import org.usfirst.frc3668.TroBot.PID;
//import org.usfirst.frc3668.TroBot.Robot;
//import org.usfirst.frc3668.TroBot.RobotMath;
import frc.robot.Constants.Profiler_Constants_DriveTrain;

import frc.robot.motion_profile.MotionProfiler;
import frc.robot.subsystems.SubDriveTrain;
import frc.robot.subsystems.SubGyro;


import frc.robot.PID;
import frc.robot.RobotContainer;
import frc.robot.RobotMath;

//import com.fasterxml.jackson.databind.deser.AbstractDeserializer;

import edu.wpi.first.wpilibj2.command.CommandBase;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoDriveProfileGyro extends CommandBase{
	private final SubDriveTrain subDriveTrain;
	private final SubGyro _subGyro;

	private double _distance;
	private double _cruiseSpeed;
	private boolean _isFinished = false;
	private double _startTime;
	private double _requestedHeading = 0;
	private double _distanceSignum;
	private double _absDistance;
	private double _abortTime;
	private double _endTime;
	private MotionProfiler mp;
	private PID pid = new PID(Profiler_Constants_DriveTrain.Kp, Profiler_Constants_DriveTrain.Ki, Profiler_Constants_DriveTrain.Kd);
	
	public AutoDriveProfileGyro(double dist_inches, double vel_inches_sec, double heading_deg, double accel_inches_sec_sec) {

		_distance = dist_inches;
		_absDistance = Math.abs(dist_inches);
		_distanceSignum = Math.signum(dist_inches);
		_cruiseSpeed = vel_inches_sec;
		_requestedHeading = heading_deg;

		_subGyro = RobotContainer.getInstance().subGyro;
		//addRequirements(subGyro);
			
		subDriveTrain = RobotContainer.getInstance().subDriveTrain;;
		addRequirements(subDriveTrain);

		}
	
	protected void ProfileMockConstructor(double Speed, double distance){
		_distance = distance;
		_absDistance = Math.abs(distance);
		_distanceSignum = Math.signum(distance);
		_cruiseSpeed = Speed;		
	}
	
	// Called just before this Command runs the first time
	public void initialize() {
		System.err.println("Initializing");
		mp = new MotionProfiler(_absDistance, Profiler_Constants_DriveTrain.profileInitVelocity, _cruiseSpeed, Profiler_Constants_DriveTrain.profileDriveAccelration);
		subDriveTrain.resetBothEncoders();
		_abortTime = _absDistance / _cruiseSpeed;
		_endTime = mp._stopTime * Profiler_Constants_DriveTrain.profileEndTimeScalar;
		System.err.println(String.format(
				"Projected Accelration Time: %1$.3f \tProjected Cruise Time: %2$.3f \t Projected Deccelration Time: %3$.3f \t Projected Length of Drive: %4$.3f \t Given Distance: %5$.3f \t Abort: %6$.3f",
				mp._accelTime, mp._cruiseTime, mp._deccelTime, mp._stopTime, _distance, _abortTime));
		_startTime = RobotMath.getTime();
		_isFinished = false;
	}

	// Called repeatedly when this Command is scheduled to run
	public void execute() {
		System.err.println("Auto Drive Profile Running");
		double encoderVal = subDriveTrain.getEncoder_Inches_LR();
		double deltaTime = RobotMath.getTime() - _startTime;
		double profileDist = mp.getTotalDistanceTraveled(deltaTime);
		double currentHeading = _subGyro.getNormaliziedNavxAngle();
		double turnValue = calcTurnRate(currentHeading);
		double profileVelocity = mp.getProfileCurrVelocity(deltaTime);
		double throttlePos = (profileVelocity / subDriveTrain.kMaxInchesPerSecond);
		double pidVal = pid.calcPID(profileDist, encoderVal);
		double finalThrottle = throttlePos + pidVal;
		
		String msg = String.format(
				"CurrVel: %1$.3f \t throttle: %2$.3f \t Time: %3$.3f \t ProfileX: %4$.3f \t Encoder: %5$.3f \t PID Value: %10$.3f \t P: %14$.3f \t I: %13$.3f \t D: %11$.3f \t Final Throttle: %12$.3f \t Gyro: %15$.3f",
				profileVelocity, throttlePos, deltaTime, mp.getTotalDistanceTraveled(deltaTime),
				encoderVal, subDriveTrain.getEncoderPosLeft_Inches(),
				subDriveTrain.getEncoderPosRight_Inches(), currentHeading, turnValue, pidVal, pid.getDError(), finalThrottle, pid.getIError(), pid.getPError(), currentHeading);
		//FULL LOG MESSAGE: CurrVel: %1$.3f \t throttle: %2$.3f \t deltaTime: %3$.3f \t Disantce Travelled: %4$.3f \t AvgEncoder: %5$.3f \t Left Encoder: %6$.3f \t Right Encoder: %7$.3f \t Gyro Raw Heading: %8$.3f \t Turn Value: %9$.3f \t PID Value: %10$.3f \t P Value: %11$.3f \t Final Throttle: %12$.3f
		System.err.println(msg);
		//log.makeEntry(msg);
		//SmartDashboard.putNumber("Drive Left Encoder:", Robot.subDriveTrain.getLeftEncoderDist());
		//SmartDashboard.putNumber("Drive Right Encoder: ", Robot.subDriveTrain.getRightEncoderDist());

		subDriveTrain.Drive((finalThrottle * _distanceSignum), turnValue);

//		if (deltaTime > _abortTime && Robot.subDriveTrain.getEncoderAvgDistInch() == 0) {
//			System.out.println("Pasted Abort Time, Dead Encoders");
//			_isFinished = true;
//			Robot.subDriveTrain._isSafeToMove = false;
//		}
		if ( encoderVal < _absDistance + Profiler_Constants_DriveTrain.profileMovementThreshold && 
		     encoderVal > _absDistance - Profiler_Constants_DriveTrain.profileMovementThreshold) {
			System.err.println("At Distance");
			_isFinished = true;
		}
		
		if(deltaTime > _endTime) {
			_isFinished = true;
		}
	}

	protected double calcTurnRate(double currentHeading) {
		double turnRate = RobotMath.calcTurnRate(currentHeading, _requestedHeading, subDriveTrain.kp_DriveStraightGyro);
		//if(currentHeading > _requestedHeading) {
		//	turnRate = turnRate * -1;
		//}
		return turnRate;
	}

	// Make this return true when this Command no longer needs to run execute()
	public boolean isFinished() {
		return _isFinished;
	}

	// Called once after isFinished returns true
	protected void end() {
		subDriveTrain.Drive(0, 0);
		subDriveTrain.resetBothEncoders();

		

		System.out.println("AutoDriveProfileGyro is Finished Left Encoder: " + subDriveTrain.getEncoderPosRight_Inches() + " Right Encoder: " + subDriveTrain.getEncoderPosRight_Inches());
		System.err.println(String.format(
				"Projected Accelration Time: %1$.3f \tProjected Cruise Time: %2$.3f \t Projected Deccelration Time: %3$.3f \t Projected Length of Drive: %4$.3f \t Given Distance: %5$.3f",
				mp._accelTime, mp._cruiseTime, mp._deccelTime, mp._stopTime, _distance));

		// mp = null;
		// log.write();
		// log = null;
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}