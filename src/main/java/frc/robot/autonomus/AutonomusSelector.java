package frc.robot.autonomus;


// import 
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// routines
import frc.robot.autonomus.routines.twoBallAuto;
import frc.robot.autonomus.routines.threeBallAuto;
import frc.robot.autonomus.routines.BaseRoutineCode;
import frc.robot.autonomus.routines.fourBallAuto;

// import Subsystems
import frc.robot.subsystems.swerve.Swerve;
import frc.robot.subsystems.Gut;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;


public class AutonomusSelector {
    private SendableChooser<BaseRoutineCode> autonomusSelector = new SendableChooser<BaseRoutineCode>();


    public AutonomusSelector(Swerve swerve, Shooter shooter, Intake intake, Gut gut) {
        autonomusSelector.addOption(
            "twoBall",
            new twoBallAuto(swerve, shooter, intake, gut)
        );
        autonomusSelector.addOption(
            "threeBall",
            new threeBallAuto(swerve, shooter, intake, gut)
        );
    }

    public BaseRoutineCode get(){
        return autonomusSelector.getSelected();
    }
}
