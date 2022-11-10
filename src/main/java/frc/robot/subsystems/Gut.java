package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.sensors.ColorSensor;

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
    //private boolean intake.isIntakeDeployed() = false;
    private boolean requestShoot = false;

    // Motors
    // names are distance from the intake 
    private CANSparkMax closeMotor;
    private CANSparkMax farMotor;
    
    // Sensors
    public final ColorSensor colorSensor = new ColorSensor();
    public final Boolean beamBreakClose = false; // add beam break here
    public final Boolean beamBreakFar = false; // add beam break here

// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
//                          look here
// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^


    // Other variables
    private Alliance allianceColor;
    public double stateStartTime = 0.00;
    public Shooter shooter;
    public Intake intake;
    private double gutSpeed = 0.5;
    private double gutShootSpeed = 0.5;
    public boolean operatorRequestGut = false;
    public boolean operatorRequestGutDirection = false;

    // Configure Gut on instantiation
    public Gut(Shooter shooter, Intake intake) {
        this.shooter = shooter;
        this.intake = intake;

        // Initial state
        gutState = GutStates.IDLE_NO_CARGO;

        // Initializing motor controllers
        closeMotor = new CANSparkMax(GUT_CLOSE_ID, MotorType.kBrushless);
        farMotor = new CANSparkMax(GUT_FAR_ID, MotorType.kBrushless);

        // Restore motor controller factory defaults
        closeMotor.restoreFactoryDefaults();
        farMotor.restoreFactoryDefaults();
    }

    // Public method to request shoot
    public void requestShoot() {
        requestShoot = true;
    }

    public void stopRequestShoot() {
        requestShoot = false;
    }

    // Public method to request a state reset
    public void requestReset(boolean reset) {
        gutState = GutStates.IDLE_NO_CARGO;
    }

    // get time S
    private double getTime() {
        return RobotController.getFPGATime() / 1.0E6;
    }

    // operator requests
    public void operatorSpinGut (Boolean spinBackward) {
        operatorRequestGut = true;
        operatorRequestGutDirection = spinBackward;
    }

    // Public method to handle state / output functions
    public void periodic() {
        allianceColor = DriverStation.getAlliance();
        SmartDashboard.putString("allianceColor", allianceColor.toString());

        SmartDashboard.putString("gutState", gutState.toString());

//----------------------------------------------------------------- start
 // no sensor code
        if (gutState == GutStates.IDLE_NO_CARGO) {
            // State Outputs
            closeMotor.set(0.0);
            farMotor.set(0.0);

            // State Transitions
            if (requestShoot) {
                gutState = GutStates.SHOOT_CARGO;

            } else if (intake.isIntakeDeployed()) {
                gutState = GutStates.INTAKE_NO_CARGO;
            } 

        }

        // Intaking based states

        else if (gutState == GutStates.INTAKE_NO_CARGO) {

            // State Outputs
            farMotor.set(-0.1);
            closeMotor.set(0.1);

            //intake.requestDeploy(false);

            // Make sure shooter is idleing for the barf acttion
            shooter.requestIdle();

            // State Transitions
            if (requestShoot) {
                gutState = GutStates.SHOOT_CARGO;

            } else if (!intake.isIntakeDeployed()) {
                gutState = GutStates.IDLE_NO_CARGO;
            }
        }

        else if (gutState == GutStates.SHOOT_CARGO) {
            // State Outputs
            farMotor.set(0);
            closeMotor.set(0);

            // checks if the shooter is up to speed
            if (shooter.canShoot()) {
                farMotor.set(gutShootSpeed);
                closeMotor.set(gutShootSpeed);
            }

            // State Transitions
            if (!requestShoot || !shooter.isShooting()) {
                gutState = GutStates.IDLE_NO_CARGO;
            }

        }
//----------------------------------------------------------------- end
    
        // Operator overides
        if (operatorRequestGut) {
            // spin backard if true
            if (operatorRequestGutDirection) {
                farMotor.set(-gutSpeed);
                closeMotor.set(-gutSpeed);
            } else {
                farMotor.set(gutSpeed);
                closeMotor.set(gutSpeed);
            }
        }
    }
}


