package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class GutPrototype {

    // Hardware
    CANSparkMax motor = new CANSparkMax(16, MotorType.kBrushless);

    // Methods
    public void spin(double percent) {
        motor.set(percent);
    } 

}
