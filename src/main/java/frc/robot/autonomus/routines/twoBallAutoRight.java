package frc.robot.autonomus.routines;

import frc.robot.subsystems.swerve.Swerve;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.subsystems.Gut;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;


public class twoBallAutoRight extends BaseRoutineCode{

  public twoBallAutoRight(Swerve swerve, Shooter shooter, Intake intake, Gut gut) {
    super(swerve, shooter, intake, gut);
    swerve.reset(new Pose2d());

  }

  public void periodic() {
    boolean isShooting = false;
    boolean isMoving = false;

    SmartDashboard.putNumber("timer", timer.get());
    /* auto start */
    // needs to be tested
    
    // shoot two balls
    if (timer.get() >= 0 && timer.get() < SmartDashboard.getNumber("DriveDelay", 7)) {
      isShooting = true;
    }
    if (timer.get() >= 3 && timer.get() < SmartDashboard.getNumber("DriveDelay", 7)) {
      gut.operatorSpinGut(false);
    }
    // swerve out of the tarmac
    if (timer.get() >= SmartDashboard.getNumber("DriveDelay", 7) && timer.get() < SmartDashboard.getNumber("DriveTime", 14)){
        SmartDashboard.putNumber("moving?", timer.get());
        swerve.requestManual(-0.6, 0, 0);
        isMoving = true;
    }
    
    /* auto end */

    // handles shooting
    if (isShooting == true) {
      shooter.requestShoot(Robot.shooterSpeedCal);
      gut.requestShoot();
    } else {
      shooter.requestIdle();
      gut.stopRequestShoot();
    }
    if (!isMoving) {
        swerve.requestManual(0, 0, 0);
    }
  }
}
