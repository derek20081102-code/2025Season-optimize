// package frc.robot.subsystems;

// import edu.wpi.first.math.controller.PIDController;
// import edu.wpi.first.networktables.DoubleArraySubscriber;
// import edu.wpi.first.networktables.NetworkTable;
// import edu.wpi.first.networktables.NetworkTableInstance;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import edu.wpi.first.wpilibj2.command.SubsystemBase;
// import frc.robot.Constants;
// import frc.robot.Constants.VisionLedConstants;
// import frc.robot.Constants.VisionConstants;
// import frc.robot.LimelightHelpers;

// public class LeftVisionSub extends SubsystemBase {
//   private PIDController xPID; 
//   private PIDController yPID;
//   private PIDController zPID;

//   private final NetworkTable table;

//   private final DoubleArraySubscriber botPose;

//   public LeftVisionSub(){
//     xPID = new PIDController(0.17, 0, 0.001);
//     yPID = new PIDController(0.19, 0, 0.005);
//     zPID = new PIDController(0.01, 0, 0);

//     table = NetworkTableInstance.getDefault().getTable("limelight-left");
//     botPose = table.getDoubleArrayTopic("botpose_targetspace").subscribe(new double[]{0, 0, 0, 0, 0, 0});

//     arriveSetpoint();
//   }

//   public double getXOutput_Leftreef(){
//     if (VisionLedConstants.isL4Mode) {
//       return Constants.setMaxOutput(xPID.calculate(getZ(), VisionConstants.leftReefXSetpoint_L4), 0.3);
//     }else{
//       return Constants.setMaxOutput(xPID.calculate(getZ(), VisionConstants.leftReefXSetpoint), 0.3);
//     }
//   }

//   public double getYOutput_Leftreef(){
//     if (VisionLedConstants.isL4Mode) {
//       return Constants.setMaxOutput(yPID.calculate(getX(), VisionConstants.leftReefYSetpoint_L4), 0.3) ;
//     }else{
//       return Constants.setMaxOutput(yPID.calculate(getX(), VisionConstants.leftReefYSetpoint), 0.3) ;
//     }
//   }

//   public double getZOutput_Leftreef(){
//     if (VisionLedConstants.isL4Mode) {
//       return Constants.setMaxOutput(zPID.calculate(getRY(), VisionConstants.leftReefZSetpoint_L4), 0.1);
//     }else{
//       return Constants.setMaxOutput(zPID.calculate(getRY(), VisionConstants.leftReefZSetpoint), 0.1);
//     }
//   }

//   public double getXOutput_MiddleReef(){
//     return Constants.setMaxOutput(xPID.calculate(getZ(), VisionConstants.left_MiddleReefXSetpoint), 0.2);
//   }

//   public double getYOutput_MiddleReef(){
//     return Constants.setMaxOutput(yPID.calculate(getX(), VisionConstants.left_MiddleReefYSetpoint), 0.2) ;
//   }

//   public double getZOutput_MiddleReef(){
//     return Constants.setMaxOutput(zPID.calculate(getRY(), VisionConstants.left_MiddleReefZSetpoint), 0.1);
//   }

//   public double getX(){
//     return LimelightHelpers.getTargetPose3d_RobotSpace("limelight-left").getX();
//   }

//   public double getZ(){
//     return LimelightHelpers.getTargetPose3d_RobotSpace("limelight-left").getZ();
//   }

//   public double getRY(){
//     return botPose.get()[4];
//   }

//   public boolean hastarget(){
//     return LimelightHelpers.getTV("limelight-left");
//   }

//   public boolean arriveXposition(){
//     return Math.abs(xPID.getError())<0.01;
//   }

//   public boolean arriveYposition(){
//     return Math.abs(yPID.getError())<0.01;
//   }

//   public boolean arriveRotationPosition(){
//     return Math.abs(zPID.getError())<1;
//   }

//   public void tracking_hasTarget(){
//     LimelightHelpers.setLEDMode_ForceOn("limelight-left");
//   }

//   public void tracking_noTarget(){
//     LimelightHelpers.setLEDMode_ForceBlink("limelight-left");
//   }

//   public void arriveSetpoint(){
//     LimelightHelpers.setLEDMode_ForceOff("limelight-left");
//   }

//   public void LED(){
//     if(arriveXposition() & arriveYposition() && arriveRotationPosition()){
//       arriveSetpoint();
//     }else{
//       if(hastarget()){
//         tracking_hasTarget();
//       }else{
//         tracking_noTarget();
//       }
//     }
//   }

//   @Override
//   public void periodic(){
//     SmartDashboard.putNumber("LeftVision/Tx", getX());
//     SmartDashboard.putNumber("LeftVision/Tz", getZ());
//     SmartDashboard.putNumber("LeftVision/RY", getRY());

//     SmartDashboard.putBoolean("LeftVision/hastarget", hastarget());

//     SmartDashboard.putNumber("xoutput", getXOutput_Leftreef());
//     SmartDashboard.putNumber("youtput", getYOutput_Leftreef());
//     SmartDashboard.putNumber("RYoutput", getZOutput_Leftreef());
//   }
// }