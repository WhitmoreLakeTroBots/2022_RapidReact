/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.limelightConstants;
import frc.robot.Constants.limelightConstants.cameras;

public class SubLimelight extends SubsystemBase {
    public static NetworkTableInstance inst = null;
    // public static NetworkTable llTable = null;
    public NetworkTableEntry ltx = null;
    public NetworkTableEntry lty = null;
    public NetworkTableEntry lta = null;
    public NetworkTableEntry ltv = null;
    public NetworkTableEntry lts = null;
    public NetworkTableEntry ltl = null;
    public NetworkTableEntry ltcm = null;
    public NetworkTableEntry ltlm = null;
    public NetworkTableEntry ltp = null;
    public NetworkTable llTable = null;

    public String limelight_high = "limelight-high";
    public String limelight_low = "limelight-low";

    // private NetworkTable llTable =
    // NetworkTableInstance.getDefault().getTable("limelight");
    public SubLimelight(limelightConstants.cameras cam) {
        inst = NetworkTableInstance.getDefault();

        if (cam == cameras.limelight_high) {
            llTable = inst.getTable("limelight-high");

        } else if (cam == cameras.limelight_low) {
            llTable = inst.getTable("limelight-low");
        }

        ltx = llTable.getEntry("tx");
        lty = llTable.getEntry("ty");
        lta = llTable.getEntry("ta");
        ltv = llTable.getEntry("tv");
        lts = llTable.getEntry("ts");
        ltl = llTable.getEntry("tl");
        ltcm = llTable.getEntry("camMode");
        ltlm = llTable.getEntry("ledMode");
        ltp = llTable.getEntry("pipeline");
        setCamMode(CAM_MODE.DRIVERSTATION_FEEDBACK);        
    }

    /*
     * public subLimelight() {
     * llTable.getEntry("camMode").setNumber(CAM_MODE.VISION_PROCESSING.val);
     * 
     * }
     */
    public double getTX() {
        return ltx.getDouble(0);
    }

    public double getTY() {
        return lty.getDouble(0);
    }

    // Whether the limelight has any valid targets (0 or 1)
    public boolean hasTarget() {
        NetworkTableEntry tv = llTable.getEntry("tv");
        return tv.getDouble(0) == 1;
    }

    public void setLEDMode(LED_MODE ledMode) {
        llTable.getEntry("ledMode").setNumber(ledMode.val);
    }

    public void setCamMode(CAM_MODE camMode) {
        llTable.getEntry("camMode").setNumber(camMode.val);
    }

    public void setPipeline(int pipeline) {
        llTable.getEntry("pipeline").setNumber(pipeline);
    }

    public enum LED_MODE {
        OFF(1), ON(0), BLINKING(2);

        public int val;

        LED_MODE(int i) {
            val = i;
        }
    }

    public enum CAM_MODE {
        VISION_PROCESSING(0), DRIVERSTATION_FEEDBACK(1);

        public int val;

        CAM_MODE(int i) {
            val = i;
        }
    }

}