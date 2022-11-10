package frc.robot.autonomus.routines;

import frc.robot.subsystems.swerve.Swerve;
import frc.robot.subsystems.Gut;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class BaseRoutineCode {
    public Swerve swerve;
    public Shooter shooter;
    public Intake intake;
    public Gut gut;
    public Timer timer = new Timer();

    public BaseRoutineCode(Swerve swerve, Shooter shooter, Intake intake, Gut gut) {
        this.swerve = swerve;
        this.shooter = shooter;
        this.intake = intake;
        this.gut = gut;
    }

    public void start() {
        timer.reset();
        timer.start();
    }

    public void periodic() {
    }
}