/*
    no sensor code
--------------------------------------------------------
        // no sensor code
        if (gutState == GutStates.IDLE_NO_CARGO) {
            // State Outputs
            closeMotor.set(0.0);
            farMotor.set(0.0);

            // State Transitions
            if (requestShoot) {
                gutState = GutStates.SHOOT_CARGO;

            } else if (intake.isIntakeDeployed()) {
                gutState = GutStates.INTAKE_NO_CARGO;
            } 

        }

        // Intaking based states

        else if (gutState == GutStates.INTAKE_NO_CARGO) {

            // State Outputs
            farMotor.set(-0.1);
            closeMotor.set(0.1);

            //intake.requestDeploy(false);

            // Make sure shooter is idleing for the barf acttion
            shooter.requestIdle();

            // State Transitions
            if (requestShoot) {
                gutState = GutStates.SHOOT_CARGO;

            } else if (!intake.isIntakeDeployed()) {
                gutState = GutStates.IDLE_NO_CARGO;
            }
        }

        else if (gutState == GutStates.SHOOT_CARGO) {
            // State Outputs
            farMotor.set(0);
            closeMotor.set(0);

            // checks if the shooter is up to speed
            if (shooter.canShoot()) {
                farMotor.set(gutShootSpeed);
                closeMotor.set(gutShootSpeed);
            }

            // State Transitions
            if (!requestShoot || !shooter.isShooting()) {
                gutState = GutStates.IDLE_NO_CARGO;
            }

        }
--------------------------------------------------------
*/


/*
    beambreak code
--------------------------------------------------------
    // beambreak code
    if (gutState == GutStates.IDLE_NO_CARGO) {
            // State Outputs
            closeMotor.set(0.0);
            farMotor.set(0.0);

            // State Transitions
            if (intake.isIntakeDeployed()) {
                if (beamBreakFar == true && beamBreakClose == false) {
                    gutState = GutStates.INTAKE_ONE_CARGO;

                } else if (beamBreakFar == true && beamBreakClose == true) {
                    gutState = GutStates.INTAKE_TWO_CARGO;

                } else if (beamBreakFar == false && beamBreakFar == true) {
                    gutState = GutStates.INTAKE_NO_CARGO;
                }

            } else {
                if (beamBreakFar == true && beamBreakClose == false) {
                    gutState = GutStates.IDLE_ONE_CARGO;
                    
                } else if (beamBreakFar == true && beamBreakClose == true) {
                    gutState = GutStates.IDLE_TWO_CARGO;
                    
                }

            }

        }

        // If we have one correct cargo
        else if (gutState == GutStates.IDLE_ONE_CARGO) {
            
            // state outputs
            closeMotor.set(0.0);
            farMotor.set(0.0);

            // State Transitions
            if (intake.isIntakeDeployed() && !requestShoot) {
                gutState = GutStates.INTAKE_ONE_CARGO;
            }

            if (requestShoot) {
                gutState = GutStates.SHOOT_CARGO;
            }

        } 

        // If we have two correct cargo
        else if (gutState == GutStates.IDLE_TWO_CARGO) {

            // State Outputs
            closeMotor.set(0.0);
            farMotor.set(0.0);
                        
            // State Transitions
            if (intake.isIntakeDeployed() && !requestShoot) {
                gutState = GutStates.INTAKE_TWO_CARGO;
            }

            if (requestShoot) {
                gutState = GutStates.SHOOT_CARGO;
            }

        }

        // Intaking based states

        // If we have no cargo
        else if (gutState == GutStates.INTAKE_NO_CARGO) {

            // State Outputs
            farMotor.set(gutSpeed);
            closeMotor.set(gutSpeed);

            //intake.requestDeploy(false);

            // Make sure shooter is idleing for the barf acttion
            shooter.requestIdle();

            // State Transitions
            if (!intake.isIntakeDeployed()) {
                gutState = GutStates.IDLE_NO_CARGO;
            }

            if (beamBreakFar == true) {
                gutState = GutStates.INTAKE_ONE_CARGO;
            }

        }

        // If we have one ball and intaking
        else if (gutState == GutStates.INTAKE_ONE_CARGO) {

            // State Outputs
            closeMotor.set(gutSpeed);
            farMotor.set(0);

            //intake.requestDeploy(false);

            // State Transitions
            if (beamBreakClose == true) {
                gutState = GutStates.INTAKE_TWO_CARGO;

            } else if (!intake.isIntakeDeployed()) {
                gutState = GutStates.IDLE_ONE_CARGO;

            } else if (requestShoot) {
                gutState = GutStates.SHOOT_CARGO;
            }

        }

        else if (gutState == GutStates.INTAKE_TWO_CARGO) {

            // State Outputs
            farMotor.set(gutSpeed);
            closeMotor.set(gutSpeed);

            // Make sure shooter is idleing for the shoot
            shooter.requestIdle();

            // State Transitions
            if (!intake.isIntakeDeployed()) {
                gutState = GutStates.IDLE_TWO_CARGO;
            } else if (requestShoot) {
                gutState = GutStates.SHOOT_CARGO;
            }

        }

        else if (gutState == GutStates.SHOOT_CARGO) {
            // State Outputs
            farMotor.set(0);
            closeMotor.set(0);

            // checks if the shooter is up to speed
            if (shooter.canShoot()) {

                if (beamBreakFar == true && beamBreakClose == true) {
                    farMotor.set(gutShootSpeed);
                    closeMotor.set(gutShootSpeed);
                    intake.requestDeploy(true);

                } else if (beamBreakFar == true && beamBreakClose == false) {
                    farMotor.set(gutSpeed);
                }
            }

            // State Transitions
            if (!requestShoot) {
                gutState = GutStates.IDLE_NO_CARGO;
            } else if (!shooter.isShooting()) {
                gutState = GutStates.IDLE_NO_CARGO;
            }
            
        }
--------------------------------------------------------
*/


