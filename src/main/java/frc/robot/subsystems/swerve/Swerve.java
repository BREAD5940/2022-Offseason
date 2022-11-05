package frc.robot.subsystems.swerve;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;


import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import frc.robot.Robot;
import edu.wpi.first.wpilibj.XboxController.Button;
import frc.robot.Constants.Drive.*;
import com.fasterxml.jackson.core.sym.NameN;
import com.kauailabs.navx.frc.AHRS;

public class Swerve {
    
    // Gyro
    private final AHRS gyro = new AHRS(SPI.Port.kMXP);
    public  Translation2d FL_LOCATION = new Translation2d(Units.inchesToMeters(20.5), Units.inchesToMeters(20.5));
    private Translation2d FR_LOCATION = new Translation2d(Units.inchesToMeters(20.5), -Units.inchesToMeters(20.5));
    private Translation2d BL_LOCATION = new Translation2d(-Units.inchesToMeters(20.5), Units.inchesToMeters(20.5));
    private Translation2d BR_LOCATION = new Translation2d(-Units.inchesToMeters(20.5), -Units.inchesToMeters(20.5));

    public final MK2SwerveModule fl = new MK2SwerveModule(13, 12, 1, Units.degreesToRadians(300.2), false, false);
    public final MK2SwerveModule fr = new MK2SwerveModule(11, 10, 3, Units.degreesToRadians(111.3), false, true);
    public final MK2SwerveModule bl = new MK2SwerveModule(15, 14, 0, Units.degreesToRadians(10), false, false);
    public final MK2SwerveModule br = new MK2SwerveModule(17, 16, 2, Units.degreesToRadians(290.2), false, true);
    // Kinematics & Odometry
    private final SwerveDriveKinematics kinematics = new SwerveDriveKinematics(FL_LOCATION, FR_LOCATION, BL_LOCATION, BR_LOCATION);
    private final SwerveDriveOdometry matchOdometry = new SwerveDriveOdometry(kinematics, gyro.getRotation2d());

    // Field2d
    private Pose2d pose = matchOdometry.getPoseMeters();
    public final Field2d field = new Field2d();

    // Swerve state enum
    public enum SwerveState {
        TELEOP_MODE, 
        Manual_MODE,
    }

    // State variables
    private boolean atVisionHeadingSetpoint = false;
    public static double ROBOT_MAX_SPEED = 4.29768;
    
    // State variables
    private SwerveState systemState = SwerveState.Manual_MODE;
    private boolean requestTeleop = false;
    private boolean requestManual = false;
    private double lastTransitioned = 0.0;


    public Swerve() {
        // Might want to do some config in the constructor
        field.setRobotPose(pose);
    }

    public void setSpeeds(double xSpeed, double ySpeed, double rot) {
        SwerveModuleState[] states = kinematics.toSwerveModuleStates(ChassisSpeeds.fromFieldRelativeSpeeds(
            xSpeed, ySpeed, rot, pose.getRotation())
        );

        SwerveDriveKinematics.desaturateWheelSpeeds(states, ROBOT_MAX_SPEED);
        fl.setState(states[0]);
        fr.setState(states[1]);
        bl.setState(states[2]);
        br.setState(states[3]);
    }

    /* Helper methods */
    // Returns the raw gyro angle; is negated to be counterclockwise positive
    public double getRawGyro() {
        return -gyro.getAngle();
    }

    // Returns the rotation2d representing the gyro angle 
    public Rotation2d getGyro() {
        return gyro.getRotation2d();
    }

    // Resets match odometry
    public void reset(Pose2d newPose) {
        matchOdometry.resetPosition(newPose, gyro.getRotation2d());
        pose = matchOdometry.getPoseMeters();
    }

    // Updates match odometry
    public void updateOdometry() {
        pose = matchOdometry.update(
            gyro.getRotation2d(),
            fl.getState(), 
            fr.getState(), 
            bl.getState(),
            br.getState()
        );
        field.setRobotPose(pose);
        // SmartDashboard.putData(field);
    }

    // Returns the match pose
    public Pose2d getPose() {
        return pose;
    }

    // Sets whether or not the drivetrain is at its vision reference
    public void setAtVisionHeadingSetpoint(boolean set) {
        atVisionHeadingSetpoint = set;
    }

    // Returns whether or not the drivetrain is at its vision reference
    public boolean getAtVisionHeadingSetpoint() {
        return atVisionHeadingSetpoint;
    }
       // Returns the ROBOT RELATIVE speed of the drivetrain
       public Translation2d getVelocity() {
        ChassisSpeeds speeds = kinematics.toChassisSpeeds(
            fl.getState(),
            fr.getState(),
            bl.getState(),
            br.getState()
        );
        return new Translation2d(
            speeds.vxMetersPerSecond,
            speeds.vyMetersPerSecond
        );
    }

    // Returns the Chassis Speeds object, representing the ROBOT RELATIVE velocity of the drivetrain 
    public ChassisSpeeds getChassisSpeeds() {
        return kinematics.toChassisSpeeds(
            fl.getState(), 
            fr.getState(),
            bl.getState(),
            br.getState()
        );
    }

    public void requestTeleop() {
        requestTeleop = true;
        requestManual = false;
    }

    public void requestManual(double xSpeed, double ySpeed, double rot) {
        requestManual = true;
        requestTeleop = false;
        setSpeeds(xSpeed, ySpeed, rot);
    }

    // periodic method should be called every 20ms    
    public void periodic() {
        SwerveState nextSystemState = systemState;

        if (systemState == SwerveState.Manual_MODE) {

            // Outputs
            if (requestTeleop) {
                nextSystemState = SwerveState.TELEOP_MODE;
            }
        } else if (systemState == SwerveState.TELEOP_MODE) {
            if (requestManual) {
                nextSystemState = SwerveState.Manual_MODE;
            }
        }

        if (nextSystemState != systemState) {
            lastTransitioned = RobotController.getFPGATime()/1.0E6;
            systemState = nextSystemState;
        }
    }   
}
