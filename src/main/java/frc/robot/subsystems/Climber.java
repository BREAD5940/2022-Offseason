package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import static frc.robot.Constants.Climber.*;

// Intake Subsystem
public class Climber {
    // State
    private enum ClimberStates {
        DEPLOYED, // Climber is deployed
        STOWED, // Climber is stowed and inactive
    }

    // State
    private ClimberStates climberState;

    // Motors
    private CANSparkMax climberMotor;

    // Configure Climber upon instantiation
    public Climber() {
        // Initial state
        climberState = ClimberStates.STOWED;

        // Initializing motor controllers
        climberMotor = new CANSparkMax(CLIMBER_ID, null);

        // Restore motor controller factory defaults
        climberMotor.restoreFactoryDefaults();
    }

    // Public method to request climber to deploy
    public void requestDeploy() {
        climberState = ClimberStates.DEPLOYED;
    }

    // Public method to request climber to stow
    public void requestStow() {
        climberState = ClimberStates.STOWED;
    }

    // Public method to handle state / output functions
    public void periodic() {
        if (climberState == ClimberStates.STOWED) {
            climberMotor.set(-0.5);
        } else if (climberState == ClimberStates.DEPLOYED) {
            climberMotor.set(0.5);
        }
    }
}