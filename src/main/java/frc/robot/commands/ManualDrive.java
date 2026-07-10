package frc.robot.commands;

import java.util.function.DoubleSupplier;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.SwerveSub;

public class ManualDrive extends Command {
  private final SwerveSub m_SwerveSub;

  private final DoubleSupplier xSpeedFunc;
  private final DoubleSupplier ySpeedFunc;
  private final DoubleSupplier zSpeedFunc;

  private final SlewRateLimiter xLimiter;
  private final SlewRateLimiter yLimiter;
  private final SlewRateLimiter zLimiter;

  private double xSpeed;
  private double ySpeed;
  private double zSpeed;

  public ManualDrive(SwerveSub m_SwerveSub, DoubleSupplier xSpeed, DoubleSupplier ySpeed, DoubleSupplier zSpeed) {
    this.m_SwerveSub = m_SwerveSub;

    this.xSpeedFunc = xSpeed;
    this.ySpeedFunc = ySpeed;
    this.zSpeedFunc = zSpeed;

    this.xLimiter = new SlewRateLimiter(4.6);
    this.yLimiter = new SlewRateLimiter(4.6);
    this.zLimiter = new SlewRateLimiter(4.6);

    addRequirements(m_SwerveSub);
  }

  @Override
  public void initialize() {

  }

  @Override
  public void execute() {
    xSpeed = xSpeedFunc.getAsDouble();
    ySpeed = ySpeedFunc.getAsDouble();
    zSpeed = zSpeedFunc.getAsDouble();

    xSpeed = MathUtil.applyDeadband(this.xSpeed, OperatorConstants.kJoystickDeadBand);
    ySpeed = MathUtil.applyDeadband(this.ySpeed, OperatorConstants.kJoystickDeadBand);
    zSpeed = MathUtil.applyDeadband(this.zSpeed, OperatorConstants.kJoystickDeadBand);

    xSpeed = xLimiter.calculate(this.xSpeed);
    ySpeed = yLimiter.calculate(this.ySpeed);
    zSpeed = zLimiter.calculate(this.zSpeed);

    // if (LEDConstants.isSlowMode) {
    //   xSpeed = xSpeed*0.4;
    //   ySpeed = ySpeed*0.4;
    //   zSpeed = zSpeed*0.4;
    // }else{
      xSpeed = xSpeed*0.4;
      ySpeed = ySpeed*0.4;
      zSpeed = zSpeed*0.4;
    // }
    
    SmartDashboard.putNumber("ManualDrive/Xspeed", xSpeed);
    SmartDashboard.putNumber("ManualDrive/Yspeed", ySpeed);
    SmartDashboard.putNumber("ManualDrive/Zspeed", zSpeed);

    m_SwerveSub.drive(this.xSpeed, this.ySpeed, zSpeed,true);
  }

  @Override
  public void end(boolean interrupted) {
    m_SwerveSub.drive(0, 0, 0, false);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}