package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.sensors.ColorSensor;
import frc.robot.subsystems.swerve.Swerve;

import static frc.robot.Constants.Gut.*;

public class Gut {

    // State
    public enum GutStates {
        IDLE_NO_CARGO,
        IDLE_ONE_CARGO,
        IDLE_TWO_CARGO,
        INTAKE_NO_CARGO,
        INTAKE_ONE_CARGO,
        INTAKE_TWO_CARGO,
        OUTTAKE_ONE_CARGO,
        SHOOT_CARGO
    }

    // State
    private GutStates gutState;
    private boolean requestIntake = false;
    private boolean requestShoot = false;

    // Motors
    // names are distance from the intake 
    private CANSparkMax closeMotor;
    private CANSparkMax farMotor;
    
    // Sensors
    public final ColorSensor colorSensor = new ColorSensor();

    // Other variables
    private Alliance allianceColor;
    public double stateStartTime = 0.00;
    public Shooter shooter;
    public Intake intake;
    private double gutSpeed = 0.5;
    private Gut gut; 

    // Configure Gut on instantiation
    public Gut(Shooter shooter, Intake intake, Gut gut) {
        this.gut = gut;
        this.shooter = shooter;
        this.intake = intake;

        // Initial state
        gutState = GutStates.IDLE_NO_CARGO;

        // Initializing motor controllers
        closeMotor = new CANSparkMax(GUT_CLOSE_ID, MotorType.kBrushless);
        farMotor = new CANSparkMax(GUT_FAR_ID, MotorType.kBrushless);
        allianceColor = DriverStation.getAlliance();

        // Restore motor controller factory defaults
        closeMotor.restoreFactoryDefaults();
        farMotor.restoreFactoryDefaults();
    }

    // Public method to request shoot
    public void requestShoot() {
        requestShoot = true;
    }

    // Public method to request a state reset
    public void requestReset(boolean reset) {
        gutState = GutStates.IDLE_NO_CARGO;
    }

    // get time S
    private double getTime() {
        return RobotController.getFPGATime() / 1.0E6;
    }

    // Public method to handle state / output functions
    public void periodic() {
        if (gutState == GutStates.IDLE_NO_CARGO) {
            // State Outputs
            closeMotor.set(0.0);
            farMotor.set(0.0);

            // State Transitions
            if (requestIntake) {
                if (colorSensor.getColorFar() == allianceColor && colorSensor.getColorClose() == Alliance.Invalid) {
                    gutState = GutStates.INTAKE_ONE_CARGO;
                }

                if (colorSensor.getColorFar() == allianceColor && colorSensor.getColorClose() == allianceColor) {
                    gutState = GutStates.INTAKE_TWO_CARGO;
                }

                if (colorSensor.getColorFar() == allianceColor && colorSensor.getColorClose() != allianceColor) {
                    gutState = GutStates.OUTTAKE_ONE_CARGO;
                }

            } else {
                if (colorSensor.getColorFar() == allianceColor
                        && colorSensor.getColorClose() == Alliance.Invalid) {

                    gutState = GutStates.IDLE_ONE_CARGO;
                }

                if (colorSensor.getColorFar() == allianceColor && colorSensor.getColorClose() == allianceColor) {

                    gutState = GutStates.IDLE_TWO_CARGO;
                }

                if (colorSensor.getColorFar() == allianceColor && colorSensor.getColorClose() != allianceColor) {

                    gutState = GutStates.OUTTAKE_ONE_CARGO;
                }

            }

        }

        // If we have one correct cargo
        if (gutState == GutStates.IDLE_ONE_CARGO) {
                // State Transitions
            if (requestIntake && !requestShoot) {
                gutState = GutStates.INTAKE_ONE_CARGO;
            }

            if (requestShoot) {
                gutState = GutStates.SHOOT_CARGO;
            }

        }

        // If we have two correct cargo
        if (gutState == GutStates.IDLE_TWO_CARGO) {
            // State Transitions
            if (requestIntake && !requestShoot) {
                gutState = GutStates.INTAKE_TWO_CARGO;
            }

            if (requestShoot) {
                gutState = GutStates.SHOOT_CARGO;
            }

        }

        // Intaking based states

        // If we have no cargo
        if (gutState == GutStates.INTAKE_NO_CARGO) {

            // State Outputs
            // gut.set(gutSpeed);
            intake.requestDeploy(false);

            // Make sure shooter is idleing for the barf acttion
            shooter.requestIdle();

            // State Transitions
            if (!requestIntake) {
                gutState = GutStates.IDLE_NO_CARGO;
            }

            if (colorSensor.getColorFar() == allianceColor) {
                gutState = GutStates.INTAKE_ONE_CARGO;
            }

        }

        // If we have one ball and intaking
        if (gutState == GutStates.INTAKE_ONE_CARGO) {

            
            closeMotor.set(gutSpeed);

            intake.requestDeploy(false);

            // Make sure the backmost roller is not running so that we dont spit

            // State Transitions
            if (!requestIntake) {
                gutState = GutStates.IDLE_ONE_CARGO;
            }

            if (colorSensor.getColorClose() == allianceColor) {
                gutState = GutStates.INTAKE_TWO_CARGO;
            }

            if (requestShoot) {
                gutState = GutStates.SHOOT_CARGO;
            }

            if (colorSensor.getColorClose() != allianceColor) {
                gutState = GutStates.OUTTAKE_ONE_CARGO;
            }

        }

        if (gutState == GutStates.INTAKE_TWO_CARGO) {

            // State Outputs
            farMotor.set(gutSpeed);
            closeMotor.set(gutSpeed);

            // Make sure shooter is idleing for the shoot
            shooter.requestIdle();

            // State Transitions
            if (!requestIntake) {
                gutState = GutStates.IDLE_TWO_CARGO;
            }

            if (requestShoot) {
                gutState = GutStates.SHOOT_CARGO;
            }

        }

        if (gutState == GutStates.SHOOT_CARGO) {
            // State Outputs

            // Shooter subsystem should watch for this as a flag then spin up flywheel
            if (shooter.canShoot()) {

                if (colorSensor.getColorFar() == allianceColor && colorSensor.getColorClose() == allianceColor) {
                    farMotor.set(gutSpeed);
                    closeMotor.set(gutSpeed);
                    intake.requestDeploy(true);
                } else if (colorSensor.getColorFar() == allianceColor && colorSensor.getColorClose() != allianceColor) {
                    closeMotor.set(gutSpeed);
                }
            }
        }
        // State Transitions

        if (!requestShoot) {
            gutState = GutStates.IDLE_NO_CARGO;
        }

        // Outtake
        if (gutState == GutStates.OUTTAKE_ONE_CARGO) {

            // State Outputs
            intake.requestDeploy(true);

            // Make sure shooter is idleing for the shoot
            shooter.requestIdle();

            // State Transitions
            if (!shooter.isShooting()) {
                gutState = GutStates.IDLE_NO_CARGO;
            }
            else if (requestShoot) {
                gutState = GutStates.SHOOT_CARGO;
            }

            final double now = Timer.getMatchTime();

            // poor formatting, wait for 0.7 seconds
            if (getTime() - stateStartTime > 2) {
                if (requestIntake) {
                    gutState = GutStates.INTAKE_ONE_CARGO;
                } else {
                    gutState = GutStates.IDLE_ONE_CARGO;
                }
            }

        }
    }
}