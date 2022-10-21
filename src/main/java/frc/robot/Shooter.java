package frc.robot;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.XboxController;
public class Shooter {
    
// declare jude-minded motors and controller
private CANSparkMax flyWheel1 = new CANSparkMax(1, MotorType.kBrushless);
private CANSparkMax flyWheel2 = new CANSparkMax(2, MotorType.kBrushless);
private double Speed1 = 1;
private double Speed2 = 1;

//https://www.youtube.com/watch?v=NFAaeGO0WFo

XboxController controller = new XboxController(0);

//THIS DOESN'T WORK AT ALL

public void shoot() {

if (controller.getXButton()) {
    flyWheel1.set(Speed1);
    flyWheel2.set(Speed2);
  }
  else{
    flyWheel1.set(0);
    flyWheel2.set(0);
  }

}
}
