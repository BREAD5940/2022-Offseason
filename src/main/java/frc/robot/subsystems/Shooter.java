package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.RobotController;

public class Shooter {

  // declare motor and encoder
  private CANSparkMax shooterMotor = new CANSparkMax(1, MotorType.kBrushless);
  private RelativeEncoder encoder = shooterMotor.getEncoder();
  private PIDController PIDController = new PIDController(1, 0, 0); // pid needs P tuning
  private SimpleMotorFeedforward ff = new SimpleMotorFeedforward(0, 1/473);

  // declare values
  private double setpoint = 0.0;
  private ShooterState systemState = ShooterState.IDLE;
  private boolean requestShoot = false;
  private double timeLastStateChange;

  public Shooter() {
    encoder.setVelocityConversionFactor(1); // gear ratio
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

  // get time MS
  private double getTimeMS() {
    return RobotController.getFPGATime() / 1.0E3;
  }

  public boolean atSetPoint() {
    return Math.abs(setpoint - getVelocity()) < 50;
  }

  public void periodic() {
    ShooterState lastSystemState = systemState;
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
      if (getTimeMS
  () - timeLastStateChange >= 250) {
        systemState = ShooterState.AT_SETPOINT;
      }
    } else if (systemState == ShooterState.AT_SETPOINT) {
      setFlywheelRPM(setpoint);
    }

    if (lastSystemState != systemState) {
      timeLastStateChange = getTimeMS
  ();
    }
  }

  public enum ShooterState {
    IDLE,
    APPROACHING_SETPOINT,
    STABALIZING, // 0.25 seconds are waited stablizing before we shoot
    AT_SETPOINT
  }
}