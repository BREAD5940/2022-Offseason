package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.RobotController;
import static frc.robot.Constants.Shooter.*;


public class Shooter {

  // declare motor and encoder
  private CANSparkMax shooterMotor = new CANSparkMax(SHOOTER_ID, MotorType.kBrushless);
  private RelativeEncoder encoder = shooterMotor.getEncoder();
  private SimpleMotorFeedforward ff = new SimpleMotorFeedforward(0, 1/473);

  // declare values
  private double setpoint = 0.0;
  private ShooterState systemState = ShooterState.IDLE;
  private boolean requestShoot = false;
  private double timeLastStateChange;

  public Shooter() {
    encoder.setVelocityConversionFactor(1); // gear ratio
  }

  public boolean isShooting() {
    return systemState != ShooterState.IDLE;
  }

  public boolean canShoot() {
    return (systemState == ShooterState.AT_SETPOINT && atSetPoint());
  }

  public double getVelocity() {
    return encoder.getVelocity();
  }

  public void setFlywheelRPM(double rpm) {
    if (rpm <= 50) {
      shooterMotor.set(0.0);
    } else {
      shooterMotor.setVoltage(ff.calculate(rpm));
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

  // get time S
  private double getTime() {
    return RobotController.getFPGATime() / 1.0E6;
  }

  public boolean atSetPoint() {
    return Math.abs(setpoint - getVelocity()) < 50;
  }

  public void periodic() {
    ShooterState lastSystemState = systemState;

    if (systemState == ShooterState.IDLE) {

      //output
      setFlywheelRPM(0.0);

      // state change
      if (requestShoot == true) {
        systemState = ShooterState.APPROACHING_SETPOINT;
      }
    } else if (systemState == ShooterState.APPROACHING_SETPOINT) {
      
      //output
      setFlywheelRPM(setpoint);

      // state change
      if (atSetPoint()) {
        systemState = ShooterState.STABALIZING;
      } else if (requestShoot == false) {
        systemState = ShooterState.IDLE;
      }
    } else if (systemState == ShooterState.STABALIZING) {

      // output
      setFlywheelRPM(setpoint);

      // state change
      if (getTime() - timeLastStateChange >= 0.25) {
        systemState = ShooterState.AT_SETPOINT;
      } else if (!atSetPoint()) {
        systemState = ShooterState.APPROACHING_SETPOINT;
      } else if (requestShoot == false) {
        systemState = ShooterState.IDLE;
      }
    } else if (systemState == ShooterState.AT_SETPOINT) {
      
      // output
      setFlywheelRPM(setpoint);
      
      // state change
      if (!atSetPoint()) {
        systemState = ShooterState.APPROACHING_SETPOINT;
      } else if (requestShoot == false) {
        systemState = ShooterState.IDLE;
      }
    }
    if (lastSystemState != systemState) {
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