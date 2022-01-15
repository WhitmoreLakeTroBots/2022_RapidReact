package frc.robot.motion_profile;

import frc.robot.Constants.Profiler_Constants_DriveTrain;

public class MotionProfileTester {
	static Logger log = new Logger(Profiler_Constants_DriveTrain.profileTestLogName);
	static MotionProfileTester mpt = new MotionProfileTester();

	public static void main(String[] args) {
		mpt.test_fpIsEqual();
		mpt.test_getProfileAccellTimes();
		mpt.test_getProfileDeltaX();
		mpt.test_getProfileCurrVelocity();
		mpt.test_getProfileAccellerationSign();
		//log.write();
	}

	void test_getProfileAccellTimes() {
		double retValue = 0;
		double dist = 200;
		double initVel = 1;
		double cruiseVel = 18;
		double accel = 3;
		MotionProfiler mp = new MotionProfiler(dist, initVel, cruiseVel, accel);
		if (fpIsEqual(mp.getProfileAccellTimes(), (17 / 3), 100)) {
			retValue = 1;
			log.makeEntry("getProfileAccellTimes1: FAIL");
			System.out.println("getProfileAccellTimes1: FAIL");
		}
		initVel = 0;
		cruiseVel = 25;
		accel = 5;
		mp = new MotionProfiler(dist, initVel, cruiseVel, accel);
		if (!fpIsEqual(mp.getProfileAccellTimes(), 5.0, 100)) {
			retValue = 1;
			log.makeEntry("getProfileAccellTimes2: FAIL");
			System.out.println("getProfileAccellTimes Result: " + mp.getProfileAccellTimes());
			System.out.println("getProfileAccellTimes2: FAIL");
		}

		if (retValue == 0) {
			log.makeEntry("getProfileAccellTimes: PASS");
			System.out.println("getProfileAccellTimes: PASS");
		}
	}

	void test_getProfileDeltaX() {
		// Tests math for getProfileDeltaX
		double dist = 200;
		double initVel = 1;
		double cruiseVel = 18;
		double accel = 3;
		double retValue = 0;
		double retValue2 = 0;
		MotionProfiler mp = new MotionProfiler(dist, initVel, cruiseVel, accel);

		if (!fpIsEqual(mp.getProfileDeltaX(), 53.833, 100)) {
			retValue = 1;
			log.makeEntry("getProfileDeltaX1: FAIL");
			System.out.println("getProfileDeltaX1: FAIL");
		}
		dist = 30;
		initVel = 1;
		cruiseVel = 18;
		accel = 300;
		mp = null;
		mp = new MotionProfiler(dist, initVel, cruiseVel, accel);
		if (!fpIsEqual(mp.getProfileDeltaX(), 0.538, 100)) {
			retValue2 = 1;
			log.makeEntry("getProfileDeltaX2: FAIL");
			System.out.println("getProfileDeltaX2: FAIL");
		}
		if ((retValue == 0) && (retValue2 == 0)) {
			log.makeEntry("getProfileDeltaX: PASS");
			System.out.println("getProfileDeltaX: PASS");
		}
	}

