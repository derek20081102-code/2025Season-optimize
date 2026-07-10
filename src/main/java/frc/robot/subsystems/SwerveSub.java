package frc.robot.subsystems;

import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.SwerveConstants;

public class SwerveSub extends SubsystemBase {
  private final SwerveModule leftFront;
  private final SwerveModule leftBack;
  private final SwerveModule rightFront;
  private final SwerveModule rightBack;

  private final Pigeon2 gyro;
  private final Pigeon2Configuration gyroConfig;

  private final SwerveDriveOdometry odometry;

  private final Field2d field;

  private RobotConfig robotConfig;
  
  private double zSpeed;

  public SwerveSub() {
    leftFront = new SwerveModule(
      SwerveConstants.leftFrontTurning_ID,
      SwerveConstants.leftFrontDrive_ID,
      SwerveConstants.leftFrontAbsolutedEncoder_ID,
      SwerveConstants.leftFrontOffset
    );
    rightFront = new SwerveModule(
      SwerveConstants.rightFrontTurning_ID,
      SwerveConstants.rightFrontDrive_ID,
      SwerveConstants.rightFrontAbsolutedEncoder_ID,
      SwerveConstants.rightFrontOffset
    );
    leftBack = new SwerveModule(
      SwerveConstants.leftBackTurning_ID,
      SwerveConstants.leftBackDrive_ID,
      SwerveConstants.leftBackAbsolutedEncoder_ID,
      SwerveConstants.leftBackOffset
    );
    rightBack = new SwerveModule(
      SwerveConstants.rightBackTurning_ID,
      SwerveConstants.rightBackDrive_ID,
      SwerveConstants.rightBackAbsolutedEncoder_ID,
      SwerveConstants.rightBackOffset
    );

    gyro = new Pigeon2(16);
    gyroConfig = new Pigeon2Configuration();

    gyroConfig.MountPose.MountPosePitch = 0;
    gyroConfig.MountPose.MountPoseRoll = 0;
    gyroConfig.MountPose.MountPoseYaw = 0;

    gyro.getConfigurator().apply(gyroConfig);

    field = new Field2d();

    odometry = new SwerveDriveOdometry(SwerveConstants.swerveKinematics, getRotation(), getModulesPosition(), getRobotPose());

    resetGyro();
                                                       
    try{
      robotConfig = RobotConfig.fromGUISettings();
    } catch (Exception e) {
      // Handle exception as needed
      e.printStackTrace();
    }
    // Configure AutoBuilder last
    AutoBuilder.configure(
            this::getRobotPose, // Robot pose supplier
            this::setPose, // Method to reset odometry (will be called if your auto has a starting pose)
            this::getChassisSpeed, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
            (speeds, feedforwards) -> autoDrive(speeds), // Method that will drive the robot given ROBOT RELATIVE ChassisSpeeds. Also optionally outputs individual module feedforwards
            new PPHolonomicDriveController( // PPHolonomicController is the built in path following controller for holonomic drive trains
                    new PIDConstants(SwerveConstants.pathingMoving_Kp, SwerveConstants.pathingtheta_Ki, SwerveConstants.pathingtheta_Kd), // Translation PID constants
                    new PIDConstants(SwerveConstants.pathingtheta_Kp, SwerveConstants.pathingMoving_Ki, SwerveConstants.pathingMoving_Kd) // Rotation PID constants
            ),
            robotConfig, // The robot configuration
            () -> {
              var alliance = DriverStation.getAlliance();
              if (alliance.isPresent()) {
                return alliance.get() == DriverStation.Alliance.Red;
              }
              return false;
            },
            this // Reference to this subsystem to set requirements
    );

    // // Set up custom logging to add the current path to a field 2d widget
    // PathPlannerLogging.setLogActivePathCallback((poses) -> field.getObject("path").setPoses(poses));
  }

  public ChassisSpeeds getChassisSpeed(){
    return SwerveConstants.swerveKinematics.toChassisSpeeds(getModuleStates());
  }

  public Pose2d getRobotPose(){
    return field.getRobotPose();
  }

  public Rotation2d getRotation(){
    return gyro.getRotation2d();
  }

  public SwerveModulePosition[] getModulesPosition(){
    return new SwerveModulePosition[]{
      leftFront.getPosition(),
      rightFront.getPosition(),
      leftBack.getPosition(),
      rightBack.getPosition()
    };
  }

  public SwerveModuleState[] getModuleStates(){
    return new SwerveModuleState[]{
      leftFront.getState(),
      rightFront.getState(),
      leftBack.getState(),
      rightBack.getState()
    };
  }

