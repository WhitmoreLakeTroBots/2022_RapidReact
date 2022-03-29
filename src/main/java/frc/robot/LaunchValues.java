package frc.robot;

public class LaunchValues {

    public static final double[][] LaunchCodes = {
            // limelight 2 can only go +-29.8 degrees in vertical direction
            // {cameraAngle, RPM},
            // https://docs.google.com/spreadsheets/d/1EMc1xOxHKY0zVEOjFElyHyc4wrLUNXh38YC5TEDD6Ho/edit#gid=0
            // Ball parked the distances with (12/1700) * (feet + 7.7) = RPM
            // {LL Ty , RPM},
            // { 35.00, 1500.0 }, // 0 feet from hub
            // { 35.00, 1233.0 }, // 1 feet from hub
            // { 35.00, 1374.0 }, // 2
            // { 35.00, 1516.0 }, // 3
            // { 35.00, 1658.0 }, // 4
            { 35.00, 1799.0 },  // 5
            { 29.00, 1941.0 },  // 6 Target aquired about 6 feet from hub
            { 27.12, 2083.0 },  // 7
            { 21.00, 2224.0 },  // 8
            { 15.00, 2366.0 },  // 9
            { 6.75, 3000.0 },  // 10
            { 3.4, 3050.0 },   // 11 //sweet spot shooting here
            { 5.00, 3100.0 },   // 12
            { 4.00, 2933.0 },   // 13
            { 3.00, 3074.0 },   // 14
            { 0.00, 3216.0 },   // 15
            { -3.00, 3358.0 },  // 16
            { -4.00, 3499.0 },  // 17 // near safe zone (203 inches)
            { -5.00, 3641.0 },  // 18
            { -6.54, 3783.0 },  // 19
            { -10.00, 3924.0 }, // 20 // far safe zone (245 inches)
            { -15.00, 4066.0 }, // 21
            { -35.00, 1800.0 } // to infinity and beyond
    };

    public static double getRPM(double cameraAngle) {

        int lcv = 0;
        for (lcv = 0; lcv < LaunchCodes.length - 1; lcv++) {
            if (cameraAngle >= LaunchCodes[lcv][0]) {
                break;
            }
        }
        System.out.print((lcv + 5) + "\t");
        return (LaunchCodes[lcv][1]);
    }

    public static int getRange(double cameraAngle) {
        // retruns the number of feet we are out from center of hub
        int lcv = 0;
        for (lcv = 0; lcv < LaunchCodes.length - 1; lcv++) {
            if (cameraAngle >= LaunchCodes[lcv][0]) {
                break;
            }
        }

        return (lcv + 5);
    }

    public static void testLaunchValues() {
        int lcv = 0;
        double rpm = 0;
        for (lcv = 40; lcv > -39; lcv--) {
            rpm = LaunchValues.getRPM(lcv);
            System.out.println(lcv + "\t:" + rpm);
        }
    }

    public static void testRange() {
        int lcv = 0;
        int range = 0;
        for (lcv = 40; lcv > -39; lcv--) {
            range = LaunchValues.getRange(lcv);
            System.out.println(lcv + "\t:" + range);
        }
    }

    public static void main(String[] arg) {

        System.out.println("Hello World!");
        testLaunchValues();
        testRange();
    }
}
