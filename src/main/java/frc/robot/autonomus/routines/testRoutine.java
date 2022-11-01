package frc.robot.autonomus.routines;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.commands.spinGutCommand;
import frc.robot.subsystems.GutPrototype;


public class testRoutine extends SequentialCommandGroup {

  public testRoutine(GutPrototype gut) {
    addRequirements(gut/*swerve, shooter, intake, gut*/);
    addCommands(
      new spinGutCommand(gut, 0.1)
    );
  }
}
