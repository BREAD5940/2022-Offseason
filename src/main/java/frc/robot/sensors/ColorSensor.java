package frc.robot.sensors;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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

    public ColorSensor(){
        serialMXP.disableTermination();
    }

    public void periodic() {
        ArrayList<String> valuesp = new ArrayList<>();
        while(valuesp.size() < 3){
            String sensorp = serialMXP.readString();
            Arrays.stream(sensorp.split("\n")).forEach(valuesp::add);
        }

        sensor = valuesp.get(1);
        System.out.print(valuesp.size());
        System.out.printf("single 6 %s \n", sensor);
        String[] values = sensor.split(" ");
        int i = values.length - 1;
        close_r = Integer.parseInt(values[i-5].replaceAll("[^0-9]",""));
        close_g = Integer.parseInt(values[i-4].replaceAll("[^0-9]",""));
        close_b = Integer.parseInt(values[i-3].replaceAll("[^0-9]",""));
        far_r = Integer.parseInt(values[i-2].replaceAll("[^0-9]",""));
        far_g = Integer.parseInt(values[i-1].replaceAll("[^0-9]",""));
        far_b = Integer.parseInt(values[i].replaceAll("[^0-9]",""));
        SmartDashboard.putString("Sensor: ", sensor);
        System.out.printf("Color Sensor raw %d %d %d %d %d %d", close_r, close_g, close_b, far_r, far_g, far_b);

        //if there is colorRatio times red then blue it is probably red
        if (close_r / close_b > colorRatio) {
            closeColor = Alliance.Red;
            close_ball_present = true;
            SmartDashboard.putString("Gut Ball Close", "red");
        } else if (close_b / close_r > colorRatio) {
            closeColor = Alliance.Blue;
            close_ball_present = true;
            SmartDashboard.putString("Gut Ball Close", "blue");
        } else{
            closeColor = Alliance.Invalid;
            close_ball_present = false;
          SmartDashboard.putString("Gut Ball Close", "not present");
        }
        if (far_r / far_b > colorRatio) {
            farColor = Alliance.Red;
            far_ball_present = true;
            SmartDashboard.putString("Gut Ball Far", "red");
        } else if (far_b / far_r > colorRatio) {
            farColor = Alliance.Blue;
            far_ball_present = true;
            SmartDashboard.putString("Gut Ball Far", "blue");
        } else{
            farColor = Alliance.Invalid;
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