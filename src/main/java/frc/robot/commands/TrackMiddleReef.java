// package frc.robot.commands;

// import java.util.function.BooleanSupplier;
// import java.util.function.DoubleSupplier;
// import edu.wpi.first.math.MathUtil;
// import edu.wpi.first.math.filter.SlewRateLimiter;
// import edu.wpi.first.wpilibj2.command.Command;
// import frc.robot.Constants.OperatorConstants;
// import frc.robot.Constants.VisionLedConstants;
// import frc.robot.subsystems.LeftVisionSub;
// import frc.robot.subsystems.RightVisionSub;
// import frc.robot.subsystems.SwerveSub;

// public class TrackMiddleReef extends Command {    
//   private final SwerveSub m_SwerveSubsystem;
//   private final RightVisionSub m_RightVisionSubsystem;
//   private final LeftVisionSub m_LeftVisionSubsystem;

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

//   private BooleanSupplier ifTrackMiddleReefFunc;

//   private boolean ifTrackMiddleReef;
    
//   public TrackMiddleReef(RightVisionSub rightVisionSubsystem, LeftVisionSub leftVisionSubsystem, SwerveSub swerveSubsystem, DoubleSupplier xSpeed, DoubleSupplier ySpeed, DoubleSupplier zSpeed, BooleanSupplier ifTrackMiddleReefFunc) {
//     this.m_LeftVisionSubsystem = leftVisionSubsystem;
//     this.m_RightVisionSubsystem = rightVisionSubsystem; 
//     this.m_SwerveSubsystem = swerveSubsystem;

//     this.xSpeedFunc = xSpeed;
//     this.ySpeedFunc = ySpeed;
//     this.zSpeedFunc = zSpeed;

//     this.xLimiter = new SlewRateLimiter(4.6);
//     this.yLimiter = new SlewRateLimiter(4.6);
//     this.zLimiter = new SlewRateLimiter(4.6);

//     this.ifTrackMiddleReefFunc = ifTrackMiddleReefFunc;

//     addRequirements(m_LeftVisionSubsystem,m_RightVisionSubsystem, m_SwerveSubsystem);
//   }

//   @Override
//   public void initialize() {
//     VisionLedConstants.arriveSetpoint_Base = false;
//   }

//   @Override
//   public void execute() {
//     ifTrackMiddleReef = ifTrackMiddleReefFunc.getAsBoolean();

//     m_LeftVisionSubsystem.LED();
//     m_RightVisionSubsystem.LED();

//     if (m_LeftVisionSubsystem.hastarget() && ifTrackMiddleReef){
//       fieldOrient = false;

//       xSpeed = m_LeftVisionSubsystem.getXOutput_MiddleReef();
//       ySpeed = -m_LeftVisionSubsystem.getYOutput_MiddleReef();
//       zSpeed = -m_LeftVisionSubsystem.getZOutput_MiddleReef();

//       if (m_LeftVisionSubsystem.arriveXposition()){
//         xSpeed = 0;
//       }

//       if (m_LeftVisionSubsystem.arriveYposition()){
//         ySpeed = 0;
//       }

//       if (m_LeftVisionSubsystem.arriveRotationPosition()){
//         zSpeed = 0;
//       }

//       if (m_LeftVisionSubsystem.arriveXposition() && m_LeftVisionSubsystem.arriveYposition() && m_LeftVisionSubsystem.arriveRotationPosition()){
//         VisionLedConstants.arriveSetpoint_Base = true;
//       }
//     } else {
//       if (m_RightVisionSubsystem.hastarget()){
//         fieldOrient = false;

//         xSpeed = m_RightVisionSubsystem.getXOutput_MiddleReef();
//         ySpeed = -m_RightVisionSubsystem.getYOutput_MiddleReef();
//         zSpeed = -m_RightVisionSubsystem.getZOutput_MiddleReef();

//         if (m_RightVisionSubsystem.arriveXposition()){
//           xSpeed = 0;
//         }

//         if (m_RightVisionSubsystem.arriveYposition()){
//           ySpeed = 0;
//         }

//         if (m_RightVisionSubsystem.arriveRotationPosition()){
//           zSpeed=0;
//         }

//         if (m_RightVisionSubsystem.arriveXposition() && m_RightVisionSubsystem.arriveYposition() && m_RightVisionSubsystem.arriveRotationPosition()){
//           VisionLedConstants.arriveSetpoint_Base = true;
//         }
//       } else {
//         fieldOrient = true;

//         this.xSpeed = -xSpeedFunc.getAsDouble() * 0.4;
//         this.ySpeed = -ySpeedFunc.getAsDouble() * 0.4;
//         this.zSpeed = -zSpeedFunc.getAsDouble() * 0.2;

//         this.xSpeed = MathUtil.applyDeadband(this.xSpeed, OperatorConstants.kJoystickDeadBand);
//         this.ySpeed = MathUtil.applyDeadband(this.ySpeed, OperatorConstants.kJoystickDeadBand);
//         this.zSpeed = MathUtil.applyDeadband(this.zSpeed, OperatorConstants.kJoystickDeadBand);

//         this.xSpeed = xLimiter.calculate(this.xSpeed);
//         this.ySpeed = yLimiter.calculate(this.ySpeed);
//         this.zSpeed = zLimiter.calculate(this.zSpeed);
//       }
//       m_SwerveSubsystem.drive(xSpeed, ySpeed, zSpeed, fieldOrient);
//     }
//   }

//   @Override
//   public void end(boolean interrupted) {
//     VisionLedConstants.arriveSetpoint_Base = false;
//     m_RightVisionSubsystem.arriveSetpoint();
//   }

//   @Override
//   public boolean isFinished() {
//     return false;
//   }
// }