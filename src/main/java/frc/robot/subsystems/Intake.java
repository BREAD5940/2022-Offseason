package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Intake {
    private CANSparkMax verticalRollerMotor;
    private CANSparkMax horizontalRollerMotor;
    private CANSparkMax deploymentMotor;
    private double suckSpeed;
    private double spitSpeed;
    private double inactiveSpeed;
  
    public Intake() {
      // Initializing motor controllers
      verticalRollerMotor = new CANSparkMax(0, MotorType.kBrushless);
      horizontalRollerMotor = new CANSparkMax(0, MotorType.kBrushless);
      deploymentMotor = new CANSparkMax(0, MotorType.kBrushless);
  
      // Motor speeds
      suckSpeed = 1.0;
      spitSpeed = -0.3;
      inactiveSpeed = 0.0;
    }

    //TODO: for the deploy and intake methods, use position control to accurately set motors based on encoder position

    //deploy intake
    public void deploy(boolean spit, double deploymentMotorSpeed) {
      if (spit) {
        verticalRollerMotor.set(spitSpeed);
        horizontalRollerMotor.set(spitSpeed);
      } else {
        verticalRollerMotor.set(suckSpeed);
        horizontalRollerMotor.set(suckSpeed);
      }
  
      deploymentMotor.set(deploymentMotorSpeed);
    }
  
    // stow intake
    public void stow() {
      verticalRollerMotor.set(inactiveSpeed);
      horizontalRollerMotor.set(inactiveSpeed);
      deploymentMotor.set(inactiveSpeed);
    }
}