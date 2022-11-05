package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;

import static frc.robot.Constants.Intake.*;

// Intake Subsystem
public class Intake {
  // State
  private enum IntakeStates {
    HOMING, // Homing state
    STOWED_INACTIVE, // Intake is stowed and inactive
    DEPLOYED_ACTIVE_IN, // Intake is deployed and actively intaking cargo
    DEPLOYED_ACTIVE_OUT // Intake is deployed and is actively outtaking cargo
  }

  // State
  private IntakeStates intakeState;

  // Motors
  private CANSparkMax verticalRollerMotor;
  private CANSparkMax horizontalRollerMotor;
  private CANSparkMax deploymentMotor;

  // Encoders, Controllers, and others
  private SparkMaxPIDController deploymentPid;
  private RelativeEncoder deploymentEncoder;

  private double timeLastStateChange;

  // Configure Intake upon instantiation
  public Intake() {
    // Initial state
    intakeState = IntakeStates.HOMING;

    // Initializing motor controllers
    verticalRollerMotor = new CANSparkMax(INTAKE_VERTICAL_ROLLER_ID, MotorType.kBrushless);
    horizontalRollerMotor = new CANSparkMax(INTAKE_HORIZONTAL_ROLLER_ID, MotorType.kBrushless);
    deploymentMotor = new CANSparkMax(INTAKE_DEPLOYMENT_ID, MotorType.kBrushless);

    // Restore motor controller factory defaults
    verticalRollerMotor.restoreFactoryDefaults();
    horizontalRollerMotor.restoreFactoryDefaults();
    deploymentMotor.restoreFactoryDefaults();

    // Initialize pid controller, deployment encoder, and limit switch
    deploymentPid = deploymentMotor.getPIDController();
    deploymentEncoder = deploymentMotor.getEncoder();

    // Configure deployment encoder
    deploymentEncoder.setPositionConversionFactor(INTAKE_GEARING * 360);

    // Configure deployment motor
    deploymentPid.setFeedbackDevice(deploymentEncoder);
    deploymentPid.setP(1);
    deploymentPid.setI(0);
    deploymentPid.setD(0);
    deploymentPid.setOutputRange(-0.5, 0.5);
  }

  // Public method to request intake to return to home position
  public void requestHome() {
    intakeState = IntakeStates.HOMING;
  }

  // Public method to request intake to deploy and either spin inwards or outwards
  public void requestDeploy(boolean outtake) {
    if (outtake) {
      intakeState = IntakeStates.DEPLOYED_ACTIVE_OUT;
    } else {
      intakeState = IntakeStates.DEPLOYED_ACTIVE_IN;
    }
  }

  // Public method to request intake to stow
  public void requestStow() {
    intakeState = IntakeStates.STOWED_INACTIVE;
  }

  // Public method to home intake
  private void home() {
    // TODO: Figure out a velocity value to check for
    if (this.getVelocity() < 1 && timeLastStateChange + 0.5 < getTime()) {
      // Setting proper encoder value
      deploymentMotor.setVoltage(0.0);
      deploymentEncoder.setPosition(0.0);

      intakeState = IntakeStates.STOWED_INACTIVE;
    } else {
      deploymentMotor.setVoltage(-1.0); // TODO: Figure out a good voltage value
    }
  }

  // Private method to deploy intake to its deployment setpoint
  private void deploy() {
    deploymentPid.setReference(INTAKE_DEPLOYED_SETPOINT, CANSparkMax.ControlType.kPosition);
  }

  // Private method to stow intake to its stowing setpoint
  private void stow() {
    deploymentPid.setReference(INTAKE_STOWED_SETPOINT, CANSparkMax.ControlType.kPosition);
  }

  // Private method to spin rollers
  private void spinRollers(boolean outtake) {
    verticalRollerMotor.set(outtake ? -0.3 : 1.0);
    horizontalRollerMotor.set(outtake ? -0.3 : 1.0);
  }

  // Private method to stop rollers
  private void stopRollers() {
    verticalRollerMotor.set(0.0);
    horizontalRollerMotor.set(0.0);
  }

  // Private method that returns the velocity of the deployment encoder
  private double getVelocity() {
    return Math.abs(deploymentEncoder.getVelocity());
  }

  // get time S
  private double getTime() {
    return RobotController.getFPGATime() / 1.0E6;
  }

  // Public method to handle state / output functions
  public void periodic() {
    IntakeStates lastSystemState = intakeState;

    if (intakeState == IntakeStates.HOMING) {
      home();
    } else if (intakeState == IntakeStates.STOWED_INACTIVE) {
      stow();
      stopRollers();
    } else if (intakeState == IntakeStates.DEPLOYED_ACTIVE_IN) {
      deploy();
      spinRollers(false);
    } else if (intakeState == IntakeStates.DEPLOYED_ACTIVE_OUT) {
      deploy();
      spinRollers(true);
    }

    if (lastSystemState != intakeState) {
      timeLastStateChange = getTime();
    }
  }
}