package frc.robot;

import frc.robot.subsystems.swerve.Swerve;
import frc.robot.subsystems.Gut;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;

public class Controls {
    public static XboxController controller = new XboxController(0);
    public Swerve swerve;
    public Shooter shooter;
    public Intake intake;
    public Gut gut;

    // get subsystems 
    public Controls(Swerve swerve, Shooter shooter, Intake intake, Gut gut) {}

    public void periodic() {
        // Intake Controls
        if (controller.getRightTriggerAxis() >= 0.1) {
            intake.requestDeploy(false);
        } else if (controller.getLeftTriggerAxis() >= 0.1) {
            intake.requestDeploy(true);
        } else {
            intake.requestStow();
        }

        // Swerve Controls

        // reset rotation
        if (controller.getRawButton(Button.kStart.value)) {
            swerve.reset(new Pose2d());
        }
        
        //movement
        double x = controller.getRightY();
        double y = controller.getRightX();

        //Outputs 
        double omega = controller.getLeftX();
        double dx = Math.abs(x) > 0.05 ? Math.pow(-x, 1) * Swerve.ROBOT_MAX_SPEED : 0.0;
        double dy = Math.abs(y) > 0.05 ? Math.pow(-y, 1) * Swerve.ROBOT_MAX_SPEED : 0.0;
        double rot = Math.abs(omega) > 0.1 ? Math.pow(-omega, 3) * 2.5 : 0.0;
        swerve.setSpeeds(dx, dy, rot);
    }
}
