package frc.robot.subsystems;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.XboxController;
public class Shooter {
      
  // declare motor and encoder
  private CANSparkMax motor1 = new CANSparkMax(1, MotorType.kBrushless);
  private RelativeEncoder encoder = motor1.getEncoder();

  private double setpoint = 0.0;
  private ShooterState systemState = ShooterState.IDLE;
  private boolean requestShoot = false;
  private int timeLastStateChange;
  public Shooter() {

  }

  public void setVoltage() {
    motor1.setVoltage(2000);
  }

  public double getVelocity() {
    return encoder.getVelocity();
  }

  public void setFlywheelRPM(double rpm) {
    if (rpm == 0.0) {
      motor1.set(0.0);
    } else {
      motor1.set(rpm);;
    }
  }

  public void requestIdle() {
    setpoint = 0.0;
    requestShoot = false;
  }

  public void requestShoot(double rpm) {
    setpoint = rpm;
    requestShoot = true;
  }

  public boolean atSetPoint() {
    return Math.abs(setpoint - getVelocity()) < 50;
  }

  public void periodic() {
    ShooterState nextSystemState = systemState;

    if (systemState == ShooterState.IDLE) {
      setFlywheelRPM(0.0); // idle rpm
    } else if (! atSetPoint()) {
      systemState = ShooterState.APPROACHING_SETPOINT;
      setFlywheelRPM(setpoint);
    } else if (systemState == ShooterState.APPROACHING_SETPOINT) {
      systemState = ShooterState.STABALIZING;
      setFlywheelRPM(setpoint);
    } else if (systemState == ShooterState.STABALIZING) {
      setFlywheelRPM(setpoint);
      if (timenow() - timeLastStateChange >= 250) {
        systemState = ShooterState.AT_SETPOINT;
      }
    } else if (systemState == ShooterState.AT_SETPOINT) {
      setFlywheelRPM(setpoint);
    }

    if (nextSystemState != systemState) {
      timeLastStateChange = timenow();
    }
  }

  public enum ShooterState {
    IDLE, 
    APPROACHING_SETPOINT, 
    STABALIZING,
    AT_SETPOINT
  }
}