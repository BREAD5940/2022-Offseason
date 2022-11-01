package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class GutPrototype extends SubsystemBase{

    // Hardware
    CANSparkMax motor = new CANSparkMax(16, MotorType.kBrushless);

    // Methods
    public void spin(double percent) {
        motor.set(percent);
    } 

}
