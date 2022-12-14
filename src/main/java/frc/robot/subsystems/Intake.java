package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
  private IntakeState systemState;
  private boolean requestIntake = false;
  private boolean requestOuttake = false;
  private boolean requestStow = false;
  private boolean requestHome = false;
  private boolean didStateChange = false;

  // Motors
  private CANSparkMax verticalRollerMotor;
  private CANSparkMax horizontalRollerMotor;
  private CANSparkMax deploymentMotor;

  // Encoders, Controllers, and others
  private SparkMaxPIDController deploymentPid;
  public RelativeEncoder deploymentEncoder;

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
    deploymentMotor.setIdleMode(IdleMode.kBrake);

    // Initialize pid controller, deployment encoder, and limit switch
    deploymentPid = deploymentMotor.getPIDController();
    deploymentEncoder = deploymentMotor.getEncoder();

    // Configure deployment encoder
    //deploymentEncoder.setPositionConversionFactor(INTAKE_GEARING);

    // Configure deployment motor
    deploymentPid.setFeedbackDevice(deploymentEncoder);
    deploymentPid.setP(1);
    deploymentPid.setI(0);
    deploymentPid.setD(0);
    deploymentPid.setOutputRange(-0.25, 0.25);
  }

  // Public method to request intake to return to home position
  public void requestHome() {
    requestHome = true;


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

  //public void testMoter() {
    //deploymentMotor.setVoltage(-3);
  //}


  // Private method to deploy intake to its deployment setpoint
  private void deploy() {
    /*if (deploymentEncoder.getPosition() < 5) {
      deploymentMotor.setVoltage(3);
    } else {
      deploymentMotor.setVoltage(0);
    }*/
    deploymentPid.setReference(INTAKE_DEPLOYED_SETPOINT, CANSparkMax.ControlType.kPosition);
  }

  // Private method to stow intake to its stowing setpoint
  private void stow() {
    /*if (deploymentEncoder.getPosition() > 1) {
      deploymentMotor.setVoltage(-3);
    } else {
      deploymentMotor.setVoltage(0);
    }*/

    deploymentPid.setReference(INTAKE_STOWED_SETPOINT, CANSparkMax.ControlType.kPosition);
  }

  // Private method to spin rollers
  private void spinRollers(boolean outtake) {
    verticalRollerMotor.set(outtake ? -0.3 : 1);
    horizontalRollerMotor.set(outtake ? -0.3 : 1);
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

    if (!didStateChange) {
      didStateChange = true;
      timeLastStateChange = getTime();
    }

    IntakeState nextSystemState = systemState;
    SmartDashboard.putString("Intake State", systemState.toString());


    if (systemState == IntakeState.HOMING) {

      // Outputs
      deploymentMotor.setVoltage(-3);

      // Figure out a velocity value to check for
      if (this.getVelocity() < 10 && timeLastStateChange + 0.5 < getTime()) {

        // Setting proper encoder value
        deploymentEncoder.setPosition(0.0);
        deploymentMotor.set(0.0);
        
        // State Transition
        systemState = IntakeState.STOWED_INACTIVE;
      }

    } else if (systemState == IntakeState.STOWED_INACTIVE) {
      // Outputs
      stow();
      stopRollers();

      // State Transitions
      if (requestHome) {
        requestHome = false;
        systemState = IntakeState.HOMING;
      } else if (requestIntake) {
        requestIntake = false;
        systemState = IntakeState.DEPLOYED_ACTIVE_IN;
      } else if (requestOuttake) {
        requestOuttake = false;
        systemState = IntakeState.DEPLOYED_ACTIVE_OUT;
      }

    } else if (systemState == IntakeState.DEPLOYED_ACTIVE_IN) {


      // Outputs
      deploy();
      spinRollers(false);

      // State Transitions
      if (requestHome) {
        requestHome = false;
        systemState = IntakeState.HOMING;
      } else if (requestStow) {
        requestStow = false;
        systemState = IntakeState.STOWED_INACTIVE;
      } else if (requestOuttake) {
        systemState = IntakeState.DEPLOYED_ACTIVE_OUT;
        requestOuttake = false;
      }

    } else if (systemState == IntakeState.DEPLOYED_ACTIVE_OUT) {


      // Outputs
      deploy();
      spinRollers(true);

      // State Transitions
      if (requestHome) {
        requestHome = false;
        systemState = IntakeState.HOMING;
      } else if (requestIntake) {
        requestIntake = false;
        systemState = IntakeState.DEPLOYED_ACTIVE_IN;
      } else if (requestStow) {
        systemState = IntakeState.STOWED_INACTIVE;
        requestStow = false;
      }
    }

    if (nextSystemState != systemState) {
      timeLastStateChange = getTime();
    }
  }
}