  public void setModouleStates(SwerveModuleState[] desiredStates){
      SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, SwerveConstants.maxDriveSpeed_MeterPerSecond);
      leftFront.setState(desiredStates[0]);
      rightFront.setState(desiredStates[1]);
      leftBack.setState(desiredStates[2]);
      rightBack.setState(desiredStates[3]);
  }

  public void setModouleStates_Auto(SwerveModuleState[] desiredStates){
    SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, SwerveConstants.maxDriveSpeed_MeterPerSecond);
    leftFront.setState(desiredStates[0]);
    rightFront.setState(desiredStates[1]);
    leftBack.setState(desiredStates[2]);
    rightBack.setState(desiredStates[3]);
  }

  public void resetGyro(){
    gyro.reset();
  }

  public void setPose(Pose2d poses){
    odometry.resetPosition(getRotation(), getModulesPosition(), poses);
  }

  public void drive(double xSpeed, double ySpeed, double zSpeed, boolean fieldOrient){
    SwerveModuleState[] state;
    xSpeed = xSpeed * SwerveConstants.maxDriveSpeed_MeterPerSecond;
    ySpeed = ySpeed * SwerveConstants.maxDriveSpeed_MeterPerSecond;
    zSpeed = zSpeed * Math.toRadians(SwerveConstants.maxAngularVelocity_Angle);
    this.zSpeed = zSpeed;
    if(fieldOrient) {
      state = SwerveConstants.swerveKinematics.toSwerveModuleStates(ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, zSpeed, getRotation()));
    }else{
      state = SwerveConstants.swerveKinematics.toSwerveModuleStates(new ChassisSpeeds(xSpeed, ySpeed, zSpeed));
    }
    setModouleStates(state);
  } 

  public void autoDrive(ChassisSpeeds speeds){
    ChassisSpeeds targetSpeeds = ChassisSpeeds.discretize(speeds, 0.01);
    SwerveModuleState[] states = SwerveConstants.swerveKinematics.toSwerveModuleStates(targetSpeeds);

    setModouleStates(states);
  }

  @Override
  public void periodic(){
    odometry.update(getRotation(), getModulesPosition());
    field.setRobotPose(odometry.getPoseMeters());

    SmartDashboard.putNumber("Swerve/leftFrontAbsolutePosion", leftFront.getTurningPosition());
    SmartDashboard.putNumber("Swerve/leftBackAbsolutePosion", leftBack.getTurningPosition());
    SmartDashboard.putNumber("Swerve/rightFrontAbsolutePosion", rightFront.getTurningPosition());
    SmartDashboard.putNumber("Swerve/rightBackAbsolutePosion", rightBack.getTurningPosition());

    SmartDashboard.putNumber("Swerve/leftFrontTurningMotorPosition", leftFront.getTurningMotorPosition());
    SmartDashboard.putNumber("Swerve/leftBackTurningMotorPosition", leftBack.getTurningMotorPosition());
    SmartDashboard.putNumber("Swerve/rightFrontTurningMotorPosition", rightFront.getTurningMotorPosition());
    SmartDashboard.putNumber("Swerve/rightBackTurningMotorPosition", rightBack.getTurningMotorPosition());
    
    SmartDashboard.putNumber("Swerve/leftFrontDrivingMotorPosition", leftFront.getDrivePosition());
    SmartDashboard.putNumber("Swerve/leftBackDrivingMotorPosition", leftBack.getDrivePosition());
    SmartDashboard.putNumber("Swerve/rightFrontDrivingMotorPosition", rightFront.getDrivePosition());
    SmartDashboard.putNumber("Swerve/rightBackDrivingMotorPosition", rightBack.getDrivePosition());

    SmartDashboard.putNumber("Swerve/zSpeed", zSpeed);

    SmartDashboard.putNumber("Swerve/gyro", gyro.getYaw().getValueAsDouble());

    SmartDashboard.putNumber("Swerve/leftFrontTurningMotorVelocity", leftFront.getTurningMotorVelocity());
    SmartDashboard.putNumber("Swerve/leftBackTurningMotorVelocity", leftBack.getTurningMotorVelocity());
    SmartDashboard.putNumber("Swerve/rightFrontTurningMotorVelocity", rightFront.getTurningMotorVelocity());
    SmartDashboard.putNumber("Swerve/rightBackTurningMotorVelocity", rightBack.getTurningMotorVelocity());
  }
}