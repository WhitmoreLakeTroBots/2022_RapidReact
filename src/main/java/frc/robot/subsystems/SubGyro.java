package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.SPI;

import com.kauailabs.navx.frc.AHRS;

public class SubGyro extends SubsystemBase {

  private AHRS navx;
  public static double navxOffset = 0.0;

  public SubGyro() {
    navx = new AHRS(SPI.Port.kMXP);
    resetNavx();
  }


  @Override
  public void periodic() {

  }

  public void invertNavx () {
    if (navxOffset == 0.0) {
      navxOffset = 180.0;
    }
    else {
      navxOffset = 0;
    }
  }

  public void invertNavx (boolean value) {
    if (value) {
      navxOffset = 180.0;
    }
    else {
      navxOffset = 0.0;
    }
  }

  public double getNavxAngleRaw() {
    return navx.getAngle();
  }

  public double getNormaliziedNavxAngle() {
    return gyroNormalize(getNavxAngleRaw() + navxOffset);
  }

  public void resetNavx() {
    navx.reset();
    navx.zeroYaw();
  }

  public double gyroNormalize(double heading) {
    // takes the full turns out of heading
    // gives us values from 0 to 180 for the right side of the robot
    // and values from 0 to -179 degrees for the left side of the robot
    double degrees = heading % 360;

    if (degrees > 180) {
      degrees = degrees - 360;
    }
    if (degrees < -179) {
      degrees = degrees + 360;
    }
    return degrees;
  }

  public boolean gyroInTol(double currHeading, double desiredHeading, double tol) {

    double upperTol = gyroNormalize(desiredHeading + tol);
    double lowerTol = gyroNormalize(desiredHeading - tol);
    double normalCurr = gyroNormalize(currHeading);
    double signumUpperTol = Math.signum(upperTol);
    double signumLowerTol = Math.signum(lowerTol);
    boolean retValue = false;
    // works for all positive numbers direction values
    if (signumUpperTol > 0 && signumLowerTol > 0) {
      if ((normalCurr >= lowerTol) && (normalCurr <= upperTol)) {
        retValue = true;
      }
    }
    // works for negative values
    else if (signumUpperTol < 0 && signumLowerTol < 0) {
      if ((normalCurr >= lowerTol) && (normalCurr <= upperTol)) {
        retValue = true;
      }
    }
    // mixed values -tol to + tol This happens at 180 degrees
    else if ((signumUpperTol < 0) && (signumLowerTol > 0)) {
      if ((Math.abs(normalCurr) >= Math.abs(lowerTol)) && (Math.abs(normalCurr) >= Math.abs(upperTol))) {
        retValue = true;
      }
    }
    // mixed values -tol to + tol This happens at 0 degrees
    else if ((signumUpperTol > 0) && (signumLowerTol < 0)) {
      if ((Math.abs(normalCurr) <= Math.abs(lowerTol)) && (Math.abs(normalCurr) <= Math.abs(upperTol))) {
        retValue = true;
      }
    }
    return (retValue);
  }

  public double deltaHeading(double currHeading, double targetHeading) {

    // float signumCurrHeading = Math.signum (currHeading);
    // float signumTargetHeading = Math.signum (targetHeading);
    double returnValue = 0;

    // Positive value
    if (currHeading >= 0 && targetHeading >= 0) {
      returnValue = targetHeading - currHeading;
    }
    // one of each
    else if (currHeading >= 0 && targetHeading <= 0) {
      returnValue = (targetHeading + currHeading);
    }
    // one of each again
    else if (currHeading <= 0 && targetHeading >= 0) {
      returnValue = -1 * (targetHeading + currHeading);
    }
    // both negative
    else if (currHeading <= 0 && targetHeading <= 0) {
      returnValue = targetHeading - currHeading;
    }

    return returnValue;
  }


  public double deltaHeading(double targetHeading) {

    return deltaHeading (getNormaliziedNavxAngle(), targetHeading);
  }
}