/*
    color sensor code
--------------------------------------------------------
    // color sensor code
    if (gutState == GutStates.IDLE_NO_CARGO) {
            // State Outputs
            closeMotor.set(0.0);
            farMotor.set(0.0);

            // State Transitions
            if (intake.isIntakeDeployed()) {
                if (colorSensor.getColorFar() == allianceColor && colorSensor.getColorClose() != allianceColor) {
                    gutState = GutStates.INTAKE_ONE_CARGO;

                } else if (colorSensor.getColorFar() == allianceColor && colorSensor.getColorClose() == allianceColor) {
                    gutState = GutStates.INTAKE_TWO_CARGO;

                } else if (colorSensor.getColorFar() == allianceColor && colorSensor.getColorClose() != Alliance.Invalid) {
                    stateStartTime = getTime();
                    gutState = GutStates.OUTTAKE_ONE_CARGO;

                } else if (colorSensor.getColorFar() != allianceColor && colorSensor.getColorClose() != allianceColor) {
                    gutState = GutStates.INTAKE_NO_CARGO;
                }

            } else {
                if (colorSensor.getColorFar() == allianceColor && colorSensor.getColorClose() != allianceColor) {
                    gutState = GutStates.IDLE_ONE_CARGO;
                    
                } else if (colorSensor.getColorFar() == allianceColor && colorSensor.getColorClose() == allianceColor) {
                    gutState = GutStates.IDLE_TWO_CARGO;
                    
                } else if (colorSensor.getColorFar() == allianceColor && colorSensor.getColorClose() != allianceColor) {
                    stateStartTime = getTime();
                    gutState = GutStates.OUTTAKE_ONE_CARGO;
                } 

            }

        }

        // If we have one correct cargo
        else if (gutState == GutStates.IDLE_ONE_CARGO) {
            
            // state outputs
            closeMotor.set(0.0);
            farMotor.set(0.0);

            // State Transitions
            if (intake.isIntakeDeployed() && !requestShoot) {
                gutState = GutStates.INTAKE_ONE_CARGO;
            }

            if (requestShoot) {
                gutState = GutStates.SHOOT_CARGO;
            }

        } 

        // If we have two correct cargo
        else if (gutState == GutStates.IDLE_TWO_CARGO) {

            // State Outputs
            closeMotor.set(0.0);
            farMotor.set(0.0);
                        
            // State Transitions
            if (intake.isIntakeDeployed() && !requestShoot) {
                gutState = GutStates.INTAKE_TWO_CARGO;
            }

            if (requestShoot) {
                gutState = GutStates.SHOOT_CARGO;
            }

        }

        // Intaking based states

        // If we have no cargo
        else if (gutState == GutStates.INTAKE_NO_CARGO) {

            // State Outputs
            farMotor.set(gutSpeed);
            closeMotor.set(gutSpeed);

            //intake.requestDeploy(false);

            // Make sure shooter is idleing for the barf acttion
            shooter.requestIdle();

            // State Transitions
            if (!intake.isIntakeDeployed()) {
                gutState = GutStates.IDLE_NO_CARGO;
            }

            if (colorSensor.getColorFar() == allianceColor) {
                gutState = GutStates.INTAKE_ONE_CARGO;
            }

        }

        // If we have one ball and intaking
        else if (gutState == GutStates.INTAKE_ONE_CARGO) {

            // State Outputs
            closeMotor.set(gutSpeed);
            farMotor.set(0);

            //intake.requestDeploy(false);

            // State Transitions
            if (colorSensor.getColorClose() == Alliance.Invalid) {
                stateStartTime = getTime();
                gutState = GutStates.OUTTAKE_ONE_CARGO;

            } else if (colorSensor.getColorClose() == allianceColor) {
                gutState = GutStates.INTAKE_TWO_CARGO;

            } else if (!intake.isIntakeDeployed()) {
                gutState = GutStates.IDLE_ONE_CARGO;

            } else if (requestShoot) {
                gutState = GutStates.SHOOT_CARGO;
            }

        }

        else if (gutState == GutStates.INTAKE_TWO_CARGO) {

            // State Outputs
            farMotor.set(gutSpeed);
            closeMotor.set(gutSpeed);

            // Make sure shooter is idleing for the shoot
            shooter.requestIdle();

            // State Transitions
            if (!intake.isIntakeDeployed()) {
                gutState = GutStates.IDLE_TWO_CARGO;
            } else if (requestShoot) {
                gutState = GutStates.SHOOT_CARGO;
            }

        }

        else if (gutState == GutStates.SHOOT_CARGO) {
            // State Outputs
            farMotor.set(0);
            closeMotor.set(0);

            // checks if the shooter is up to speed
            if (shooter.canShoot()) {

                if (colorSensor.getColorFar() == allianceColor && colorSensor.getColorClose() == allianceColor) {
                    farMotor.set(gutSpeed);
                    closeMotor.set(gutSpeed);
                    intake.requestDeploy(true);

                } else if (colorSensor.getColorFar() == allianceColor && colorSensor.getColorClose() != allianceColor) {
                    farMotor.set(gutSpeed);
                }
            }

            // State Transitions
            if (!requestShoot) {
                gutState = GutStates.IDLE_NO_CARGO;
            } else if (!shooter.isShooting()) {
                gutState = GutStates.IDLE_NO_CARGO;
            }
            
        }


        // Outtake
        else if (gutState == GutStates.OUTTAKE_ONE_CARGO) {
            
            // State Outputs
            intake.requestDeploy(true);
            closeMotor.set(-gutSpeed);

            // Make sure shooter is idleing for the shoot
            shooter.requestIdle();

            // State Transitions
            
            if (requestShoot) {
                gutState = GutStates.SHOOT_CARGO;
            } else // Look down for the else \/

            // wait for 0.7 seconds before state state can change
            if (getTime() - stateStartTime > 0.7) {
                if (intake.isIntakeDeployed()) {
                    gutState = GutStates.INTAKE_ONE_CARGO;
                } else {
                    gutState = GutStates.IDLE_ONE_CARGO;
                }
            }
        }
--------------------------------------------------------
*/