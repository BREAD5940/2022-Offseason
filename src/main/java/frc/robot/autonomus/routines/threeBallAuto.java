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
    
    // needs to be tested

    // shoot two balls
    if (timer.get() >= 0 && timer.get() < 1.5) {
      shooter.requestShoot(1);
    }
    // swerve out of the tarmac
    if (timer.get() >= 2 && timer.get() < 4){
      swerve.requestManual(-1.0, -0.2, 1);
    }  
    // swerve inside of tarmac
    if (timer.get() >= 3.5 && timer.get() < 5){
      swerve.requestManual(1.0, 0.2, -1);
    }  
    // shoot two balls
    if (timer.get() >= 5 && timer.get() < 6.5) {
      shooter.requestShoot(1);
    }
  }
}