// RobotBuilder Version: 4.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be
 * declared globally (i.e. public static). Do not put anything functional in
 * this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public class Constants {

    public static final class CAN_ID_Constants {

        // Drive Train Can IDs
        public static final int kCanID_DriveTrain_left_1 = 16;
        public static final int kCanID_DriveTrain_left_2 = 1;
        public static final int kCanID_DriveTrain_right_1 = 14;
        public static final int kCanID_DriveTrain_right_2 = 15;

        // Launcher Can IDs
        public static final int kCanID_Launcher_1 = 2;
        // public static final int kCanID_launcher_2 = 6;

        public static final int kCanID_Roller = 11;
        public static final int kCanID_Retractor = 10;

        public static final int kCanID_Track = 5;

        public static final int kCanID_Feeder = 4;

         public static final int kCanID_Climber_1 = 12;
         public static final int kCanID_Climber_2 = 13;

         public static final int kCanID_Climb_Transversal = 3;
   

    }

    public static final class JoyStick_Constants {
        public static final double DriveDeadband = .1;
    }

    public static final class Profiler_Constants_DriveTrain {

        // Profile Settings
        public static final String profileTestLogName = "logs\\motionProfileTestResults";
        public static final String profileLogName = "//media//sda1//motionProfile";
        public static final double profileAdditionLoopNumber = 50;
        public static final String profileLogFileExtension = ".txt";
        public static final double profileDriveAccelration = 75; // inches/sec/sec
        public static final double profileTurnAcceleration = 50; // inches/sec/sec
        public static final double profileAnglarAccelration = 10;
        public static final double profileInitVelocity = 0.0;
        public static final double profileDefaultTurnVelocity = 50; // inches/sec
        public static final double profileMovementThreshold = 0.75;
        public static final double profileEndTimeScalar = 1.3;
        public static final double profileEndTol = .25;

        public static final double Kp = .0005;
        public static final double Ki = 0.0;
        public static final double Kd = 0.0;

    }

    public static final class ControllerConstants {

        public enum RobotMode {
            Start,
            End,
            Intake,
            Carry,
            Launching,
            Climb;
        }

    }
    public static final class limelightConstants {
        public enum cameras{
            limelight_high,
            limelight_low;

        }
        // camera pipeline constants
        // High cam constants
        public static final int UpperHubShootingPipe = 0;
        public static final int LowerBlueCargoPipe = 0;
        public static final int LowerRedCargoPipe = 1;
    }

    public static final class robotPysicalProperties {
        public static final double theoreticalMaxSpeedInches = 168.0;
        public static final double robotTrackWidth = 24.0; 
        //public static final double minTurnSpeed = 0.04;
        public static final double minTurnSpeed = 0.130;
    }
}
