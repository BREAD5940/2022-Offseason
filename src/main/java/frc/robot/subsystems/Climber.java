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
        NONE // temp we have not added limits yet so wont no anything so we dont break climber
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
        climberState = ClimberStates.NONE; // set to STOWED when limits are added

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
        return((climberEncoder.getPosition() - stowedEncoderPos) * 1); // add 360 rotation to hight change ratio instead of 1
    }

    // make sure we dont over/under extend
    public boolean isClimberStowed() {
        return(getClimberPos() <= 0);
    }

    public boolean isClimberDeployed() {
        return(getClimberPos() >= 0); // climber max extetion
    }

    // Periodic method
    public void periodic() {
        // Handle states and climber limits
        if (climberState == ClimberStates.STOWED && !isClimberStowed()) {
                climberMotor.set(-0.5);
        } else if (climberState == ClimberStates.DEPLOYED && !isClimberDeployed()) {
                climberMotor.set(0.5);
        }
        // temp code to reset climberState because limits are not added
        climberState = ClimberStates.NONE;
    }
}