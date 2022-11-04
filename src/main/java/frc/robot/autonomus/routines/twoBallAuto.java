package frc.robot.autonomus.routines;

import frc.robot.subsystems.swerve.Swerve;
import edu.wpi.first.wpilibj.RobotController;
import frc.robot.subsystems.Gut;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

public class twoBallAuto {
  public Swerve swerve;
  public Shooter shooter;
  public Intake intake;
  public Gut gut;
  public double timeStarted = getTime();

  public twoBallAuto(Swerve swerve, Shooter shooter, Intake intake, Gut gut) {}

  private double getTime() {
    return RobotController.getFPGATime() / 1.0E6;
  }

  public void periodic() {
    double autoTime = getTime() - timeStarted;

    if (autoTime >= 0 && autoTime < 2) {
      shooter.requestShoot();
    }
    if (autoTime >= 3 && autoTime < 5){
      swerve.requestManual(-1.0, 0.0, 0.0);
    }
  }
}