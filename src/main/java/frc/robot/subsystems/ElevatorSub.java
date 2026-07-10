package frc.robot.subsystems;

import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ElevatorConstants;

public class ElevatorSub extends SubsystemBase {
  private TalonFX motor1;
  private TalonFX motor2;
  private TalonFXConfiguration motorConfig;

  private MotionMagicVoltage request;
  private MotionMagicConfigs motionMagicConfig;

  private double elevatorSetpoint;

  private String mode = "start";

  public ElevatorSub(){
    motor1 = new TalonFX(ElevatorConstants.kMotor1ID);
    motor2 = new TalonFX(ElevatorConstants.kMotor2ID);

    motorConfig = new TalonFXConfiguration();

    motionMagicConfig = new MotionMagicConfigs();

    motorConfig.CurrentLimits.SupplyCurrentLimit = ElevatorConstants.kSupplyCurrentLimit;

    motorConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    motorConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    motorConfig.Slot1.GravityType = GravityTypeValue.Elevator_Static;
    motorConfig.Slot1.StaticFeedforwardSign = StaticFeedforwardSignValue.UseVelocitySign;

    motorConfig.Slot1.kP = ElevatorConstants.kElevatorPidController_kP;
    motorConfig.Slot1.kI = ElevatorConstants.kElevatorPidController_kI;
    motorConfig.Slot1.kD = ElevatorConstants.kElevatorPidController_kD;
    motorConfig.Slot1.kG = ElevatorConstants.kElevatorPidController_kG;
    motorConfig.Slot1.kA = ElevatorConstants.kElevatorPidController_kA;
    motorConfig.Slot1.kV = ElevatorConstants.kElevatorPidController_kV;

    motor1.getConfigurator().apply(motorConfig);
    motor2.getConfigurator().apply(motorConfig);

    motionMagicConfig.MotionMagicCruiseVelocity = ElevatorConstants.kMotionMagicCruiseVelocity;
    motionMagicConfig.MotionMagicAcceleration = ElevatorConstants.kMotionMagicAcceleration;
    motionMagicConfig.MotionMagicJerk = ElevatorConstants.kMotionMagicJerk;

    motor1.getConfigurator().apply(motionMagicConfig);
    motor2.getConfigurator().apply(motionMagicConfig);

    resetEncoder();

    motor2.setControl(new Follower(motor1.getDeviceID(), false));

    request = new MotionMagicVoltage(elevatorSetpoint);
    funnel_Elevator();
  }

  public void resetEncoder(){
    motor1.setPosition(0);
    motor2.setPosition(0);
  }

  public void funnel_Elevator(){
    elevatorSetpoint = ElevatorConstants.kFunnelPosition;
    mode = "funnel";
  }

  public void L1_Elevator(){
    elevatorSetpoint = ElevatorConstants.kL1Position;
    mode = "L1";
  }

  public void L2_Elevator(){
    elevatorSetpoint = ElevatorConstants.kL2Position;
    mode = "L2";
  }

  public void L3_Elevator(){
    elevatorSetpoint = ElevatorConstants.kL3Position;
    mode = "L3";
  }

  public void L4_Elevator(){
    elevatorSetpoint = ElevatorConstants.kL4Position;
    mode = "L4";
  }

  public boolean arriveSetpoint(){
    return Math.abs(getMotor1_Position() - elevatorSetpoint) < ElevatorConstants.kElevatorAcceptError
        && Math.abs(getMotor2_Position() - elevatorSetpoint) < ElevatorConstants.kElevatorAcceptError;
  }

  public double getMotor1_Position(){
    return motor1.getPosition().getValueAsDouble();
  }

  public double getMotor2_Position(){
    return motor2.getPosition().getValueAsDouble();
  }

  public double getMotor1_Temp(){
    return motor1.getDeviceTemp().getValueAsDouble();
  }

  public double getMotor2_Temp(){
    return motor2.getDeviceTemp().getValueAsDouble();
  }

  public double getMotor1_Voltage(){
    return motor1.getMotorVoltage().getValueAsDouble();
  }

  public double getMotor2_Voltage(){
    return motor2.getMotorVoltage().getValueAsDouble();
  }

  public String getMode(){
    return mode;
  }

  @Override
  public void periodic(){
    motor1.setControl(request.withPosition(elevatorSetpoint).withSlot(1));

    SmartDashboard.putNumber("Elevator/Setpoint", elevatorSetpoint);

    SmartDashboard.putNumber("Elevator/Motor1/Position", getMotor1_Position());
    SmartDashboard.putNumber("Elevator/Motor2/Position", getMotor2_Position());

    SmartDashboard.putNumber("Elevator/Motor1/Temp", getMotor1_Temp());
    SmartDashboard.putNumber("Elevator/Motor2/Temp", getMotor2_Temp());

    SmartDashboard.putNumber("Elevator/Motor1/Voltage", getMotor1_Voltage());
    SmartDashboard.putNumber("Elevator/Motor2/Voltage", getMotor2_Voltage());

    SmartDashboard.putString("Elevator/Mode", mode);
    SmartDashboard.putString("outputInfo", request.getControlInfo().values().toString());
    
  }
}