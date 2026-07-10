package frc.robot;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.FunnelCoral;
import frc.robot.commands.ManualDrive;
import frc.robot.commands.PutL1;
import frc.robot.commands.PutL3;
import frc.robot.commands.PutL4;
// import frc.robot.commands.TrackLeftReef;
// import frc.robot.commands.TrackMiddleReef;
import frc.robot.commands.TrackRightReef;
import frc.robot.commands.PutL2;
import frc.robot.subsystems.ElevatorSub;
// import frc.robot.subsystems.LeftVisionSub;
import frc.robot.subsystems.RightVisionSub;
// import frc.robot.subsystems.LedSub;
import frc.robot.subsystems.ShooterSub;
import frc.robot.subsystems.SwerveSub;

public class RobotContainer {
  private final CommandXboxController m_DriverController = new CommandXboxController(Constants.OperatorConstants.kDriverControllerPort);

  private final SwerveSub m_SwerveSub = new SwerveSub();
  private final ElevatorSub m_ElevatorSub = new ElevatorSub();
  private final ShooterSub m_ShooterSub = new ShooterSub();
  private final RightVisionSub m_RightVisionSub = new RightVisionSub();
  // private final LeftVisionSub m_LeftVisionSub = new LeftVisionSub();
  // private final LedSub m_LedSub = new LedSub();

  private final SendableChooser<Command> autoChooser;

  public RobotContainer() {
    // NamedCommands.registerCommand(null, getAutonomousCommand());
    autoChooser = AutoBuilder.buildAutoChooser();
    SmartDashboard.putData("AutoMode", autoChooser);
    configureBindings();
  }

  private void configureBindings() {
    DoubleSupplier xSpeedFunc = ()-> m_DriverController.getRawAxis(1);
    DoubleSupplier ySpeedFunc = ()-> m_DriverController.getRawAxis(0);
    DoubleSupplier zSpeedFunc = ()-> m_DriverController.getRawAxis(4);

    m_SwerveSub.setDefaultCommand(new ManualDrive(m_SwerveSub, xSpeedFunc, ySpeedFunc, zSpeedFunc));
  
    m_DriverController.x().whileTrue(Commands.runOnce(() -> m_SwerveSub.resetGyro()));

    m_DriverController.axisGreaterThan(2, 0.5).whileTrue(new FunnelCoral(m_ElevatorSub, m_ShooterSub/* , m_LedSub*/));

    BooleanSupplier ifFeed = () -> m_DriverController.axisGreaterThan(3, 0.5).getAsBoolean();

    m_DriverController.start().onTrue(new PutL1(m_ElevatorSub, m_ShooterSub, ifFeed));
    m_DriverController.a().onTrue(new PutL2(m_ElevatorSub, m_ShooterSub, ifFeed));
    m_DriverController.b().onTrue(new PutL3(m_ElevatorSub, m_ShooterSub, ifFeed));
    // m_DriverController.y().onTrue(new PutL4(m_ElevatorSub, m_ShooterSub, ifFeed));

    BooleanSupplier ifTrackRightReef = () -> m_DriverController.rightBumper().getAsBoolean();
    BooleanSupplier ifTrackLeftReef = () -> m_DriverController.leftBumper().getAsBoolean();
    BooleanSupplier ifTrackMiddleReef = () -> m_DriverController.leftBumper().getAsBoolean() && m_DriverController.rightBumper().getAsBoolean();

    // new Trigger(ifTrackLeftReef).whileTrue(new TrackLeftReef(m_LeftVisionSub, m_SwerveSub, xSpeedFunc, ySpeedFunc, zSpeedFunc, ifTrackLeftReef));
    new Trigger(ifTrackRightReef).whileTrue(new TrackRightReef(m_RightVisionSub, m_SwerveSub, xSpeedFunc, ySpeedFunc, zSpeedFunc, ifTrackRightReef));
    // new Trigger(ifTrackMiddleReef).whileTrue(new TrackMiddleReef(m_RightVisionSub, m_LeftVisionSub, m_SwerveSub, xSpeedFunc, ySpeedFunc, zSpeedFunc, ifTrackMiddleReef));
  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }
}