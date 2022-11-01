package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.subsystems.GutPrototype;


public class Robot extends TimedRobot {
  private RobotContainer m_robotContainer;

  public static XboxController controller = new XboxController(0);

  GutPrototype gutPrototype = new GutPrototype();

  @Override
  public void robotInit() {
    m_robotContainer = new RobotContainer();
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }

  @Override
  public void autonomousInit() {
  }
    
  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
  }
    
  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}

  // Controller Configuration
  public void configureTeleopControls() {
  }


}
