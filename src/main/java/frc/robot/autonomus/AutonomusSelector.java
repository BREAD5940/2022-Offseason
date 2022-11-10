package frc.robot.autonomus;

// import 
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// routines
import frc.robot.autonomus.routines.twoBallAutoLeft;
import frc.robot.autonomus.routines.twoBallAutoRight;
import frc.robot.autonomus.routines.threeBallAuto;
import frc.robot.autonomus.routines.BaseRoutineCode;

// import Subsystems
import frc.robot.subsystems.swerve.Swerve;
import frc.robot.subsystems.Gut;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

public class AutonomusSelector {
    private SendableChooser<BaseRoutineCode> autonomusSelector = new SendableChooser<BaseRoutineCode>();

    public AutonomusSelector(Swerve swerve, Shooter shooter, Intake intake, Gut gut) {
        autonomusSelector.setDefaultOption(
                "DO_NOTHING",
                new BaseRoutineCode(swerve, shooter, intake, gut));
        autonomusSelector.addOption(
                "two Ball R",
                new twoBallAutoRight(swerve, shooter, intake, gut));
        autonomusSelector.addOption(
                "two Ball L",
                new twoBallAutoLeft(swerve, shooter, intake, gut));
        autonomusSelector.addOption(
                "three Ball",
                new threeBallAuto(swerve, shooter, intake, gut));
        SmartDashboard.putData("Autonomus Selector", autonomusSelector);

    }

    public BaseRoutineCode get() {
        return autonomusSelector.getSelected();
    }
}
