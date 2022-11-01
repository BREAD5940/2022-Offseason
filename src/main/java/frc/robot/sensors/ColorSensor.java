package frc.robot.sensors;

import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.SerialPort;

public class ColorSensor {
    public int far_r = 0;
    public int far_b = 0;
    public int close_r = 0;
    public int close_b = 0;

    public String sensor = "";

    public static int colorRatio = 4; // Ratio for color decision

    private Alliance closeColor = Alliance.Invalid;
    private Alliance farColor = Alliance.Invalid;

    SerialPort serialMXP = new SerialPort(9600, SerialPort.Port.kMXP);

    public void periodic() {

        sensor = serialMXP.readString();

        if (sensor == "SENSOR1") {
            close_r = Integer.parseInt(serialMXP.readString());
            close_b = Integer.parseInt(serialMXP.readString());
        }

        if (sensor == "SENSOR2") {
            far_r = Integer.parseInt(serialMXP.readString());
            far_b = Integer.parseInt(serialMXP.readString());
        }

        // if there is colorRatio times red then blue it is probably red
        if (close_r / close_b > colorRatio) {
            closeColor = Alliance.Red;
        }

        if (close_b / close_r > colorRatio) {
            closeColor = Alliance.Red;
        }

        if (far_r / far_b > colorRatio) {
            farColor = Alliance.Red;
        }

        if (far_b / far_r > colorRatio) {
            farColor = Alliance.Blue;
        }

    }

    // Methods to call in other files to read the colors here
    public Alliance getColorClose() {
        return closeColor;
    }

    public Alliance getColorFar() {
        return farColor;
    }

}