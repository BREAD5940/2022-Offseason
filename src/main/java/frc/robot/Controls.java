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
    public static XboxController controller0 = new XboxController(0);
    public static XboxController controller1 = new XboxController(1);
    public Swerve swerve;
    public Shooter shooter;
    public Intake intake;
    public Gut gut;
    public Climber climber;

    // get subsystems 
    public Controls(Swerve swerve, Shooter shooter, Intake intake, Gut gut, Climber climber) {}

    public void periodic() {
        // Intake Controls
        if (controller0.getRightTriggerAxis() >= 0.1) {
            intake.requestDeploy(false);
        } else if (controller0.getLeftTriggerAxis() >= 0.1) {
            intake.requestDeploy(true);
        } else {
            intake.requestStow();
        }

        // Swerve Controls

        // reset rotation
        if (controller0.getRawButton(Button.kStart.value)) {
            swerve.reset(new Pose2d());
        }
        
        // movement inputs
        double x = controller0.getRightY();
        double y = controller0.getRightX();
        double omega = controller0.getLeftX();

        // movement outputs 
        double dx = Math.abs(x) > 0.05 ? Math.pow(-x, 1) * Swerve.ROBOT_MAX_SPEED : 0.0;
        double dy = Math.abs(y) > 0.05 ? Math.pow(-y, 1) * Swerve.ROBOT_MAX_SPEED : 0.0;
        double rot = Math.abs(omega) > 0.1 ? Math.pow(-omega, 3) * 2.5 : 0.0;
        swerve.setSpeeds(dx, dy, rot);

        // Climber Controls
        if (controller.getYButton()) {
            climber.requestDeploy();
        } else if (controller.getAButton()) {
            climber.requestStow();
        } else {
            climber.requestStow();
        }
    }
}
