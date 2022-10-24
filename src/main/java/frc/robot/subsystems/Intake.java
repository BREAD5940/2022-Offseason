package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import static frc.robot.Constants.Intake.*;

// Intake Subsystem
public class Intake {
    // State
    private enum IntakeState {
      STOWED_INACTIVE, // Intake is stowed and inactive
      DEPLOYED_ACTIVE_IN, // Intake is deployed and actively intaking cargo
      DEPLOYED_ACTIVE_OUT // Intake is deployed and is actively outtaking cargo
    }

    private IntakeState intakeState;
    private CANSparkMax verticalRollerMotor;
    private CANSparkMax horizontalRollerMotor;
    private CANSparkMax deploymentMotor;
    private SparkMaxPIDController deploymentPid;
    private RelativeEncoder deploymentEncoder;
  
    // Configuration upon instantiation
    public Intake() {
      // Initial state
      intakeState = IntakeState.STOWED_INACTIVE;

      // Initializing motor controllers (configure CAN Ids later)
      verticalRollerMotor = new CANSparkMax(0, MotorType.kBrushless);
      horizontalRollerMotor = new CANSparkMax(0, MotorType.kBrushless);
      deploymentMotor = new CANSparkMax(0, MotorType.kBrushless);

      // Restore motor controller factory defaults
      verticalRollerMotor.restoreFactoryDefaults();
      horizontalRollerMotor.restoreFactoryDefaults();
      deploymentMotor.restoreFactoryDefaults();

      // Initialize pid controller + deployment encoder objects
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

    // Requests intake to deploy and either spit or suck
    public void requestDeploy(boolean outtake) {
      if (outtake) {
        intakeState = IntakeState.DEPLOYED_ACTIVE_IN;
      } else {
        intakeState = IntakeState.DEPLOYED_ACTIVE_OUT;
      }
    }

    // Requests intake to stow
    public void requestStow() {
      intakeState = IntakeState.STOWED_INACTIVE;
    }

    // Public method to find if the intake is at the given setpoint
    private boolean intakeAtSetpoint(IntakeState setpoint) {
      double setpointType = setpoint == IntakeState.DEPLOYED_ACTIVE_IN || setpoint == IntakeState.DEPLOYED_ACTIVE_OUT ? INTAKE_DEPLOYED_SETPOINT : INTAKE_STOWED_SETPOINT;
      boolean atSetpoint = Math.abs(deploymentEncoder.getPosition() - setpointType) < INTAKE_SETPOINT_EPSILON;

      return atSetpoint;
    }

    // Private method to deploy intake to its deployment setpoint
    private void deploy() {
      if (this.intakeAtSetpoint(IntakeState.DEPLOYED_ACTIVE_IN) || this.intakeAtSetpoint(IntakeState.DEPLOYED_ACTIVE_OUT)) {
        return;
      } else {
        deploymentPid.setReference(INTAKE_DEPLOYED_SETPOINT, CANSparkMax.ControlType.kPosition);
      }
    }
  
    // Private method to stow intake to its stowing setpoint 
    private void stow() {
      if (this.intakeAtSetpoint(IntakeState.STOWED_INACTIVE)) {
        return;
      } else {
        deploymentPid.setReference(INTAKE_STOWED_SETPOINT, CANSparkMax.ControlType.kPosition);
      }
    }

    // Private method to spin rollers
    private void spinRollers(boolean outtake) {
      verticalRollerMotor.set(outtake ? -0.3 : 1.0);
      horizontalRollerMotor.set(outtake ? -0.3 : 1.0);
    }

    // Public method to handle state / output functions
    public void periodic() {
      if (intakeState == IntakeState.STOWED_INACTIVE) {
        stow();
      } else if (intakeState == IntakeState.DEPLOYED_ACTIVE_IN) {
        deploy();
        spinRollers(false);
      } else if (intakeState == IntakeState.DEPLOYED_ACTIVE_OUT) {
        deploy();
        spinRollers(true);
      }
    }
}