package frc.robot.autonomus;

import frc.robot.autonomus.routines.templateRoutine;
import frc.robot.autonomus.routines.twoBallAuto;

// import Subsystems
import frc.robot.subsystems.swerve.Swerve;
import frc.robot.subsystems.Gut;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;


public class AutonomusSelector {
    private twoBallAuto autonomusSelector;


    public AutonomusSelector(Swerve swerve, Shooter shooter, Intake intake, Gut gut) {
        autonomusSelector = new twoBallAuto(swerve, shooter, intake, gut);
    }
    

    public twoBallAuto get(){
        return autonomusSelector;
    }
}
