package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ModuleConstants;;

public class SwerveModule extends SubsystemBase {
  private final SparkMax turningMotor;
  private final SparkFlex driveMotor;

  private final SparkMaxConfig turningConfig;
  private final SparkFlexConfig driveConfig;

  private final CANcoder absolutedEncoder;
  private final CANcoderConfiguration cancoderConfig;

  private final PIDController turningPidController;
  private final SimpleMotorFeedforward driveFeedForward;

  public SwerveModule(int turningMotor_ID, int driveMotor_ID, int absolutedEncoder_ID, double offset){
    turningMotor = new SparkMax(turningMotor_ID,MotorType.kBrushless);
    driveMotor = new SparkFlex(driveMotor_ID, MotorType.kBrushless);

    turningConfig = new SparkMaxConfig();
    driveConfig = new SparkFlexConfig();

    absolutedEncoder = new CANcoder(absolutedEncoder_ID);
    cancoderConfig = new CANcoderConfiguration();

    turningPidController = new PIDController(ModuleConstants.turningPidController_Kp, ModuleConstants.turningPidController_Ki, ModuleConstants.turningPidController_Kd);
    turningPidController.enableContinuousInput(ModuleConstants.pidRangeMin, ModuleConstants.pidRangeMax);

    driveFeedForward = new SimpleMotorFeedforward(ModuleConstants.driveFeedforward_Ks, ModuleConstants.driveFeedforward_Kv);

    turningConfig.inverted(true);
    driveConfig.inverted(true);

    cancoderConfig.MagnetSensor.SensorDirection = SensorDirectionValue.CounterClockwise_Positive;
    // cancoderConfig.MagnetSensor.AbsoluteSensorDiscontinuityPoint = AbsoluteSensorDiscontinuityPoint.Unsigned_0To1;
    cancoderConfig.MagnetSensor.MagnetOffset = offset;

    turningConfig.idleMode(IdleMode.kBrake);
    driveConfig.idleMode(IdleMode.kBrake);  
    
    turningMotor.configure(turningConfig,ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
    driveMotor.configure(driveConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
    absolutedEncoder.getConfigurator().apply(cancoderConfig);

    resetEncoder();
  }

  public void resetEncoder(){
    driveMotor.getEncoder().setPosition(0);
  }

  public SwerveModuleState getState(){
    return new SwerveModuleState(getDriveVelocity(), Rotation2d.fromDegrees(getTurningAngle()));
  }

  public SwerveModulePosition getPosition(){
    return new SwerveModulePosition(getDrivePosition(), Rotation2d.fromDegrees(getTurningAngle()));
  }

  public double getDriveVelocity(){
    return driveMotor.getEncoder().getVelocity()*ModuleConstants.driveEncoderRot2Meter;
  }

  public double getDrivePosition(){
    return driveMotor.getEncoder().getPosition();
  }

  public double getTurningPosition(){
    return absolutedEncoder.getAbsolutePosition().getValueAsDouble();
  }

  public double getTurningMotorVelocity(){
    return turningMotor.getEncoder().getVelocity();
  }

  public double getTurningMotorPosition(){
    return turningMotor.getEncoder().getPosition();
  }

  public double getTurningAngle(){
    return absolutedEncoder.getAbsolutePosition().getValueAsDouble()*360;
  }

  public void stopMotor(){
    driveMotor.set(0);
    turningMotor.set(0);
  }

  public void setState(SwerveModuleState state){
    // Turn Motor
    state.optimize(getState().angle);
    double turningMotorOutput = turningPidController.calculate(getState().angle.getDegrees(), state.angle.getDegrees());
    turningMotor.set(turningMotorOutput);
    // Drive motor
    double driveMotorOutput = driveFeedForward.calculate(state.speedMetersPerSecond)/12;
    driveMotor.set(driveMotorOutput);
  }

  @Override
  public void periodic(){

  }
}