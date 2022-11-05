package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.RobotController;

public class Shooter {

  // declare motor and encoder
  private CANSparkMax motor1 = new CANSparkMax(1, MotorType.kBrushless);
  private RelativeEncoder encoder = motor1.getEncoder();
  private PIDController PIDController = new PIDController(1, 0, 0); // pid needs P tuning

  // declare values
  private double setpoint = 0.0;
  private ShooterState systemState = ShooterState.IDLE;
  private boolean requestShoot = false;
  private double timeLastStateChange;

  // get gut class
  public Gut gut;

  public void gutInput(Gut gut) {
  }

  public boolean canShoot() {
    return (systemState == ShooterState.AT_SETPOINT && atSetPoint());
  }

  public void setVoltage() {
    motor1.setVoltage(2000);
  }

  public double getVelocity() {
    return encoder.getVelocity();
  }

  public void setFlywheelRPM(double rpm) {
    double e = rpm - getVelocity();
    if (rpm <= 0.1) {
      motor1.set(0.0);
    } else {
      motor1.setVoltage(expectedVoltageNeeded(rpm) + PIDController.calculate(e));
    }
  }
  
  private double expectedVoltageNeeded(double rpm) {
    // 473rpm per V
    return rpm / 473;
  }

  public void requestIdle() {
    setpoint = 0.0;
    requestShoot = false;
  }

  public void requestShoot() {
    gut.requestShoot();
    setpoint = 1.0; // shoot rpm
    requestShoot = true;
  }

  // get time MS
  private double getTime() {
    return RobotController.getFPGATime() / 1.0E3;
  }

  public boolean atSetPoint() {
    return Math.abs(setpoint - getVelocity()) < 50;
  }

  public void periodic() {
    ShooterState nextSystemState = systemState;
    if (requestShoot == false) {
      systemState = ShooterState.IDLE;
    } else {
      if (systemState == ShooterState.IDLE) {
        systemState = ShooterState.APPROACHING_SETPOINT;
      }
    }
    if (systemState == ShooterState.IDLE) {
      setFlywheelRPM(0.0); // idle rpm
    } else if (!atSetPoint()) { // this is so if the rpm is close then not close it still works
      systemState = ShooterState.APPROACHING_SETPOINT;
      setFlywheelRPM(setpoint);
    } else if (systemState == ShooterState.APPROACHING_SETPOINT) {
      systemState = ShooterState.STABALIZING;
      setFlywheelRPM(setpoint);
    } else if (systemState == ShooterState.STABALIZING) {
      setFlywheelRPM(setpoint);
      if (getTime() - timeLastStateChange >= 250) {
        systemState = ShooterState.AT_SETPOINT;
      }
    } else if (systemState == ShooterState.AT_SETPOINT) {
      setFlywheelRPM(setpoint);
    }

    if (nextSystemState != systemState) {
      timeLastStateChange = getTime();
    }
  }

  public enum ShooterState {
    IDLE,
    APPROACHING_SETPOINT,
    STABALIZING, // 0.25 seconds are waited stablizing before we shoot
    AT_SETPOINT
  }
}