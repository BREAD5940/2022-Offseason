package frc.robot.autonomus;

import frc.robot.autonomus.routines.testRoutine;

// import Subsystems
import frc.robot.subsystems.swerve.Swerve;
import frc.robot.subsystems.Gut;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;


public class AutonomusSelector {
    private testRoutine autonomusSelector;


    public AutonomusSelector(Swerve swerve, Shooter shooter, Intake intake, Gut gut) {
        autonomusSelector = new testRoutine(swerve, shooter, intake, gut);
    }
    
    public testRoutine get() {
        return autonomusSelector;
    }
}
