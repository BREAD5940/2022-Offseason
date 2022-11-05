package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj.RobotController;

import com.revrobotics.CANSparkMax;
import static frc.robot.Constants.Climber.*;

// Intake Subsystem
public class Climber {

    // Makes states
    private enum ClimberStates {
        DEPLOYED, // Climber is deployed
        STOWED, // Climber is stowed and inactive
        HOMING, // Finds the stowed pos
        MANUAL 
    }
    private ClimberStates climberState;
    
    private boolean requestDeploy = false;
    private boolean requestStowed = false;
    private boolean requestHoming = false;

    // Motor/Encoder
    private CANSparkMax climberMotor;
    private RelativeEncoder climberEncoder;

    // lastSystemState
    private double lastSystemState;

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
        climberEncoder.setPosition(0);
    }


    public void jogUp() {
        climberMotor.set(1);
    }

    public void jogDown() {
        climberMotor.set(-1);
    }
    //old code
    /*
    // Public method to request climber to deploy
    public void requestDeploy() {
        requestDeploy = true;
    }

    // Public method to request climber to stow
    public void requestStow() {
        requestStowed = true;
    }

    // Public method to request climber to start homing
    public void requestHoming() {
        requestHoming = true;
    }

    // get climber pos
    public double getClimberPos() {
        return((climberEncoder.getPosition()) * 2.35619);
    }

    // make sure we dont over/under extend
    public boolean isClimberStowed() {
        return(getClimberPos() <= 0);
    }

    public boolean isClimberDeployed() {
        return(getClimberPos() >= 23);
    }

    // find the stowed pos of the climber
    public void findStowedPos() {
        // move climber down slowly
        climberMotor.set(-0.05);

        // check if climber is still moving
        if (climberEncoder.getVelocity() < 1 && lastSystemState + 1 < getTime()) {
            climberMotor.set(0.0);

            // set state to stowed
            climberState = ClimberStates.STOWED;

            // set pos to 0 when using getClimberPos()
            climberEncoder.setPosition(0);
        }
    }

    public boolean isHome() {
        return climberState == ClimberStates.HOMING;
    }

    private double getTime() {
        return RobotController.getFPGATime()/1.0E6;
    }

    // Periodic method
    public void periodic() {
        ClimberStates nextSystemState = climberState;

        if (requestHoming) {
            climberState = ClimberStates.HOMING;
        } else if (requestStowed) {
            climberState = ClimberStates.STOWED;
        } else if (requestDeploy) {
            climberState = ClimberStates.DEPLOYED;
        }

        // Handle states and climber limits
        if (climberState == ClimberStates.STOWED && !isClimberStowed()) {
            climberMotor.set(-0.5);
        } else if (climberState == ClimberStates.DEPLOYED && !isClimberDeployed()) {
            climberMotor.set(0.5);
        } else if (climberState == ClimberStates.HOMING) {
            findStowedPos();
        } else {
            climberMotor.set(0.0);
        }

        if (nextSystemState != climberState) {
            lastSystemState = getTime();
        }
    }
    */
}