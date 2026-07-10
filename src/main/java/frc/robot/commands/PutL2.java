package frc.robot.commands;

import java.util.function.BooleanSupplier;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.VisionConstants;
import frc.robot.subsystems.ElevatorSub;
import frc.robot.subsystems.ShooterSub;

public class PutL2 extends Command {
  private ElevatorSub m_ElevatorSub;
  private ShooterSub m_ShooterSub;

  private BooleanSupplier ifFeedFunc;
  private boolean ifFeed;

  public PutL2(ElevatorSub m_ElevatorSub, ShooterSub m_ShooterSub, BooleanSupplier ifFeed) {
    this.m_ElevatorSub = m_ElevatorSub;
    this.m_ShooterSub = m_ShooterSub;

    this.ifFeedFunc = ifFeed;

    addRequirements(m_ElevatorSub, m_ShooterSub);
  }

  @Override
  public void initialize() {
    m_ElevatorSub.L2_Elevator();
    System.out.println("hi");
  }

  @Override
  public void execute() {
    ifFeed = ifFeedFunc.getAsBoolean();

    if ((ifFeed && m_ElevatorSub.arriveSetpoint())||(m_ElevatorSub.arriveSetpoint()&&VisionConstants.arriveGoal)) {
      m_ShooterSub.shooterOutCoral_Wheel();
    }
  }

  @Override
  public void end(boolean interrupted) {
    m_ShooterSub.stop_Wheel();
    m_ElevatorSub.funnel_Elevator();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}