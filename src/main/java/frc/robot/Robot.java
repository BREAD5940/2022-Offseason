package frc.robot;

import javax.xml.namespace.QName;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.autonomus.routines.twoBallAuto;

public class Robot extends TimedRobot {
  private RobotContainer m_robotContainer;

  private Controls controls;
  private twoBallAuto auto;

  @Override
  public void robotInit() {
    m_robotContainer = new RobotContainer();
    controls = new Controls(m_robotContainer.swerve, m_robotContainer.shooter, m_robotContainer.intake, m_robotContainer.gut, m_robotContainer.climber);
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
    auto = m_robotContainer.getAutonomous();

    // reset systems
    m_robotContainer.intake.requestHome();
    //m_robotContainer.climber.requestHoming();
  }

  @Override
  public void autonomousPeriodic() {
    m_robotContainer.periodic();
    auto.periodic();
  }

  @Override
  public void teleopInit() {
    m_robotContainer.swerve.requestTeleop();
  }

  @Override
  public void teleopPeriodic() {
    m_robotContainer.periodic();
    controls.periodic();
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
}