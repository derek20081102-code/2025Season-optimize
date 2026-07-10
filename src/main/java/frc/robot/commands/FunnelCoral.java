package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ElevatorSub;
// import frc.robot.subsystems.LedSub;
import frc.robot.subsystems.ShooterSub;

public class FunnelCoral extends Command {
  private ElevatorSub m_ElevatorSub;
  private ShooterSub m_ShooterSub;
  // private LedSub m_LedSub;

  public FunnelCoral(ElevatorSub m_ElevatorSub, ShooterSub m_ShooterSub/* , LedSub m_LedSub*/) {
    this.m_ElevatorSub = m_ElevatorSub;
    this.m_ShooterSub = m_ShooterSub;
    // this.m_LedSub = m_LedSub;

    addRequirements(m_ElevatorSub, m_ShooterSub/* , m_LedSub*/);
  }

  @Override
  public void initialize() {
    m_ElevatorSub.funnel_Elevator();
  }

  @Override
  public void execute() {
    // if (m_ShooterSub.hasCoral()) {
    //   m_LedSub.solidGreen();
    // } else {
    //   m_LedSub.scrollingRainbow();
      if (m_ElevatorSub.arriveSetpoint()) {
        m_ShooterSub.shooterInCoral_Wheel();
      }
    // }
  }

  @Override
  public void end(boolean interrupted) {
    m_ShooterSub.stop_Wheel();
  }

  @Override
  public boolean isFinished() {
    return m_ShooterSub.hasCoral();
  }
}