/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.Constants.Profiler_Constants_DriveTrain;

/**
 * Add your docs here.
 */
public class CommonLogic {

  public static double CapMotorPower(double MotorPower, double negCapValue, double posCapValue) {
    // logic to cap the motor power between a good range
    double retValue = MotorPower;

    if (MotorPower < negCapValue) {
      retValue = negCapValue;
    }

    if (MotorPower > posCapValue) {
      retValue = posCapValue;
    }

    return retValue;
  }

  public static final double joyDeadBand(double joy, double deadband) {

    double retValue = joy;
    if (Math.abs(retValue) < Math.abs(deadband)) {
      retValue = 0;
    }
    return Math.pow(retValue, 2) * Math.signum(joy);
  }

  public static final boolean isInRange(double curRevs, double desiredRevs, double Tol) {

    double loVal = desiredRevs - Tol;
    double hiVal = desiredRevs + Tol;
    boolean retValue = false;

    if (curRevs > loVal && curRevs < hiVal) {
      retValue = true;
    }
    return retValue;
  }

  public static double getTime() {
    return (System.nanoTime() / Math.pow(10, 9));
  }

  public static double calcTurnRate(double deltaHeading, double proportion) {

    double commandedTurnRate = deltaHeading * proportion;
    return commandedTurnRate; // IS ALWAYS POSITIVE!
  }

  public double deg2Rad(double degrees) {
    return Math.toRadians(degrees);
  }

  public static double calcArcLength(double degrees, double radius) {
    return (Math.toRadians(degrees) * radius);
  }

  public static double calcProfileAbortTime(double dist_inch,
      double vel_inch_sec, double accel_inch_sec_sec) {

    double time2_accel = (vel_inch_sec / accel_inch_sec_sec);
    double dist2_accel = (.5 * accel_inch_sec_sec * time2_accel * time2_accel);
    double retValue = 0;
    // Is this a triangle or trappazoid profile
    if ((dist2_accel * 2) > dist_inch) {
      // it is triangle and we never reach curise speed;
      // this happens with short move distances and high speeds with low accel
      retValue = (time2_accel * 2 * Profiler_Constants_DriveTrain.profileEndTimeScalar);
    } else {
      // it is trapazoid and we do cruise for a while
      double inchAtCruise = dist_inch - (2 * dist2_accel);
      double timeAtCruise = inchAtCruise / vel_inch_sec;
      retValue = ((2 * time2_accel) + timeAtCruise) * Profiler_Constants_DriveTrain.profileEndTimeScalar;
    }
    return retValue;
  }
}
