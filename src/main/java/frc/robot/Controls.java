package frc.robot;

import frc.robot.subsystems.swerve.Swerve;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Gut;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;

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
    }

    public void periodic() {
        /* Driver Controls */
        // Intake Controls
        if (driver.getRightTriggerAxis() >= 0.1) {
            intake.requestDeploy(false);
        } else if (driver.getLeftTriggerAxis() >= 0.1) {
            intake.requestDeploy(true);
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
        double dx = Math.abs(x) > 0.05 ? Math.pow(-x, 1) * Swerve.ROBOT_MAX_SPEED : 0.0;
        double dy = Math.abs(y) > 0.05 ? Math.pow(-y, 1) * Swerve.ROBOT_MAX_SPEED : 0.0;
        double rot = Math.abs(omega) > 0.1 ? Math.pow(-omega, 3) * 2.5 : 0.0;
        swerve.setSpeeds(dx, dy, rot);

        /* Operator Controls */
        // Climber Controls
        if (operator.getYButton()) {
            climber.requestDeploy();
        } else if (operator.getAButton()) {
            climber.requestStow();
        } else {
            climber.requestStow();
        }
    }
}
