package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.OperatorConstants;
import frc.robot.Constants.VisionConstants;
import frc.robot.Constants.VisionLedConstants;
import frc.robot.subsystems.RightVisionSub;
import frc.robot.subsystems.SwerveSub;

public class TrackRightReef extends Command {   
  
  private final RightVisionSub m_RightVisionSubsystem;
  private final SwerveSub m_SwerveSubsystem;

  private final DoubleSupplier xSpeedFunc;
  private final DoubleSupplier ySpeedFunc;
  private final DoubleSupplier zSpeedFunc;

  private final SlewRateLimiter xLimiter;
  private final SlewRateLimiter yLimiter;
  private final SlewRateLimiter zLimiter;

  private double xSpeed;
  private double ySpeed;
  private double zSpeed;

  private boolean fieldOrient;

  private BooleanSupplier ifTrackRightReefFunc;

  private boolean ifTrackRightReef;
    
  public TrackRightReef(RightVisionSub visionSubsystem, SwerveSub swerveSubsystem ,DoubleSupplier xSpeed, DoubleSupplier ySpeed, DoubleSupplier zSpeed, BooleanSupplier ifTrackRightReefFunc) {
    this.m_RightVisionSubsystem = visionSubsystem;
    this.m_SwerveSubsystem = swerveSubsystem;

    this.xSpeedFunc = xSpeed;
    this.ySpeedFunc = ySpeed;
    this.zSpeedFunc = zSpeed;

    this.xLimiter = new SlewRateLimiter(4.6);
    this.yLimiter = new SlewRateLimiter(4.6);
    this.zLimiter = new SlewRateLimiter(4.6);

    this.ifTrackRightReefFunc = ifTrackRightReefFunc;

    addRequirements(m_RightVisionSubsystem, m_SwerveSubsystem);
  }

  @Override
  public void initialize() {
    VisionLedConstants.arriveSetpoint_Base = false;
    VisionConstants.arriveGoal = false;
  }

  @Override
  public void execute() {
    ifTrackRightReef = ifTrackRightReefFunc.getAsBoolean();

    m_RightVisionSubsystem.LED();

    if (m_RightVisionSubsystem.hastarget() && ifTrackRightReef) {
      fieldOrient = false;

      xSpeed = m_RightVisionSubsystem.getXOutput_RightReef();
      ySpeed = -m_RightVisionSubsystem.getYOutput_RightReef();
      zSpeed = m_RightVisionSubsystem.getZOutput_RightReef();

      if (m_RightVisionSubsystem.arriveXposition()){
        xSpeed = 0;
      }

      if (m_RightVisionSubsystem.arriveYposition()){
        ySpeed = 0;
      }

      if (m_RightVisionSubsystem.arriveRotationPosition()){
        zSpeed=0;
      }

      if (m_RightVisionSubsystem.arriveXposition() && m_RightVisionSubsystem.arriveYposition() && m_RightVisionSubsystem.arriveRotationPosition()){
        VisionLedConstants.arriveSetpoint_Base = true;
        VisionConstants.arriveGoal = true;
      }
    } else {
      fieldOrient = true;

      this.xSpeed = xSpeedFunc.getAsDouble() * 0.4;
      this.ySpeed = ySpeedFunc.getAsDouble() * 0.4;
      this.zSpeed = zSpeedFunc.getAsDouble() * 0.2;

      this.xSpeed = MathUtil.applyDeadband(this.xSpeed, OperatorConstants.kJoystickDeadBand);
      this.ySpeed = MathUtil.applyDeadband(this.ySpeed, OperatorConstants.kJoystickDeadBand);
      this.zSpeed = MathUtil.applyDeadband(this.zSpeed, OperatorConstants.kJoystickDeadBand);

      this.xSpeed = xLimiter.calculate(this.xSpeed);
      this.ySpeed = yLimiter.calculate(this.ySpeed);
      this.zSpeed = zLimiter.calculate(this.zSpeed);
    }
    m_SwerveSubsystem.drive(xSpeed, ySpeed, zSpeed, fieldOrient);
  }

  @Override
  public void end(boolean interrupted) {
    VisionLedConstants.arriveSetpoint_Base = false;
    m_RightVisionSubsystem.arriveSetpoint();
    VisionConstants.arriveGoal = false;
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}