package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.autonomus.routines.BaseRoutineCode;
import frc.robot.subsystems.swerve.Swerve;

public class Robot extends TimedRobot {
  private RobotContainer m_robotContainer;

  private Controls controls;
  private BaseRoutineCode auto;
  public static double shooterSpeedCal = 4100.0;
  public static double idleSetpoint = 1500.0;

  @Override
  public void robotInit() {
    m_robotContainer = new RobotContainer();
    controls = new Controls(m_robotContainer.swerve, m_robotContainer.shooter, m_robotContainer.intake, m_robotContainer.gut, m_robotContainer.climber);
    SmartDashboard.putNumber("ShooterSpeedCal", shooterSpeedCal);
    SmartDashboard.putNumber("DriveDelay", 7);
    SmartDashboard.putNumber("DriveTime", 18 );
  }

  @Override
  public void robotPeriodic() {
    shooterSpeedCal = SmartDashboard.getNumber("ShooterSpeedCal", shooterSpeedCal);
    //SmartDashboard.putNumber("FL Swerve Angle", m_robotContainer.swerve.fl.getModuleAngle());
    //SmartDashboard.putNumber("FR Swerve Angle", m_robotContainer.swerve.fr.getModuleAngle());
    //SmartDashboard.putNumber("BR Swerve Angle", m_robotContainer.swerve.br.getModuleAngle());
    //SmartDashboard.putNumber("BL Swerve Angle", m_robotContainer.swerve.bl.getModuleAngle());
  }

  @Override
  public void autonomousInit() {
    auto = m_robotContainer.getAutonomous();
    auto.start();
    // reset systems
    m_robotContainer.intake.requestHome();
    m_robotContainer.swerve.reset(new Pose2d());
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