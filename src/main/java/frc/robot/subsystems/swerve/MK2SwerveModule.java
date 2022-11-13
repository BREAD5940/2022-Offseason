package frc.robot.subsystems.swerve;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.MathUtil;

public class MK2SwerveModule {

    /* Declare the hardware here */
    public final double wheelRadius = 0.0508;
    public final CANSparkMax driveMotor;
    public final CANSparkMax turnMotor;
    public final RelativeEncoder driveEncoder;
    public final AnalogEncoder turnEncoder;
    public final PIDController turnPID = new PIDController(0.5, 0.0, 0);
    public final PIDController drivePID = new PIDController(0.01, 0.0, 0.0);
    public final SimpleMotorFeedforward driveFF = new SimpleMotorFeedforward(0.0, 2.82);
    public final boolean velEncoderReversed;
    public final boolean driveReversed;

    public MK2SwerveModule(int driveMotorID, int steerMotorID, int encoderID, double encoderOffset, boolean velEncoderReversed, boolean driveReversed) {
        // Configure the steer motor, drive motor, and the encoders. 
        driveMotor = new CANSparkMax(driveMotorID, MotorType.kBrushless);
        driveMotor.restoreFactoryDefaults();
        driveMotor.setIdleMode(IdleMode.kCoast);
        driveMotor.setSmartCurrentLimit(40);
        turnMotor = new CANSparkMax(steerMotorID, MotorType.kBrushless);
        turnMotor.restoreFactoryDefaults();
        turnMotor.setSmartCurrentLimit(25);
        driveEncoder = driveMotor.getEncoder();
        turnEncoder = new AnalogEncoder(encoderID, encoderOffset);
        turnPID.enableContinuousInput(-Math.PI, Math.PI);
        this.driveReversed = driveReversed;
        this.velEncoderReversed = velEncoderReversed;
    }
    public double getVelocity() {
        return velEncoderReversed ? -(((driveEncoder.getVelocity() * (1.0/7.04))/60.0) * 2 * Math.PI * wheelRadius) : 
        (((driveEncoder.getVelocity() * (1.0/7.04))/60.0) * 2 * Math.PI * wheelRadius);
    }
    public double getModuleAngle() {
        return turnEncoder.get();
    }
    public SwerveModuleState getState() {
        // Logic for returning swerve module state
        return new SwerveModuleState(getVelocity(), new Rotation2d(turnEncoder.get()));
    }
   
    public void setState(SwerveModuleState desiredState) {
        // Set the desired state of the swerve module
        SwerveModuleState state = SwerveModuleState.optimize(desiredState, new Rotation2d(turnEncoder.get()));
        double turnOutput = MathUtil.clamp(turnPID.calculate(turnEncoder.get(), state.angle.getRadians()), -0.5, 0.5);
        turnMotor.set(turnOutput);
        double driveFFOutput = driveFF.calculate(state.speedMetersPerSecond);
        double drivePIDOutput = drivePID.calculate(getVelocity(), state.speedMetersPerSecond);
        double driveOutput = MathUtil.clamp(driveFFOutput + drivePIDOutput, -12, 12);
        driveMotor.setVoltage(driveReversed ? -driveOutput : driveOutput);
    }


    
}
