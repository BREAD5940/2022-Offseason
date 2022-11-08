package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
  private IntakeState systemState;
  private boolean requestIntake = false;
  private boolean requestOuttake = false;
  private boolean requestStow = false;
  private boolean requestHome = false;

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
    systemState = IntakeState.HOMING;

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
    requestHome = true;
  }

  // Public method to request intake to deploy and either spin inwards or outwards
  public void requestDeploy(boolean outtake) {
    if (outtake) {
      requestOuttake = true;
    } else {
      requestIntake = true;
    }
  }

  public boolean isIntakeDeployed(){
    return IntakeState.DEPLOYED_ACTIVE_IN == systemState;
  }

  // Public method to request intake to stow
  public void requestStow() {
    requestStow = true;
  }

  // Private method to home intake
  private void home(IntakeState nextSystemState) {
    deploymentMotor.set(-0.05);

    // Figure out a velocity value to check for
    if (this.getVelocity() < 1 && timeLastStateChange + 0.5 < getTime()) {

      // Setting proper encoder value
      deploymentEncoder.setPosition(0.0);
      deploymentMotor.set(0.05);

      // State Transition
      nextSystemState = IntakeState.STOWED_INACTIVE;
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
    verticalRollerMotor.set(outtake ? -0.3 : 0.5);
    horizontalRollerMotor.set(outtake ? -0.3 : 0.5);
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

    SmartDashboard.putNumber("intake position", deploymentEncoder.getPosition());
    SmartDashboard.putNumber("intake velocity", deploymentEncoder.getVelocity());
    IntakeState nextSystemState = systemState;

    if (nextSystemState == IntakeState.HOMING) {
      SmartDashboard.putString("Intake State", "HOMING");

      // Outputs and State Transitions
      home(nextSystemState);

    } else if (nextSystemState == IntakeState.STOWED_INACTIVE) {
      // Outputs
      stow();
      stopRollers();

      // State Transitions
      if (requestHome) {
        requestHome = false;
        nextSystemState = IntakeState.HOMING;
      } else if (requestIntake) {
        requestIntake = false;
        nextSystemState = IntakeState.DEPLOYED_ACTIVE_IN;
      } else if (requestOuttake) {
        nextSystemState = IntakeState.DEPLOYED_ACTIVE_OUT;
        requestOuttake = false;
      }

    } else if (nextSystemState == IntakeState.DEPLOYED_ACTIVE_IN) {
      SmartDashboard.putString("Intake State", "DEPLOYED_ACTIVE_IN");


      // Outputs
      deploy();
      spinRollers(false);

      // State Transitions
      if (requestHome) {
        nextSystemState = IntakeState.HOMING;
      } else if (requestStow) {
        nextSystemState = IntakeState.STOWED_INACTIVE;
      } else if (requestOuttake) {
        nextSystemState = IntakeState.DEPLOYED_ACTIVE_OUT;
      }

    } else if (nextSystemState == IntakeState.DEPLOYED_ACTIVE_OUT) {
      SmartDashboard.putString("Intake State", "DEPLOYED_ACTIVE_OUT");


      // Outputs
      deploy();
      spinRollers(true);

      // State Transitions
      if (requestHome) {
        nextSystemState = IntakeState.HOMING;
      } else if (requestStow) {
        nextSystemState = IntakeState.STOWED_INACTIVE;
      } else if (requestIntake) {
        nextSystemState = IntakeState.DEPLOYED_ACTIVE_IN;
      }
    }

    if (nextSystemState != systemState) {
      timeLastStateChange = getTime();
      systemState = nextSystemState;
    }
  }
}