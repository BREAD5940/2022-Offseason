package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.sensors.ColorSensor;
import static frc.robot.Constants.Gut.*;
import frc.robot.subsystems.Shooter;
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
    private CANSparkMax closeMotor;
    private CANSparkMax farMotor;
    private CANSparkMax gutVecteryBois;
    private CANSparkMax gut;

    // Sensors
    public final ColorSensor colorSensor = new ColorSensor();

    // Other variables
    private Alliance allianceColor;
    public double stateStartTime = 0.00;
    public Shooter shooter;
    // Configure Gut on instantiation
    public Gut(Shooter shooter) {

        // Initial state
        gutState = GutStates.IDLE_NO_CARGO;

        // Initializing motor controllers
        closeMotor = new CANSparkMax(GUT_CLOSE_ID, MotorType.kBrushless);
        farMotor = new CANSparkMax(GUT_FAR_ID, MotorType.kBrushless);
        gutVecteryBois = farMotor;
        gut = closeMotor;
        allianceColor = DriverStation.getAlliance();
    }

    // Public method to request shoot
    public void requestShoot() {
        requestShoot = true;
    }

    // Public method to request a state reset
    public void requestReset(boolean reset) {
        gutState = GutStates.IDLE_NO_CARGO;
    }

    public void spinRollers() {
    }

    // Public method to handle state / output functions
    public void periodic() {
        if (gutState == GutStates.IDLE_NO_CARGO) {
            // State Outputs
            gut.set(0.0);
            gutVecteryBois.set(0.0); // fill in once defined later

            // State Transitions

            // IDLE based states

            if (requestIntake) {
                // If we want to intake and we are given these inital conditions then change
                // states to -->
                if (colorSensor.getColorFar() == allianceColor
                        && colorSensor.getColorClose() == Alliance.Invalid) { // intake no cargo
                    gutState = GutStates.INTAKE_ONE_CARGO;
                }

                if (colorSensor.getColorFar() == allianceColor && colorSensor.getColorClose() == allianceColor) {
                    gutState = GutStates.INTAKE_TWO_CARGO;
                }

                if (colorSensor.getColorFar() == allianceColor && colorSensor.getColorClose() != allianceColor) {
                    gutState = GutStates.OUTTAKE_ONE_CARGO;
                }

            } else {
                // When not requesting intake and we think we have no cargo double check that
                // all is true
                // I belive this is useful in autos and such

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

            // State Outputs
            gut.set(0.0);
            gutVecteryBois.set(0.0); // fill in once defined later

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

            // State Outputs
            gut.set(0.0);
            gutVecteryBois.set(0.0); // fill in once defined later

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
            gut.set(0.5);
            gutVecteryBois.set(0.5); // fill in once defined later

            // Make sure shooter is idleing for the barf acttion

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

            // State Outputs
            gut.set(0.5); // only close to intake (might be none)
            gutVecteryBois.set(0.5); // fill in once defined later

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
            gut.set(0.0);
            gutVecteryBois.set(0.0); // fill in once defined later

            // Make sure shooter is idleing for the shoot

            // State Transitions
            if (!requestIntake) {
                gutState = GutStates.IDLE_TWO_CARGO;
            }

            if (requestShoot) {
                gutState = GutStates.SHOOT_CARGO;
            }

        }

        // Special Cases, Shooting,

        if (gutState == GutStates.SHOOT_CARGO) {

            // State Outputs

            // shooter.setRPM(fender)

            // shooter subsystem should watch for this as a flag then spin up flywheel
            if (shooter.canShoot()) {

                if (colorSensor.getColorFar() == allianceColor && colorSensor.getColorClose() == allianceColor) {
                    gut.set(0.5);
                    gutVecteryBois.set(0.5); // fill in once defined later

                    // shoot
                }

                if (colorSensor.getColorFar() == allianceColor && colorSensor.getColorClose() == allianceColor) {
                    gut.set(0.5); // only shoot one ball
                }

                //

            }
            // State Transitions

            if (!requestShoot) {
                gutState = GutStates.IDLE_NO_CARGO;
            } // I can do this because of the logic in IDLE_NO_CARGO
              // it will auto sence what state it is acctually in

        }

        // Outtake
        if (gutState == GutStates.OUTTAKE_ONE_CARGO) {

            // State Outputs
            gut.set(0.0);
            gutVecteryBois.set(-0.5); // fill in once defined later

            // Make sure shooter is idleing for the shoot

            // State Transitions

            if (requestShoot) {
                gutState = GutStates.SHOOT_CARGO;
            }

            final double now = Timer.getMatchTime();

            // poor formatting, wait for 0.7 seconds
            if ((RobotController.getFPGATime() / 1.0E6) - stateStartTime > 2) {
                if (requestIntake) {
                    gutState = GutStates.INTAKE_ONE_CARGO;
                } else {
                    gutState = GutStates.IDLE_ONE_CARGO;
                }
            }

        }
    }
}