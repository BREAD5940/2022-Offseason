package frc.robot.autonomus;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.autonomus.routines.testRoutine;
import frc.robot.subsystems.GutPrototype;
 


public class AutonomusSelector {
    private SendableChooser<SequentialCommandGroup> autonomusSelector = new SendableChooser<SequentialCommandGroup>();


    public AutonomusSelector(GutPrototype gut/*Swerve swerve, Shooter shooter, Intake intake, Gut gut*/) {
        autonomusSelector.setDefaultOption(
            "DO_NOTHING", 
            new SequentialCommandGroup()
        );
        autonomusSelector.addOption(
            "Spin_Gut_Test", 
            new testRoutine(gut)
        );
    }
    
    public SequentialCommandGroup get() {
        return autonomusSelector.getSelected();
    }
}
