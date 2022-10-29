// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
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
  public final MK2SwerveModule fl = new MK2SwerveModule(12, 13, 1, Units.degreesToRadians(175.9), false, false);
  public final MK2SwerveModule fr = new MK2SwerveModule(11, 10, 3, Units.degreesToRadians(111.3), false, false);
  public final MK2SwerveModule bl = new MK2SwerveModule(15, 14, 0, Units.degreesToRadians(10), false, false);
  public final MK2SwerveModule br = new MK2SwerveModule(17, 16, 2, Units.degreesToRadians(290.2), false, false);

  GutPrototype gutPrototype = new GutPrototype();
 // Swerve swerve = new Swerve();
  @Override
  public void robotInit() {}

  @Override
  public void robotPeriodic() {
   //fl.setState(new SwerveModuleState(2,Rotation2d.fromDegrees(0)));

      // SmartDashboard.putNumber("fl-raw-angle", Units.radiansToDegrees(swerve.fl.getModuleAngle()));
      // SmartDashboard.putNumber("fr-raw-angle",  Units.radiansToDegrees(swerve.fr.getModuleAngle()));
      // SmartDashboard.putNumber("bl-raw-angle",  Units.radiansToDegrees(swerve.bl.getModuleAngle()));
      // SmartDashboard.putNumber("br-raw-angle",  Units.radiansToDegrees(swerve.br.getModuleAngle()));
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
    fl.setState(new SwerveModuleState(2,Rotation2d.fromDegrees(0)));
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
    if (controller.getAButton()) {
      gutPrototype.spin(0.8);
    } else {
      gutPrototype.spin(0.0);
    }
  }


}
