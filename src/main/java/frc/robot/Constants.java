package frc.robot;

public final class Constants {
    // Constants pertaining to the Intake subsystem
    public static class Drive {
        // Motor Ids
        // order: FL FR BL BR
        // these are not updated is the swerver file change both if are change one
        public static final int[] DRIVE_IDS = { 13, 10, 15, 17 }; // Motor Ids for driving motors
        public static final int[] ANGLE_IDS = { 12, 11, 14, 16 }; // Motor Ids for angle motors (Front)
        public static final int[] ENCODER_IDS = { 1, 3, 0, 2 }; // Motor Ids for encoders
    }

    // Constants pertaining to the Gut subsystem
    public static class Gut {
        // Motor Ids
        public static final int GUT_CLOSE_ID = 18;
        public static final int GUT_FAR_ID = 19;
    }

    // Constants pertaining to the Shooter subsystem
    public static class Shooter {
        // Motor Ids
        public static final int SHOOTER_ID = 24;
    }

    // Constants pertaining to the Intake subsystem
    public static class Intake {
        // Motor Ids
        public static final int INTAKE_DEPLOYMENT_ID = 20;
        public static final int INTAKE_VERTICAL_ROLLER_ID = 21;;
        public static final int INTAKE_HORIZONTAL_ROLLER_ID = 22;

        // Set points and other values
        //public static final double INTAKE_GEARING = 1.0/30;
        public static final double INTAKE_STOWED_SETPOINT = 0.5;
        public static final double INTAKE_DEPLOYED_SETPOINT = 5.0;
        public static final double INTAKE_SETPOINT_EPSILON = 0.000001d;
    }

    // Constants pertaining to the Climber subsystem
    public static class Climber {
        // Motor Ids
        public static final int CLIMBER_ID = 23;
        public static final double CLIMBER_DEPLOYED_SETPOINT = 0.0;
        public static final double CLIMBER_STOWED_SETPOINT = 0.0;
    }
}