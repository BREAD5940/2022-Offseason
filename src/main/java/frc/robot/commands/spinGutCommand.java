package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.GutPrototype;

public class spinGutCommand extends CommandBase {
  public spinGutCommand(GutPrototype gut, double percent) {
    gut.spin(percent);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {}

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }
}
