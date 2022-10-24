package frc.robot.subsystems.swerve;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.RobotController;

// Analog encoder class 
public class AnalogEncoder {

    private final AnalogInput encoder; 
    private final double offset;

    public AnalogEncoder(int channel, double offset) {
        encoder = new AnalogInput(channel);
        this.offset = offset;
    }
    
    public double get() {
        double angle = (((encoder.getVoltage() / RobotController.getVoltage5V()) * 2.0 * Math.PI) - offset) % (2.0 * Math.PI);
        if (angle < -Math.PI) angle = (2 * Math.PI) + angle;
        if (angle > Math.PI) angle = -(2 * Math.PI) + angle;
        return -angle;
    }

    public double getRaw() {
        return encoder.getVoltage();
    }
     
}