package frc.robot.vision;

import java.lang.annotation.Target;

import javax.swing.plaf.synth.SynthEditorPaneUI;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonTrackedTarget;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AprilTagCode {
    allAprilTags targetIDs = new allAprilTags();
    private PhotonCamera camera = new PhotonCamera("gloworm");
    private Transform3d cameraToRobotCenter = new Transform3d(
            new Translation3d(Units.inchesToMeters(13.75), 0, Units.inchesToMeters(35.5)),
            new Rotation3d(0, Units.degreesToRadians(-30), Units.degreesToRadians(0))).inverse();
    // will these functions be more accurate than Transform3d?
    final double CAMERA_HEIGHT_METERS = Units.feetToMeters(2);
    final double TARGET_HEIGHT_METERS = Units.feetToMeters(5);
    final double CAMERA_PITCH_RADIANS = Units.degreesToRadians(0);

    private Pose3d robotGlobalPose;

    private allAprilTags aprilTagPoseList;

    // private Transform3d targetFix = new Transform3d(new Translation3d(0, 0, 0),
    // new Rotation3d(90, 0, 90))

    // targetToCameraFLIP: [-0.02, -0.49, 1.49] [60.43, 2.00, -88.52]
    // globalCameraPose: [-0.49, 1.49, -0.02] [-2.30, -29.55, -177.38]

    private Pose3d fixedTransformBy(Pose3d pose, Transform3d transf) {
        return new Pose3d(
                pose.getTranslation().plus(transf.getTranslation().rotateBy(pose.getRotation())),
                transf.getRotation().plus(pose.getRotation()));
    }

    public void calculate() {
        var result = camera.getLatestResult();
        if (result.hasTargets()) {
            PhotonTrackedTarget target = result.getBestTarget();
            Transform3d targetToCamera = target.getCameraToTarget().inverse();
            if (target.getFiducialId() != -1 && aprilTagPoseList.hasKey(target.getFiducialId())) {
                var tagGlobalPose = aprilTagPoseList.getTagTransform(target.getFiducialId());

                // var tagGlobalPose = new Pose3d(
                // new Translation3d(
                // Units.inchesToMeters(31), 0, Units.inchesToMeters(54.5)),
                // new Rotation3d(Units.degreesToRadians(90), Units.degreesToRadians(0),
                // Units.degreesToRadians(180)));

                var cameraGlobalPose = this.fixedTransformBy(tagGlobalPose, targetToCamera);
                // var cameraGlobalPose = this.fixedTransformBy(tagGlobalPose, targetToCamera);
                robotGlobalPose = this.fixedTransformBy(cameraGlobalPose, cameraToRobotCenter);

                System.out.printf("robotGlobalPose: [%.02f, %.02f, %.02f] [%.02f, %.02f, %.02f]\n",
                        robotGlobalPose.getTranslation().getX(),
                        robotGlobalPose.getTranslation().getY(),
                        robotGlobalPose.getTranslation().getZ(),
                        Units.radiansToDegrees(robotGlobalPose.getRotation().getX()),
                        Units.radiansToDegrees(robotGlobalPose.getRotation().getY()),
                        Units.radiansToDegrees(robotGlobalPose.getRotation().getZ()));

            } else {
                robotGlobalPose = null;
            }
        } else {
            robotGlobalPose = null;
        }
    }

    public Pose3d getPos() {
        return robotGlobalPose;
    }
}