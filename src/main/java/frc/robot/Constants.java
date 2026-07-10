package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.util.Units;


public final class Constants {
  public static class OperatorConstants{
    public static final double kJoystickDeadBand = 0.1;
    public static final int kDriverControllerPort = 0;
  }

  public static double setMaxOutput(double output, double maxOutput){
    return Math.min(maxOutput, Math.max(-maxOutput, output));
  }

  public static class ModuleConstants{
    public static final double pidRangeMin = -180;
    public static final double pidRangeMax = 180;

    public static final double wheelDiameterMeters = Units.inchesToMeters(4);

    public static final double driveGearRatio = 1/5.36;
    public static final double turningGearRatio = 1.0/(150/7);

    public static final double driveVelocityConversionFactor = 
    (1/driveGearRatio)*wheelDiameterMeters*Math.PI;

    public static final double drivePositionConversionFactor = 
    (1/driveGearRatio)*wheelDiameterMeters*Math.PI;

    public static final double driveEncoderRot2MeterPerSec = driveGearRatio*Math.PI*wheelDiameterMeters;
    public static final double driveEncoderRot2Meter = driveGearRatio*Math.PI*wheelDiameterMeters;
    public static final double turningEncoderRot2RadPerSec = turningGearRatio*2*Math.PI;
    public static final double driveEncoderRot2MeterPerMin = driveEncoderRot2MeterPerSec*60;
    public static final double driveEncoderRot2RadPerMin = turningEncoderRot2RadPerSec*60;

    public static final double turningPidController_Kp = 0.008;
    public static final double turningPidController_Ki = 0;
    public static final double turningPidController_Kd = 0.0001;

    public static final double drivePidController_Kp = 0;
    public static final double drivePidController_Ki = 0;
    public static final double drivePidController_Kd = 0;

    public static final double driveFeedforward_Ks = 0.13;
    public static final double driveFeedforward_Kv = 2;
  }

  public class SwerveConstants{
    public static final int leftFrontDrive_ID = 7;//3
    public static final int leftBackDrive_ID = 8;//4
    public static final int rightFrontDrive_ID = 6;//2
    public static final int rightBackDrive_ID = 5;//1

    public static final int leftFrontTurning_ID = 3;//7
    public static final int leftBackTurning_ID = 4;//8
    public static final int rightFrontTurning_ID = 2;//6
    public static final int rightBackTurning_ID = 1;//5

    public static final int leftFrontAbsolutedEncoder_ID = 11;
    public static final int leftBackAbsolutedEncoder_ID = 12;
    public static final int rightFrontAbsolutedEncoder_ID = 10;
    public static final int rightBackAbsolutedEncoder_ID = 9;

    public static final double leftFrontOffset = -0.0400390625;
    public static final double leftBackOffset = 0.451416015625;
    public static final double rightFrontOffset = 0.215576171875;
    public static final double rightBackOffset = -0.459716796875;

    public static final double wheelDiameterMeters = Units.inchesToMeters(4);

    public static final double driveGearRatio = 1/5.95;
    public static final double turningGearRatio = 1.0/21;

    public static final double driveVelocityConversionFactor = 
    (1/driveGearRatio/60)*wheelDiameterMeters*Math.PI;

    public static final double drivePositionConversionFactor = 
    (1/driveGearRatio)*wheelDiameterMeters*Math.PI;

    public static final double driveEncoderRot2MeterPerSec = driveGearRatio*Math.PI*wheelDiameterMeters;
    public static final double driveEncoderRot2Meter = driveGearRatio*Math.PI*wheelDiameterMeters;
    public static final double driveEncoderRot2MeterPerMin = driveEncoderRot2MeterPerSec*60;

    public static final double kModuleDistance = 0.546;

    public static final SwerveDriveKinematics swerveKinematics = new SwerveDriveKinematics(
      new Translation2d(kModuleDistance/2, kModuleDistance/2),
      new Translation2d(kModuleDistance/2, -kModuleDistance/2),
      new Translation2d(-kModuleDistance/2, kModuleDistance/2),
      new Translation2d(-kModuleDistance/2, -kModuleDistance/2)
    );

    public static final double pathingMoving_Kp = 0;
    public static final double pathingMoving_Ki = 0;
    public static final double pathingMoving_Kd = 0;

    public static final double pathingtheta_Kp = 0;
    public static final double pathingtheta_Ki = 0;
    public static final double pathingtheta_Kd = 0;

    public static final double maxDriveSpeed_MeterPerSecond = 5;
    public static final double maxAngularVelocity_Angle = 850;
  }

  public static class ElevatorConstants{
    public static final int kMotor1ID = 13;
    public static final int kMotor2ID = 14;

    public static final double kSupplyCurrentLimit = 60;

    public static final double kElevatorPidController_kP = 1.5;
    public static final double kElevatorPidController_kI = 0;
    public static final double kElevatorPidController_kD = 0;
    public static final double kElevatorPidController_kG = 0.3;
    public static final double kElevatorPidController_kA = 0;
    public static final double kElevatorPidController_kV = 0;

    public static final double kMotionMagicCruiseVelocity = 80;
    public static final double kMotionMagicAcceleration = 90;
    public static final double kMotionMagicJerk = 400;

    public static final double kElevatorAcceptError = 1;

    public static final double kFunnelPosition = 0.3;
    public static final double kL1Position = 0;
    public static final double kL2Position = 6.7;
    public static final double kL3Position = 18;
    public static final double kL4Position = 36.73;
  }

  public static class ShooterConstants{
    public static final int kShooterMotorID = 15;

    public static final int kRedlineID = 0;

    public static final int kSupplyCurrentLimit = 60;

    public static final double kShooterHasCoralDistance = 1000;

    public static final double kShooterInCoralSpeed = -0.62;
    public static final double kShooterOutCoralSpeed = -0.6;
    public static final double kShooterOutCoralL1Speed = -0.3;
  }

  public static class VisionConstants{
    public static final double rightReefXSetpoint = 0.45;
    public static final double rightReefYSetpoint = -0.43;
    public static final double rightReefZSetpoint = -9;

    public static boolean arriveGoal;
    // public static final double rightReefXSetpoint_L4 = 0.6142;
    // public static final double rightReefYSetpoint_L4 = -0.2876;
    // public static final double rightReefZSetpoint_L4 = -18.415;

    // public static final double leftReefXSetpoint = 0.2008;
    // public static final double leftReefYSetpoint = 0.4466;
    // public static final double leftReefZSetpoint = 10.998;
    // public static final double leftReefXSetpoint_L4 = 0.3776;
    // public static final double leftReefYSetpoint_L4 = 0.4025;
    // public static final double leftReefZSetpoint_L4 = 13.273;

    // public static final double right_MiddleReefXSetpoint = 0;
    // public static final double right_MiddleReefYSetpoint = 0;
    // public static final double right_MiddleReefZSetpoint = 0;
    // public static final double left_MiddleReefXSetpoint = 0;
    // public static final double left_MiddleReefYSetpoint = 0;
    // public static final double left_MiddleReefZSetpoint = 0;

  }

  public static class VisionLedConstants{
    public static boolean arriveSetpoint_Base = false;
    public static boolean arriveSetpoint_Arm = false;
    public static boolean arriveSetpoint_Elevator = false;
    public static boolean hasTarget = false;
    public static boolean isAlgaeMode = false;
    public static boolean isSlowMode = false;
    public static boolean isL4Mode = false;
  }
}