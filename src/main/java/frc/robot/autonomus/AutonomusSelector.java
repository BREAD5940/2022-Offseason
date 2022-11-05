package frc.robot.autonomus;


// import 
import frc.robot.autonomus.routines.TemplateRoutine;
import frc.robot.autonomus.routines.twoBallAuto;
import frc.robot.autonomus.routines.threeBallAuto;
import frc.robot.autonomus.routines.fourBallAuto;
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
