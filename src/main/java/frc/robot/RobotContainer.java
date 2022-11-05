
package frc.robot;

//import edu.wpi.first.wpilibj.GenericHID;
//import edu.wpi.first.wpilibj.XboxController;

import frc.robot.subsystems.swerve.Swerve;
import frc.robot.subsystems.Gut;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Climber;

// Autonomus
import frc.robot.autonomus.AutonomusSelector;
import frc.robot.autonomus.routines.twoBallAuto;

public class RobotContainer {
  public Swerve swerve = new Swerve();
  public Shooter shooter = new Shooter();
  public Intake intake = new Intake();
  public Gut gut = new Gut(shooter, intake);
  public Climber climber = new Climber();

  public AutonomusSelector autonomusSelector = new AutonomusSelector(swerve, shooter, intake, gut);

  public RobotContainer() {}

  public void periodic() {
    swerve.updateOdometry();
    swerve.periodic();
    climber.periodic();
    intake.periodic();
    shooter.periodic();
    gut.periodic();
  }

  public twoBallAuto getAutonomous() {
    return autonomusSelector.get();
  }
}
