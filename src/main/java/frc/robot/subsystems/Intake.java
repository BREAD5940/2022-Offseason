package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;

import static frc.robot.Constants.Intake.*;

// Intake Subsystem
public class Intake {
  // State
  private enum IntakeState {
    HOMING, // Homing state
    STOWED_INACTIVE, // Intake is stowed and inactive
    DEPLOYED_ACTIVE_IN, // Intake is deployed and actively intaking cargo
    DEPLOYED_ACTIVE_OUT // Intake is deployed and is actively outtaking cargo
  }

  // State
  private IntakeState intakeState;

  // Motors
  private CANSparkMax verticalRollerMotor;
  private CANSparkMax horizontalRollerMotor;
  private CANSparkMax deploymentMotor;

  // Encoders, Controllers, and DIO Inputs
  private SparkMaxPIDController deploymentPid;
  private RelativeEncoder deploymentEncoder;
  private DigitalInput intakeLimit;

  // Configuration upon instantiation
  public Intake() {
    // Initial state
    intakeState = IntakeState.HOMING;

    // Initializing motor controllers (configure CAN Ids later)
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
    intakeLimit = new DigitalInput(0); // configure port later

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
    intakeState = IntakeState.HOMING;
  }

  // Public method to request intake to deploy and either spin inwards or outwards
  public void requestDeploy(boolean outtake) {
    if (outtake) {
      intakeState = IntakeState.DEPLOYED_ACTIVE_OUT;
    } else {
      intakeState = IntakeState.DEPLOYED_ACTIVE_IN;
    }
  }

  // Public method to request intake to stow
  public void requestStow() {
    intakeState = IntakeState.STOWED_INACTIVE;
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

  // Public method to handle state / output functions
  public void periodic() {
    if (intakeState == IntakeState.HOMING) {
      // If limit switch is not triggered
      if (!intakeLimit.get()) {
        deploymentMotor.set(-0.5);
      } else {
        // Limit switch is triggered and intake is at homing pos
        deploymentMotor.set(0.0);
        deploymentEncoder.setPosition(0.0);

        intakeState = IntakeState.STOWED_INACTIVE;
      }
    } else if (intakeState == IntakeState.STOWED_INACTIVE) {
      stow();
      stopRollers();
    } else if (intakeState == IntakeState.DEPLOYED_ACTIVE_IN) {
      deploy();
      spinRollers(false);
    } else if (intakeState == IntakeState.DEPLOYED_ACTIVE_OUT) {
      deploy();
      spinRollers(true);
    }
  }
}