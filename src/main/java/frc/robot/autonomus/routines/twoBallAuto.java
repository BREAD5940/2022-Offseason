package frc.robot.autonomus.routines;

import frc.robot.subsystems.swerve.Swerve;
import frc.robot.subsystems.Gut;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;


public class twoBallAuto extends BaseRoutineCode{

  public twoBallAuto(Swerve swerve, Shooter shooter, Intake intake, Gut gut) {
    super(swerve, shooter, intake, gut);
  }

  public void periodic() {
    // needs to be tested

    // shoot two balls
    if (timer.get() >= 0 && timer.get() < 1.5) {
      shooter.requestShoot(1);
      gut.requestShoot();
    }
    // swerve out of the tarmac
    if (timer.get() >= 2 && timer.get() < 4){
      swerve.requestManual(-1.0, 0, 0);
    }  
  }
}
