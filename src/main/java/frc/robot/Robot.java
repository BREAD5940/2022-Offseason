package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;

public class Robot extends TimedRobot {
  private RobotContainer m_robotContainer;

  public static XboxController controller = new XboxController(0);
  private Object auto;

  @Override
  public void robotInit() {
    m_robotContainer = new RobotContainer();
    m_robotContainer.intake.requestHome();
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
    auto = m_robotContainer.getAutonomous();
  }

  @Override
  public void autonomousPeriodic() {
    // auto.periodic();
  }

  @Override
  public void teleopInit() {
    m_robotContainer.swerve.requestTeleop();
  }

  @Override
  public void teleopPeriodic() {
    m_robotContainer.swerve.updateOdometry();
    m_robotContainer.swerve.periodic();
    m_robotContainer.intake.periodic();

    periodicTeleopControls();
  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

  @Override
  public void simulationInit() {
  }

  @Override
  public void simulationPeriodic() {
  }

  public void periodicTeleopControls() {
    // Intake Controls
    if (controller.getRightTriggerAxis() >= 0.1) {
      m_robotContainer.intake.requestDeploy(false);
    } else if (controller.getLeftTriggerAxis() >= 0.1) {
      m_robotContainer.intake.requestDeploy(true);
    } else {
      m_robotContainer.intake.requestStow();
    }

    // Swerve Controls
    if (controller.getRawButton(Button.kStart.value)) {
      m_robotContainer.swerve.reset(new Pose2d());
    }
  }
}
