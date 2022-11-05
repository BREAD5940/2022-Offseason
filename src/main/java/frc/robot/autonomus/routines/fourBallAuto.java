package frc.robot.autonomus.routines;

import frc.robot.subsystems.swerve.Swerve;
import frc.robot.subsystems.Gut;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

public class fourBallAuto extends BaseRoutineCode{
  
  public fourBallAuto(Swerve swerve, Shooter shooter, Intake intake, Gut gut) {
    super(swerve, shooter, intake, gut);
  }

  public void periodic() {
    boolean isShooting = false;
    /* auto start */
    // needs to be tested

    // shoot two balls
    if (timer.get() >= 0 && timer.get() < 1.5) {
      isShooting = true;
    }
    if (timer.get() >= 3 && timer.get() < 5){
      swerve.requestManual(-0.5, 0.0, 0.0);
    }
    // swerve out of the tarmac
    if (timer.get() >= 1.5 && timer.get() < 3.5){
      swerve.requestManual(-1.0, -0.2, 1);
    }  
    // swerve inside of tarmac
    if (timer.get() >= 3.5 && timer.get() < 5){
      swerve.requestManual(1.0, 0.2, -1);
    }  
    // shoot two balls
    if (timer.get() >= 5 && timer.get() < 6.5) {
      isShooting = true;
    }

    /* auto end */

    // handles shooting
    if (isShooting == true) {
      shooter.requestShoot(1);
      gut.requestShoot();
    } else {
      shooter.requestIdle();
    }
  }
}