package frc.robot.autonomus;

import frc.robot.autonomus.routines.templateRoutine;
import frc.robot.autonomus.routines.threeBallAuto;

// import Subsystems
import frc.robot.subsystems.swerve.Swerve;
import frc.robot.subsystems.Gut;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;


public class AutonomusSelector {
    private threeBallAuto autonomusSelector;


    public AutonomusSelector(Swerve swerve, Shooter shooter, Intake intake, Gut gut) {
        autonomusSelector = new threeBallAuto(swerve, shooter, intake, gut);
    }
    

    public threeBallAuto get(){
        return autonomusSelector;
    }
}
