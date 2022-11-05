package frc.robot.autonomus.routines;

import edu.wpi.first.wpilibj.RobotController;
import frc.robot.subsystems.swerve.Swerve;
import frc.robot.subsystems.Gut;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter; 

public class templateRoutine {
  public Swerve swerve;
  public Shooter shooter;
  public Intake intake;
  public Gut gut;
  //public Timer timer = new Timer();

  public templateRoutine(Swerve swerve, Shooter shooter, Intake intake, Gut gut) {
    this.swerve = swerve;
    this.shooter = shooter;
    this.intake = intake;
    this.gut = gut;
  }

  public void start() {
    //timer.
  }

  public abstract void periodic();
}
