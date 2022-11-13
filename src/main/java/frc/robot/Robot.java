package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.autonomus.routines.BaseRoutineCode;

import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.inputs.LoggedNetworkTables;
import org.littletonrobotics.junction.inputs.LoggedSystemStats;
import org.littletonrobotics.junction.io.ByteLogReceiver;
import org.littletonrobotics.junction.io.ByteLogReplay;
import org.littletonrobotics.junction.io.LogSocketServer;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import frc.robot.vision.AprilTagCode;

public class Robot extends LoggedRobot {

  private AprilTagCode testAprilTag = new AprilTagCode();

  private RobotContainer m_robotContainer;

  private Controls controls;
  private BaseRoutineCode auto;

  @Override
  public void robotInit() {
    m_robotContainer = new RobotContainer();
    controls = new Controls(m_robotContainer.swerve, m_robotContainer.shooter, m_robotContainer.intake, m_robotContainer.gut, m_robotContainer.climber);
    setUseTiming(isReal()); 
    LoggedNetworkTables.getInstance().addTable("/SmartDashboard"); 
    Logger.getInstance().recordMetadata("FRC-APRILTAGS-CRUMBS2022", "2022-Onseason"); 

    if (isReal()) {
      Logger.getInstance().addDataReceiver(new ByteLogReceiver("/media/sda1/")); 
      Logger.getInstance().addDataReceiver(new LogSocketServer(5800));
    } else {
      String path = ByteLogReplay.promptForPath();
      Logger.getInstance().setReplaySource(new ByteLogReplay(path)); 
      Logger.getInstance().addDataReceiver(new ByteLogReceiver(ByteLogReceiver.addPathSuffix(path, "_sim")));
    }

    LoggedSystemStats.getInstance().setPowerDistributionConfig(1, ModuleType.kRev);
    Logger.getInstance().start(); 
  }

  @Override
  public void robotPeriodic() {
    testAprilTag.calculate();
    //System.out.println("Field to Robot: " + testAprilTag.getPos());
    Pose3d pos=testAprilTag.getPos();
    if (pos != null){
      Logger.getInstance().recordOutput("AprilTagOdometry", new double[] {pos.getX(), pos.getY(), pos.getZ(), pos.getRotation().getAngle()});
    }
    // Logger.getInstance().recordOutput("AprilTagOdometry", new double[] {pos.getX(), pos.getY(), pos.getZ(), pos.getRotation().getAngle()});

  }

  @Override
  public void autonomousInit() {
    auto = m_robotContainer.getAutonomous();
    auto.start();
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