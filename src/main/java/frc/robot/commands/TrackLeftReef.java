// package frc.robot.commands;

// import java.util.function.BooleanSupplier;
// import java.util.function.DoubleSupplier;
// import edu.wpi.first.math.MathUtil;
// import edu.wpi.first.math.filter.SlewRateLimiter;
// import edu.wpi.first.wpilibj2.command.Command;
// import frc.robot.Constants.OperatorConstants;
// import frc.robot.Constants.VisionLedConstants;
// import frc.robot.subsystems.LeftVisionSub;
// import frc.robot.subsystems.SwerveSub;

// public class TrackLeftReef extends Command {
//   public double turnOutput;
//   public double moveOutput;
//   public double frontOutput;
    
//   private final LeftVisionSub m_LeftVisionSubsystem;
//   private final SwerveSub m_SwerveSubsystem;

//   private final DoubleSupplier xSpeedFunc;
//   private final DoubleSupplier ySpeedFunc;
//   private final DoubleSupplier zSpeedFunc;

//   private final SlewRateLimiter xLimiter;
//   private final SlewRateLimiter yLimiter;
//   private final SlewRateLimiter zLimiter;

//   private double xSpeed;
//   private double ySpeed;
//   private double zSpeed;

//   private boolean fieldOrient;

//   private BooleanSupplier ifTrackLeftReefFunc;

//   private boolean ifTrackLeftReef;
    
//   public TrackLeftReef(LeftVisionSub visionSubsystem, SwerveSub swerveSubsystem, DoubleSupplier xSpeed, DoubleSupplier ySpeed, DoubleSupplier zSpeed, BooleanSupplier ifTrackLeftReefFunc) {
//     this.m_LeftVisionSubsystem = visionSubsystem;
//     this.m_SwerveSubsystem = swerveSubsystem;

//     this.xSpeedFunc = xSpeed;
//     this.ySpeedFunc = ySpeed;
//     this.zSpeedFunc = zSpeed;

//     this.xLimiter = new SlewRateLimiter(4.6);
//     this.yLimiter = new SlewRateLimiter(4.6);
//     this.zLimiter = new SlewRateLimiter(4.6);

//     this.ifTrackLeftReefFunc = ifTrackLeftReefFunc;

//     addRequirements(m_LeftVisionSubsystem, m_SwerveSubsystem);
//   }

//   @Override
//   public void initialize() {
//     VisionLedConstants.arriveSetpoint_Base = false;
//   }

//   @Override
//   public void execute() {
//     ifTrackLeftReef = ifTrackLeftReefFunc.getAsBoolean();

//     m_LeftVisionSubsystem.LED();

//     if (m_LeftVisionSubsystem.hastarget() && ifTrackLeftReef){
//       fieldOrient = false;

//       xSpeed = m_LeftVisionSubsystem.getXOutput_Leftreef();
//       ySpeed = -m_LeftVisionSubsystem.getYOutput_Leftreef();
//       zSpeed = -m_LeftVisionSubsystem.getZOutput_Leftreef();

//       if (m_LeftVisionSubsystem.arriveXposition()){
//         xSpeed = 0;
//       }

//       if (m_LeftVisionSubsystem.arriveYposition()){
//         ySpeed = 0;
//       }

//       if (m_LeftVisionSubsystem.arriveRotationPosition()){
//         zSpeed=0;
//       }

//       if (m_LeftVisionSubsystem.arriveXposition() && m_LeftVisionSubsystem.arriveYposition() && m_LeftVisionSubsystem.arriveRotationPosition()){
//         VisionLedConstants.arriveSetpoint_Base = true;
//       }
//     } else {
//       fieldOrient = true;

//       this.xSpeed = -xSpeedFunc.getAsDouble();
//       this.ySpeed = -ySpeedFunc.getAsDouble();
//       this.zSpeed = -zSpeedFunc.getAsDouble();

//       this.xSpeed = MathUtil.applyDeadband(this.xSpeed, OperatorConstants.kJoystickDeadBand);
//       this.ySpeed = MathUtil.applyDeadband(this.ySpeed, OperatorConstants.kJoystickDeadBand);
//       this.zSpeed = MathUtil.applyDeadband(this.zSpeed, OperatorConstants.kJoystickDeadBand);

//       this.xSpeed = xLimiter.calculate(this.xSpeed);
//       this.ySpeed = yLimiter.calculate(this.ySpeed);
//       this.zSpeed = zLimiter.calculate(this.zSpeed);
//     }
//     m_SwerveSubsystem.drive(xSpeed, ySpeed, zSpeed, fieldOrient);
//   }

//   @Override
//   public void end(boolean interrupted) {
//     m_LeftVisionSubsystem.arriveSetpoint();
//     VisionLedConstants.arriveSetpoint_Base = false;
//   }

//   @Override
//   public boolean isFinished() {
//     return false;
//   }
// }