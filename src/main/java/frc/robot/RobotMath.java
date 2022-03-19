package frc.robot;


public class RobotMath {
    //returns time in whole seconds
	public static double getTime() {     
		return (System.nanoTime() / Math.pow(10, 9));
	}
	public static double headingDelta(double currentHeading, double targetHeading) {
		double headingDelta = 0;
		double invertedHeadingDelta = 0;
		
		 //Positive value
        if (currentHeading >= 0 && targetHeading >= 0) {
            headingDelta = targetHeading - currentHeading;
        }
        // one of each
        else if (currentHeading >= 0 && targetHeading <= 0) {
            //headingDelta =  (targetHeading + currentHeading);
        	headingDelta = Math.abs(targetHeading) + Math.abs(currentHeading);
        	invertedHeadingDelta = Math.abs(360 + targetHeading) - Math.abs(currentHeading);
        	headingDelta = Math.min(Math.abs(headingDelta), Math.abs(invertedHeadingDelta));
        	if(invertedHeadingDelta != headingDelta) {
        		headingDelta = headingDelta * -1;
        	}
        }
        //one of each again
        else if (currentHeading <= 0 && targetHeading >= 0) {
            //headingDelta = -1 * (targetHeading + currentHeading);
        	headingDelta = Math.abs(targetHeading) + Math.abs(currentHeading);
        	invertedHeadingDelta = Math.abs(360 - targetHeading) - Math.abs(currentHeading);
        	headingDelta = Math.min(Math.abs(headingDelta), Math.abs(invertedHeadingDelta));
        	if(invertedHeadingDelta == headingDelta) {
        		headingDelta = headingDelta * -1;
        	}
        }
        // both negative
        else if (currentHeading <= 0 && targetHeading <= 0) {
            headingDelta = targetHeading - currentHeading;
        }
        return headingDelta;
	}
	
	public static double calcTurnRate( double currentHeading, double targetHeading, double proportion) {

        double headingDelta = headingDelta(currentHeading, targetHeading);
        
       
		double commandedTurnRate = headingDelta * proportion;
		return commandedTurnRate; //IS ALWAYS POSITIVE!
	}
	
	public static double calcAnglarDelta(double currentHeading, double desiredHeading) {
		double anglarDelta = Math.abs(currentHeading - desiredHeading);
		if(anglarDelta > 180) {
			anglarDelta = anglarDelta - 180;
		}
		return anglarDelta;
	}


	// converts everything to a min throttle keeping the original sign
    public static double calcMinThrottle(double throttle, double minThrottle) {
        return Math.signum(throttle) * minThrottle;
    }

    // calculates a KP based on (throttle - min throttle) / heading delta
    public static double calcKP(double throttle, double minThrottle) {
        // Math Time.... Based on 1 second for 360 degree turns.

        // Assume robot has 14ft/sec = (14*12)in/sec = 168 inch/sec
        // Assume robot has track width of 24 inches
        // circumference of the circle = Math.pi * 24 = 75.4 inches
        // 75.4 / 168 means that motor power of .448 should give us a 1 second 360
        // degree turn.

        double full360turndist = Constants.robotPysicalProperties.robotTrackWidth * Math.PI;
        double full360throttle = full360turndist / Constants.robotPysicalProperties.theoreticalMaxSpeedInches;

        double retValue = Math.signum(throttle) * Math.abs(full360throttle / 360);

        // System.out.println("retValue = " + retValue);
        return retValue;
    }

    // calculates the motor power and scales it based on the heading delta
    public static double calcMotorPower(double targetThrottle, double minThrottle, double KP, double headingDelta) {
        double sigNum = Math.signum(targetThrottle);

        double retValue = sigNum * (Math.abs(minThrottle) + (Math.abs(KP) * Math.abs(headingDelta)));

        if (sigNum < 0) {
            retValue = CommonLogic.CapMotorPower(retValue, targetThrottle, 0.0);
        }

        else if (sigNum > 0) {
            retValue = CommonLogic.CapMotorPower(retValue, 0.0, targetThrottle);
        } else {
            retValue = 0.0;
        }
        return retValue;
    }

}
