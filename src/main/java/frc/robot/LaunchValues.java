package frc.robot;

import com.fasterxml.jackson.databind.util.ArrayBuilders.DoubleBuilder;

public class LaunchValues {

    public static final double[][] LaunchCodes = {
            // limelight 2 can only go +-29.8 degrees in vertical direction
            // {cameraAngle, RPM},
            // https://docs.google.com/spreadsheets/d/1EMc1xOxHKY0zVEOjFElyHyc4wrLUNXh38YC5TEDD6Ho/edit#gid=0
            // Ball parked the distances with RPM = (145.5)(Dist Feet) + 1448   This is an average of the 3 points
            // taken from the lab on March 28, 2022.

            // Measure from center of the hub to the front bumper of the robot with bumpers ON.
            // This is done to make it easy to measure while at competions.   Yes I know the camera
            // is really about 18 inches farther away but the important part is consistency in measurements.

            // {LL Ty , RPM},
            // { 35.00, 1500.0 }, // 0 feet from hub
            // { 35.00, 1233.0 }, // 1 feet from hub
            // { 35.00, 1374.0 }, // 2
            // { 35.00, 1516.0 }, // 3
            // { 35.00, 1658.0 }, // 4
            { 35.00, 1550.0 },  // 5  Low Shot
            { 22.59, 2321.0 },  // 6 Target aquired about 6 feet from hub
            { 18.17, 2467.0 },  // 7
            { 14.39, 2612.0 },  // 8
            { 11.13, 2758.0 },  // 9
            { 8.32, 2903.0 },  // 10
            { 5.87, 3049.0 },   // 11 //sweet spot shooting here
            { 3.74, 3194.0 },   // 12
            { 1.86, 3340.0 },   // 13
            { 0.20, 3485.0 },   // 14
            { -1.27, 3631.0 },   // 15
            { -2.59, 3776.0 },  // 16
            { -3.77, 3922.0 },  // 17 // near safe zone (203 inches)
            { -4.84, 4067.0 },  // 18
            { -5.13, 4213.0 },  // 19
            { -6.69, 4358.0 }, // 20 // far safe zone (245 inches)
            { -7.50, 4504.0 }, // 21
            { -35.00, 1550.0 }  // Low Shot
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

   public static double calcRange (double cameraYDegrees){

     // Target height is 8'8"  or 104 inches
        // robot camera hieght is 24 inches
        // practice Lab measurement shows robot camera angled up 24.9 degrees
        // we took 3 measurements from known distance and used this formula
        // a1 = Atan(h2 - h2/d) - a2

        // data can be found here 
        // https://docs.google.com/spreadsheets/d/1EMc1xOxHKY0zVEOjFElyHyc4wrLUNXh38YC5TEDD6Ho/edit#gid=0


        // Step 1. convert camera angle to distance in inches
        // d = (h2 -h1) / tan (a1 + a2)

        double h1 = 2.0; // 24 inches;
        double h2 = 8.6666; // 104 inches;
        double a1 = 24.0128;

        double d = (h2-h1) / (Math.tan(Math.toRadians(a1 + cameraYDegrees)));
        return (d);
   }

    public static double calcRPM(double cameraYDegrees){

       
        // Step 2 calculate the RPM using y = mx+ b linear form from spread sheet
        // 
        double m = 135; // change this value and b value to change just one end
        double b = 1725;  // change this value to impact both long and short shots
        double x = calcRange(cameraYDegrees);
        double rpm = (m * x) + b; 
        return (CommonLogic.CapMotorPower(rpm, 1500, 4600));

    }

}
