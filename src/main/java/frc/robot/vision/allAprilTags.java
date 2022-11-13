package frc.robot.vision;

import java.util.HashMap;
import java.util.Map;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;

public class allAprilTags {
    private Map<Integer, Pose3d> poseMap = new HashMap<>();
    public allAprilTags(){
        poseMap.put(0, new Pose3d(new Translation3d(-0.004, 7.579, 0.886), new Rotation3d(0, 0, 0))); //Blue Hangar Panel
        poseMap.put(1, new Pose3d(new Translation3d(3.233, 5.487, 1.725), new Rotation3d(0, 0, 0))); //Blue Hangar Truss - Hub
        poseMap.put(2, new Pose3d(new Translation3d(3.068, 5.331, 1.376), new Rotation3d(0, Units.degreesToRadians(-90), 0))); //Blue Hangar Truss - Side
        poseMap.put(3, new Pose3d(new Translation3d(0.004, 5.059, 0.806), new Rotation3d(0, 0, 0))); //Blue Station 2 Wall
        poseMap.put(4, new Pose3d(new Translation3d(0.004, 3.512, 0.806), new Rotation3d(0, 0, 0))); //Blue Station 3 Wall
        poseMap.put(5, new Pose3d(new Translation3d(0.121, 1.718, 0.891), new Rotation3d(0, Units.degreesToRadians(46.25), 0))); //Blue Terminal Near Station
        poseMap.put(6, new Pose3d(new Translation3d(0.873, 0.941, 0.891), new Rotation3d(0, Units.degreesToRadians(46.25), 0))); //Blue Mid Terminal
        poseMap.put(7, new Pose3d(new Translation3d(1.615, 0.157, 0.891), new Rotation3d(0, Units.degreesToRadians(46.25), 0))); //Blue End Terminal
        poseMap.put(10, new Pose3d(new Translation3d(16.463, 0.651, 0.886), new Rotation3d(0,Units.degreesToRadians(180), 0))); //Red Hangar Panel
        poseMap.put(11, new Pose3d(new Translation3d(13.235, 2.743, 1.725), new Rotation3d(0, Units.degreesToRadians(180), 0))); //Red Hangar Truss - Hub
        poseMap.put(12, new Pose3d(new Translation3d(13.391, 2.90, 1.376), new Rotation3d(0, Units.degreesToRadians(90), 0))); //Red Hangar Truss - Side
        poseMap.put(13, new Pose3d(new Translation3d(16.455, 3.176, 0.806), new Rotation3d(0, Units.degreesToRadians(180), 0))); //Red Station 2 Wall
        poseMap.put(14, new Pose3d(new Translation3d(16.455, 4.717, 0.806), new Rotation3d(0, Units.degreesToRadians(180), 0))); //Red Station 3 Wall
        poseMap.put(15, new Pose3d(new Translation3d(16.335, 6.515, 0.894), new Rotation3d(0, Units.degreesToRadians(223.8), 0))); //Red Terminal Near Station
        poseMap.put(16, new Pose3d(new Translation3d(15.59, 7.293, 0.891), new Rotation3d(0, Units.degreesToRadians(223.8), 0))); //Red Mid Terminal
        poseMap.put(17, new Pose3d(new Translation3d(14.847, 8.069, 0.891), new Rotation3d(0, Units.degreesToRadians(223.8), 0))); //Red End Terminal
        poseMap.put(40, new Pose3d(new Translation3d(7.874, 4.913, 0.703), new Rotation3d(0, Units.degreesToRadians(114), 0))); //Lower Hub Far Exit
        poseMap.put(41, new Pose3d(new Translation3d(7.431, 3.759, 0.703), new Rotation3d(0, Units.degreesToRadians(204), 0))); //Lower Hub Blue Exit
        poseMap.put(42, new Pose3d(new Translation3d(8.585, 3.316, 0.703), new Rotation3d(0, Units.degreesToRadians(-66), 0))); //Lower Hub Near Exit
        poseMap.put(43, new Pose3d(new Translation3d(9.028, 4.47, 0.703), new Rotation3d(0, Units.degreesToRadians(-24), 0))); //Lower Hub Red Exit
        poseMap.put(50, new Pose3d(new Translation3d(7.679, 4.326, 2.418), new Rotation3d(0, Units.degreesToRadians(159), Units.degreesToRadians(26.75)))); //Upper Hub Far-Blue
        poseMap.put(51, new Pose3d(new Translation3d(8.018, 3.564, 2.418), new Rotation3d(0, Units.degreesToRadians(339), Units.degreesToRadians(26.75)))); //Upper Hub Blue-Near
        poseMap.put(52, new Pose3d(new Translation3d(8.78, 3.903, 2.418), new Rotation3d(0, Units.degreesToRadians(249), Units.degreesToRadians(26.75)))); //Upper Hub Near-Red
        poseMap.put(53, new Pose3d(new Translation3d(8.441, 4.665, 2.418), new Rotation3d(0, Units.degreesToRadians(69), Units.degreesToRadians(26.75)))); //Upper Hub Red-Far
    }
    
    public Pose3d getTagTransform(double aprilTagID) {
        return poseMap.get(aprilTagID);
    }
}
