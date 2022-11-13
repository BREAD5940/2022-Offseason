package frc.robot.autonomus.routines;

import frc.robot.subsystems.swerve.Swerve;
import frc.robot.subsystems.Gut;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

public class threeBallAuto extends BaseRoutineCode{

  public threeBallAuto(Swerve swerve, Shooter shooter, Intake intake, Gut gut) {
    super(swerve, shooter, intake, gut);
  }

  public void periodic() {
    boolean isShooting = false;
    boolean isMoving = false;
    /* auto start */
    // needs to be tested

    // shoot two balls
    if (timer.get() >= 0 && timer.get() < 1.5) {
      isShooting = true;
    }
    // swerve out of the tarmac
    if (timer.get() >= 2 && timer.get() < 4){
      swerve.requestManual(-1.0, -0.2, 1);
      isMoving = true;
    }  
    // swerve inside of tarmac
    if (timer.get() >= 3.5 && timer.get() < 5){
      swerve.requestManual(1.0, 0.2, -1);
      isMoving = true;
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
    if (!isMoving) {
        swerve.requestManual(0, 0, 0);
    }
  }
}