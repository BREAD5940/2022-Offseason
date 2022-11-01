

package frc.robot;

//import edu.wpi.first.wpilibj.GenericHID;
//import edu.wpi.first.wpilibj.XboxController;

import frc.robot.subsystems.swerve.Swerve;
import frc.robot.subsystems.Gut;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

import frc.robot.autonomus.AutonomusSelector;

public class RobotContainer {
  public static Swerve swerve = new Swerve();
  public static Shooter shooter = new Shooter();
  public static Gut gut = new Gut(shooter);
  public static Intake intake = new Intake();


  public static AutonomusSelector autonomusSelector = new AutonomusSelector(swerve, shooter, intake, gut);


  public RobotContainer() {
    configureButtonBindings();
  }

  
  private void configureButtonBindings() {}


  public Object getAutonomousCommand() {
    return autonomusSelector.get();
  }
}
