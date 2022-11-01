// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.GutPrototype;
import frc.robot.subsystems.swerve.MK2SwerveModule;
import frc.robot.subsystems.swerve.Swerve;
/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */


  public static XboxController controller = new XboxController(0);

  GutPrototype gutPrototype = new GutPrototype();
  Swerve swerve = new Swerve();

  @Override
  public void robotInit() {}

  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("Gyro Angle", swerve.getRawGyro());
    SmartDashboard.putNumber("FL-angle", swerve.fl.getModuleAngle());
    SmartDashboard.putNumber("FR-angle", swerve.fr.getModuleAngle());
    SmartDashboard.putNumber("BL-angle", swerve.bl.getModuleAngle());
    SmartDashboard.putNumber("BR-angle", swerve.br.getModuleAngle());

  }

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {}

  @Override
  public void teleopPeriodic() {
    configureTeleopControls();
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
    swerve.updateOdometry();

    if (controller.getAButton()) {
      gutPrototype.spin(0.8);
    } else {
      gutPrototype.spin(0.0);
    }

    if (controller.getRawButton(Button.kStart.value)) {
      swerve.reset(new Pose2d());
    }
  }


}