	void test_getProfileCurrVelocity() {
		// checks if getCurrentVelocity() is working
		double dist = 200;
		double initVel = 1;
		double cruiseVel = 18;
		double accel = 3;
		double retValue = 0;

		MotionProfiler mp = new MotionProfiler(dist, initVel, cruiseVel, accel);
		if (mp.getProfileCurrVelocity(0.5) != 2.5) {
			retValue = 1;
			log.makeEntry("getProfileCurrVelocity1: FAIL");
			System.out.println("getProfileCurrVelocity1: FAIL");
		}

		if (mp.getProfileCurrVelocity(0.0) != 1.0) {
			retValue = 1;
			log.makeEntry("getProfileCurrVelocity2: FAIL");
			System.out.println("getProfileCurrVelocity2: FAIL");
		}

		if (mp.getProfileCurrVelocity(1.0) != 4.0) {
			retValue = 1;
			log.makeEntry("getProfileCurrVelocity3: FAIL");
			System.out.println("getProfileCurrVelocity3: FAIL");
		}

		if (mp.getProfileCurrVelocity(5.0) != 16.0) {
			retValue = 1;
			log.makeEntry("getProfileCurrVelocity4: FAIL");
			System.out.println("getProfileCurrVelocity4: FAIL");
		}
		if (mp.getProfileCurrVelocity(1.5) != 5.5) {
			retValue = 1;
			log.makeEntry("getProfileCurrVelocity5: FAIL");
			System.out.println("getProfileCurrVelocity5: FAIL");
		}

		dist = 30;
		initVel = 15;
		cruiseVel = 15;
		accel = 3;
		retValue = 0;
		mp = null;
		mp = new MotionProfiler(dist, initVel, cruiseVel, accel);

		if (mp.getProfileCurrVelocity(1.5) != 15.0) {
			retValue = 1;
			log.makeEntry("getProfileCurrVelocity6: FAIL");
			System.out.println("getProfileCurrVelocity6: FAIL");
		}

		if (mp.getProfileCurrVelocity(-5.0) != 0.0) {
			retValue = 1;
			log.makeEntry("getProfileCurrVelocity7: FAIL");
			System.out.println("getProfileCurrVelocity7: FAIL");
		}
		if (mp.getProfileCurrVelocity(10.0) != -0.0) {
			retValue = 1;
			log.makeEntry("getProfileCurrVelocity8: FAIL");
			System.out.println("getProfileCurrVelocity8: FAIL");
		}

		if (retValue == 0) {
			log.makeEntry("getProfileCurrVelocity: PASS");
			System.out.println("getProfileCurrVelocity: PASS");
		}
	}

	void test_getProfileAccellerationSign() {
		// tests the method getProfileAccellerationSign
		double dist = 30;
		double initVel = 1;
		double cruiseVel = 18;
		double accel = 3;
		double retValue = 0;
		MotionProfiler mp = new MotionProfiler(dist, initVel, cruiseVel, accel);

		if (mp.getProfileAccellerationSign() != 1) {
			log.makeEntry("getProfileAccellerationSignPos: FAIL");
			System.out.println("getProfileAccellerationSignPos: FAIL");
			retValue = 1;
		}

		mp = null;
		initVel = 18;
		cruiseVel = 18;
		mp = new MotionProfiler(dist, initVel, cruiseVel, accel);

		if (mp.getProfileAccellerationSign() != 0) {
			log.makeEntry("getProfileAccellerationSignZero: FAIL");
			System.out.println("getProfileAccellerationSignZero: FAIL");
			retValue = 1;
		}

		mp = null;
		initVel = 20;
		cruiseVel = 10;
		mp = new MotionProfiler(dist, initVel, cruiseVel, accel);

		if (mp.getProfileAccellerationSign() != -1) {
			log.makeEntry("getProfileAccellerationSignNeg: FAIL");
			System.out.println("getProfileAccellerationSignNeg: FAIL");
			retValue = 1;
		}

		if (retValue == 0) {
			log.makeEntry("getProfileAccellerationSign: PASS");
			System.out.println("getProfileAccellerationSign: PASS");
		}
	}

	boolean fpIsEqual(double fp1, double fp2, double tol) {
		int ip1 = (int) (fp1 * tol);
		int ip2 = (int) (fp2 * tol);
		return (ip1 == ip2);
	}

	void test_fpIsEqual() {
		boolean retValue = true;

		if (!fpIsEqual(3.14, 3.15, 1)) {
			log.makeEntry("fpIsEqual1: FAIL");
			System.out.println("fpIsEqual1: FAIL");
			retValue = false;
		}
		if (!fpIsEqual(3.14, 3.15, 10)) {
			log.makeEntry("fpIsEqual1: FAIL");
			System.out.println("fpIsEqual2: FAIL");
			retValue = false;
		}
		if (fpIsEqual(3.14, 3.15, 100)) {
			log.makeEntry("fpIsEqual3: FAIL");
			System.out.println("fpIsEqual3: FAIL");
			retValue = false;
		}
		if (fpIsEqual(3.141, 3.142, 1000)) {
			log.makeEntry("fpIsEqual4: FAIL");
			System.out.println("fpIsEqual4: FAIL");
			retValue = false;
		}
		if (retValue) {
			log.makeEntry("fpIsEqual: PASS");
			System.out.println("fpIsEqual: PASS");
		}
	}
}