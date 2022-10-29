package frc.robot.sensors;

import edu.wpi.first.wpilibj.SerialPort;

public class ColorSensor {

    public int far_r = 0;
    public int far_b = 0;
    public int close_r = 0;
    public int close_b = 0;

    public String sensor = "";

    public static int colorRatio = 4; // Ratio for color decision

    private CloseColorSensorStates closeColor = CloseColorSensorStates.NONE;
    private FarColorSensorStates farColor = FarColorSensorStates.NONE;

    SerialPort serialMXP = new SerialPort(9600, SerialPort.Port.kMXP);

    public enum FarColorSensorStates {
        NONE,
        RED,
        BLUE
    }

    public enum CloseColorSensorStates {
        NONE,
        RED,
        BLUE
    }

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
            closeColor = CloseColorSensorStates.RED;
        }

        if (close_b / close_r > colorRatio) {
            closeColor = CloseColorSensorStates.BLUE;
        }

        if (far_r / far_b > colorRatio) {
            farColor = FarColorSensorStates.RED;
        }

        if (far_b / far_r > colorRatio) {
            farColor = FarColorSensorStates.BLUE;
        }

    }

    // Methods to call in other files to read the colors here
    public CloseColorSensorStates getColorClose() {
        return closeColor;
    }

    public FarColorSensorStates getColorFar() {
        return farColor;
    }

}