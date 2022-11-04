package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.TimedRobot;



public class Robot extends TimedRobot {
  private RobotContainer m_robotContainer;
  private Controls controls;
  private Object auto;

  @Override
  public void robotInit() {
    m_robotContainer = new RobotContainer();
    controls = new Controls(m_robotContainer.swerve, m_robotContainer.shooter, m_robotContainer.intake, m_robotContainer.gut);
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
    controls.periodic();
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

  public void periodicTeleopControls() {}
}
