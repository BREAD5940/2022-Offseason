package frc.robot.autonomus.routines;

import frc.robot.subsystems.swerve.Swerve;
import edu.wpi.first.wpilibj.RobotController;
import frc.robot.subsystems.Gut;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

public class threeBallAuto {
  public Swerve swerve;
  public Shooter shooter;
  public Intake intake;
  public Gut gut;
  public double timeStarted = getTime();

  public threeBallAuto(Swerve swerve, Shooter shooter, Intake intake, Gut gut) {}

  private double getTime() {
    return RobotController.getFPGATime() / 1.0E6;
  }

  public void periodic() {
    double autoTime = getTime() - timeStarted;

    // needs to be tested

    // shoot two balls
    if (autoTime >= 0 && autoTime < 1.5) {
      shooter.requestShoot();
    }
    // swerve out of the tarmac
    if (autoTime >= 2 && autoTime < 4){
      swerve.requestManual(-1.0, -0.2, 1);
    }  
    // swerve inside of tarmac
    if (autoTime >= 3.5 && autoTime < 5){
      swerve.requestManual(1.0, 0.2, -1);
    }  
    // shoot two balls
    if (autoTime >= 5 && autoTime < 6.5) {
      shooter.requestShoot();
    }
  }
}