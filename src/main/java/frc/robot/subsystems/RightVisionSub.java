package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.networktables.DoubleArraySubscriber;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.LimelightHelpers;
import frc.robot.Constants.VisionConstants;

public class RightVisionSub extends SubsystemBase {
  private PIDController xPidController;
  private PIDController yPidController;
  private PIDController zPidController;

  private final NetworkTable visionTable = NetworkTableInstance.getDefault().getTable("limelight-right");
  private final DoubleArraySubscriber doubleArray = visionTable.getDoubleArrayTopic("botpose_targetspace").subscribe(new double[]{0, 0, 0, 0, 0, 0});

  public RightVisionSub(){
    xPidController = new PIDController(0.2, 0, 0.002);//0.17 0 0.001
    yPidController = new PIDController(0.19, 0, 0.005);//0.19 0 0.005
    zPidController = new PIDController(0.01, 0, 0);//0.01 0 0 

    arriveSetpoint();
  }

  public double getXOutput_RightReef(){
        return Constants.setMaxOutput(xPidController.calculate(getTZ(), VisionConstants.rightReefXSetpoint), 0.2);
  }

  public double getYOutput_RightReef(){
        return Constants.setMaxOutput(yPidController.calculate(getTX(), VisionConstants.rightReefYSetpoint), 0.2) ;
  }

  public double getZOutput_RightReef(){
        return Constants.setMaxOutput(zPidController.calculate(getRY(), VisionConstants.rightReefZSetpoint), 0.1);
  }

  // public double getXOutput_MiddleReef(){
  //   return Constants.setMaxOutput(xPidController.calculate(getTZ(), VisionConstants.right_MiddleReefXSetpoint), 0.2);
  // }

  // public double getYOutput_MiddleReef(){
  //   return Constants.setMaxOutput(yPidController.calculate(getTX(), VisionConstants.right_MiddleReefYSetpoint), 0.2) ;
  // }

  // public double getZOutput_MiddleReef(){
  //   return Constants.setMaxOutput(zPidController.calculate(getRY(), VisionConstants.right_MiddleReefZSetpoint), 0.1);
  // }

  public double getTX(){
    return LimelightHelpers.getTargetPose3d_RobotSpace("limelight-right").getX();
  }

  public double getTZ(){
    return LimelightHelpers.getTargetPose3d_RobotSpace("limelight-right").getZ();
  }

  public double getRY(){
    return doubleArray.get()[4];
  }

  public boolean hastarget(){
    return LimelightHelpers.getTV("limelight-right");
  }

  public boolean arriveXposition(){
    return Math.abs(xPidController.getError()) <= 0.04;
  }

  public boolean arriveYposition(){
    return Math.abs(yPidController.getError()) <= 0.035;
  }

  public boolean arriveRotationPosition(){
    return Math.abs(zPidController.getError()) <= 1;
  }
  

  public void tracking_hasTarget(){
    LimelightHelpers.setLEDMode_ForceOn("limelight-right");
  }

  public void tracking_noTarget(){
    LimelightHelpers.setLEDMode_ForceBlink("limelight-right");
  }

  public void arriveSetpoint(){
    LimelightHelpers.setLEDMode_ForceOff("limelight-right");
  }

  public void LED(){
    if(arriveXposition() && arriveYposition() && arriveRotationPosition()){
      arriveSetpoint();
    }else{
      if(hastarget()){
        tracking_hasTarget();
      }else{
        tracking_noTarget();
      }
    }
  }

  @Override
  public void periodic(){
    SmartDashboard.putNumber("RightVision/Tx", getTX());
    SmartDashboard.putNumber("RightVision/Tz", getTZ());
    SmartDashboard.putNumber("RightVision/RY", getRY());

    SmartDashboard.putBoolean("RightVision/hastarget", hastarget());

    SmartDashboard.putNumber("xoutput", getXOutput_RightReef());
    SmartDashboard.putNumber("youtput", getYOutput_RightReef());
    SmartDashboard.putNumber("RYoutput", getZOutput_RightReef());
  }
}
