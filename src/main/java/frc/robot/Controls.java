package frc.robot;

import frc.robot.subsystems.swerve.Swerve;
import frc.robot.sensors.ColorSensor;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Gut;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.DriverStation.Alliance;


public class Controls {
    public static XboxController driver = new XboxController(0);
    public static XboxController operator = new XboxController(1);
    public Swerve swerve;
    public Shooter shooter;
    public Intake intake;
    public Gut gut;
    public Climber climber;

    // get subsystems
    public Controls(Swerve swerve, Shooter shooter, Intake intake, Gut gut, Climber climber) {
        this.swerve = swerve;
        this.shooter = shooter;
        this.intake = intake;
        this.gut = gut;
        this.climber = climber;
    }


    
        /*controls*/

    /*
        ---- driver ----
    translate : Right JoyStick
    rotation : Left JoyStick

    reset rotation : kStart

       ---- operator ----
    intake roll in : Right Trigger
    intake roll out : Left Trigger

    shoot : A Button

    stop shooter : Y Button
    start shooter : X Button

    climber up : pov pad up
    cloimber down : pov pad down

    spin gut in : Right Bumper
    spin gut out : Left Bumper
    */

    public void periodic() {

        // Intake Controls
        if (driver.getLeftTriggerAxis() >= 0.1 || operator.getLeftTriggerAxis() >= 0.1) {
            intake.requestDeploy(true);
            gut.operatorRequestGut = true;
            gut.operatorRequestGutDirection = true;
            shooter.operatorRequestGut = true;
        } else if (driver.getRightTriggerAxis() >= 0.1 || operator.getRightTriggerAxis() >= 0.1) {
            intake.requestDeploy(false);
        } else {
            intake.requestStow();
        }

        // Swerve Controls

        // reset rotation
        if (driver.getRawButton(Button.kStart.value)) {
            swerve.reset(new Pose2d());
        }

        // movement inputs
        double x = driver.getRightY();
        double y = driver.getRightX();
        double omega = driver.getLeftX();

        // movement outputs
        double dx = Math.abs(x) > 0.1 ? Math.pow(-x, 1) * Swerve.ROBOT_MAX_SPEED : 0.0;
        double dy = Math.abs(y) > 0.1 ? Math.pow(-y, 1) * Swerve.ROBOT_MAX_SPEED : 0.0;
        double rot = Math.abs(omega) > 0.1 ? Math.pow(-omega, 3) * 2.5 : 0.0;
        swerve.setSpeeds(dx, dy, rot);

        // Shooter Controls
        if (operator.getXButtonPressed()) {
            if (Robot.idleSetpoint == 1500.0) {
                Robot.idleSetpoint  = 0.0;
            } else {
                Robot.idleSetpoint = 1500.0;
            }
        }

        if (operator.getAButton()) {            
            shooter.requestShoot(Robot.shooterSpeedCal);            
            gut.requestShoot();
        } else {
            shooter.requestIdle();
            gut.stopRequestShoot();
        }

        // Climber Controls
        if (operator.getPOV() != -1) {
            if (operator.getPOV() >= 315 || operator.getPOV() <= 45) {
                climber.jogUp();
            } else if (operator.getPOV() <= 225 && operator.getPOV() >= 135) {
                climber.jogDown();
            } else {
                climber.dontJog();
            }
        } else {
            climber.dontJog();
        }


        // gut overides
        if (operator.getRightBumper()) {
            gut.operatorSpinGut(false);
        } else if (operator.getLeftBumper()) {
            gut.operatorSpinGut(true);
        }

        // old code
        /*
         * // check if the climber is homing
         * if (climber.isHome()) {
         * // decide which type of control to use for climber
         * if (oneButton) {
         * // for one button togle
         * if (operator.getAButton() && climber.isClimberStowed()) {
         * climber.requestDeploy();
         * } else {
         * climber.requestStow();
         * }
         * } else {
         * // for 2 button togle
         * if (operator.getAButton() && !climber.isClimberDeployed()) {
         * climber.requestDeploy();
         * } else if (operator.getXButton() && !climber.isClimberStowed()) {
         * climber.requestStow();
         * }
         * }
         * }
         * 
         * // Home climber
         * // make sure add operator control
         * if (false) {
         * climber.requestHoming();
         * }
         */
    }
}
