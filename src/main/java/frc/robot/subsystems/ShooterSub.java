package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;

public class ShooterSub extends SubsystemBase {
  private SparkMax shooterMotor;
  private SparkMaxConfig shooterConfig;

  private AnalogInput redline;

  public ShooterSub(){
    shooterMotor = new SparkMax(ShooterConstants.kShooterMotorID, MotorType.kBrushless);
    shooterConfig = new SparkMaxConfig();

    redline = new AnalogInput(ShooterConstants.kRedlineID);

    shooterConfig.idleMode(IdleMode.kCoast);
    shooterConfig.inverted(false);
    shooterConfig.smartCurrentLimit(ShooterConstants.kSupplyCurrentLimit);
    
    shooterMotor.configure(shooterConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
  }

  public void shooterInCoral_Wheel(){
    shooterMotor.set(ShooterConstants.kShooterInCoralSpeed);
  }

  public void shooterOutCoral_Wheel(){
    shooterMotor.set(ShooterConstants.kShooterOutCoralSpeed);
  }

  public void shooterOutCoralL1_Wheel(){
    shooterMotor.set(ShooterConstants.kShooterOutCoralL1Speed);
  }

  public void stop_Wheel(){
    shooterMotor.stopMotor();
  }

  public boolean hasCoral(){
    return getRedline_Distance() >= ShooterConstants.kShooterHasCoralDistance;
  }

  public double getRedline_Distance(){
    return redline.getValue();
  }

  public double getMotor_Temp(){
    return shooterMotor.getMotorTemperature();
  }

  public double getMotor_Voltage(){
    return shooterMotor.getBusVoltage();
  }

  public double getMotor_Current(){
    return shooterMotor.getOutputCurrent();
  }

  @Override
  public void periodic(){
    SmartDashboard.putNumber("Shooter/Sensor/Distance", getRedline_Distance());

    SmartDashboard.putNumber("Shooter/Motor/Temp", getMotor_Temp());
    SmartDashboard.putNumber("Shooter/Motor/Voltage", getMotor_Voltage());
    SmartDashboard.putNumber("Shooter/Motor/Current", getMotor_Current());

    SmartDashboard.putBoolean("Shooter/hasCoral", hasCoral());
  }
}