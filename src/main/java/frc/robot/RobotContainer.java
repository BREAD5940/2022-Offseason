

package frc.robot;

//import edu.wpi.first.wpilibj.GenericHID;
//import edu.wpi.first.wpilibj.XboxController;
import frc.robot.subsystems.GutPrototype;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.autonomus.AutonomusSelector;

public class RobotContainer {
  public static GutPrototype gut = new GutPrototype();
  public static AutonomusSelector autonomusSelector = new AutonomusSelector(gut/*swerve, shooter, intake, gutNeck*/);


  public RobotContainer() {
    configureButtonBindings();
  }

  
  private void configureButtonBindings() {}


  public Command getAutonomousCommand() {
    return autonomusSelector.get();
  }
}
