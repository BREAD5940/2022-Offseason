
package frc.robot;

//import edu.wpi.first.wpilibj.GenericHID;
//import edu.wpi.first.wpilibj.XboxController;

import frc.robot.subsystems.swerve.Swerve;
import frc.robot.subsystems.Gut;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

import frc.robot.autonomus.AutonomusSelector;

public class RobotContainer {
  public Swerve swerve = new Swerve();
  public Shooter shooter = new Shooter();
  public Intake intake = new Intake();
  public Gut gut = new Gut(shooter, intake);

  public AutonomusSelector autonomusSelector = new AutonomusSelector(swerve, shooter, intake, gut);

  public RobotContainer() {
    shooter.gutInput(gut);
    configureButtonBindings();
  }

  private void configureButtonBindings() {
  }

  public Object getAutonomous() {
    return autonomusSelector.get();
  }
}
