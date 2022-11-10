package frc.robot.sensors;

import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.SerialPort;

public class ColorSensor {
    public int far_r = 0;
    public int far_b = 0;
    public int far_g = 0;
    public int close_r = 0;
    public int close_b = 0;
    public int close_g = 0;
    public Boolean far_ball_present = false;
    public Boolean close_ball_present = false;

    public String sensor = "";

    public static int colorRatio = 2; // Ratio for color decision

    private Alliance closeColor = Alliance.Invalid;
    private Alliance farColor = Alliance.Invalid;

    SerialPort serialMXP = new SerialPort(9600, SerialPort.Port.kMXP);

    public void periodic() {

        sensor = serialMXP.readString();
        String[] values = sensor.split(" ");
        close_r = Integer.parseInt(values[0]);
        close_g = Integer.parseInt(values[1]);
        close_b = Integer.parseInt(values[2]);

        far_r = Integer.parseInt(values[3]);
        far_g = Integer.parseInt(values[4]);
        far_b = Integer.parseInt(values[5]);
        

        // if there is colorRatio times red then blue it is probably red
        if (close_r / close_b > colorRatio) {
            closeColor = Alliance.Red;
            close_ball_present = true;
            SmartDashboard.putString("Gut Ball Close", "red");
        }
        else if (close_b / close_r > colorRatio) {
            closeColor = Alliance.Blue;
            close_ball_present = true;
            SmartDashboard.putString("Gut Ball Close", "blue");
        }
        else{
            close_ball_present = false;
            SmartDashboard.putString("Gut Ball Close", "not present");
        }

        if (far_r / far_b > colorRatio) {
            farColor = Alliance.Red;
            far_ball_present = true;
            SmartDashboard.putString("Gut Ball Far", "red");
        }
        else if (far_b / far_r > colorRatio) {
            farColor = Alliance.Blue;
            far_ball_present = true;
            SmartDashboard.putString("Gut Ball Far", "blue");
        }
        else{
            far_ball_present = false;
            SmartDashboard.putString("Gut Ball Far", "not present");

        }

    }

    // Methods to call in other files to read the colors here
    public Alliance getColorClose() {
        return closeColor;
    }

    public Alliance getColorFar() {
        return farColor;
    }

    public Boolean isBallClose(){
        return close_ball_present;
    }

    public Boolean isBallFar(){
        return far_ball_present;
    }

}