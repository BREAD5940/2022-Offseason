package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax;
import static frc.robot.Constants.Climber.*;

// Intake Subsystem
public class Climber {

    // Makes states
    private enum ClimberStates {
        DEPLOYED, // Climber is deployed
        STOWED, // Climber is stowed and inactive
    }
    private ClimberStates climberState;
    

    // Motor/Encoder
    private CANSparkMax climberMotor;
    private RelativeEncoder climberEncoder;

    // Stowned pos
    private double stowedEncoderPos;

    // Configure Climber upon instantiation
    public Climber() {
        // Initial state
        climberState = ClimberStates.STOWED;

        // Initializing motor controller
        climberMotor = new CANSparkMax(CLIMBER_ID, null);

         // Restore motor controller factory defaults
         climberMotor.restoreFactoryDefaults();

        // Initializing encoder
        climberEncoder = climberMotor.getEncoder();

        // get curent pos which sould when climber is stowed
        stowedEncoderPos = climberEncoder.getPosition();
    }

    // Public method to request climber to deploy
    public void requestDeploy() {
        climberState = ClimberStates.DEPLOYED;
    }

    // Public method to request climber to stow
    public void requestStow() {
        climberState = ClimberStates.STOWED;
    }

    // get climber pos
    public double getClimberPos() {
        return((climberEncoder.getPosition() - stowedEncoderPos) * 2.35619);
    }

    // make sure we dont over/under extend
    public boolean isClimberStowed() {
        return(getClimberPos() <= 0);
    }

    public boolean isClimberDeployed() {
        return(getClimberPos() >= 23);
    }

    // Periodic method
    public void periodic() {
        // Handle states and climber limits
        if (climberState == ClimberStates.STOWED && !isClimberStowed()) {
            climberMotor.set(-0.5);
        } else if (climberState == ClimberStates.DEPLOYED && !isClimberDeployed()) {
            climberMotor.set(0.5);
        } else {
            climberMotor.set(0.0);
        }
    }
}