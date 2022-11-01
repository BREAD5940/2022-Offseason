package frc.robot.autonomus.routines;


import frc.robot.subsystems.swerve.Swerve;
import edu.wpi.first.wpilibj.RobotController;
import frc.robot.subsystems.Gut;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;


public class testRoutine {
  public Swerve swerve;
  public Shooter shooter;
  public Intake intake;
  public Gut gut;
  public double timeStarted = getTime();

  public testRoutine(Swerve swerve, Shooter shooter, Intake intake, Gut gut) {
  }

  private double getTime() {
    return RobotController.getFPGATime()/1.0E6;
  }

  public void periodic() {
    double autoTime = getTime() - timeStarted;
    // time is in seconds
    // if (autoTime >= startTime && autoTime < endTime) {
    //   add command here
    // }
    // copy and paste
  }
}
