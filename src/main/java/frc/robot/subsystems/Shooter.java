package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static frc.robot.Constants.Shooter.*;


import static java.util.Map.entry;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;


public class Shooter {

    // declare motor and encoder
    private CANSparkMax shooterMotor = new CANSparkMax(SHOOTER_ID, MotorType.kBrushless);
    private RelativeEncoder encoder = shooterMotor.getEncoder();
    //private SimpleMotorFeedforward ff = new SimpleMotorFeedforward(0.1913043478, 1/460.0);

    // declare values
    private double setpoint = 0.0;
    private ShooterState systemState = ShooterState.IDLE;
    private boolean requestShoot = false;
    private double timeLastStateChange;
    private boolean isStoped = false;
    private PIDController pidShooter = new PIDController(0.002, 0, 0);

    public TreeMap<Double, Double> InterpolatingTable = new TreeMap<>(
        Map.ofEntries(
            entry(685.714111328125, 2.0),
            entry(1514.2861328125, 4.0),
            entry(2354.2861328125, 6.0),
            entry(3200.0, 8.0),
            entry(4051.42724609375, 10.0)
        )
    );

    public Shooter() {
        encoder.setVelocityConversionFactor(1); // gear ratio
        shooterMotor.enableVoltageCompensation(10.5);
    }

    public boolean isShooting() {
        return systemState != ShooterState.IDLE;
    }

    public boolean canShoot() {
        return (systemState == ShooterState.AT_SETPOINT && atSetPoint());
    }

    public double getVelocity() {
        return -encoder.getVelocity();
    }

    public void setFlywheelRPM(double rpm) {
        SmartDashboard.putNumber("setFlywheelRPM", rpm);
        if (rpm <= 50) {
            shooterMotor.set(0.0);
        } else {
            double outPut = pidShooter.calculate(getVelocity(), rpm) + getInterpolatingValue(rpm, InterpolatingTable);
            shooterMotor.setVoltage(-outPut);
        }
    }

    public double getInterpolatingValue(double dataPoint, TreeMap<Double, Double> InterpolatingTable) {
        Entry<Double, Double> ceil = InterpolatingTable.ceilingEntry(dataPoint);
        Entry<Double, Double> floor = InterpolatingTable.floorEntry(dataPoint);
        if (ceil == null) return floor.getValue();
        if (floor == null) return ceil.getValue();
        if (ceil.getValue().equals(floor.getValue())) return ceil.getValue();
        return (ceil.getValue() - floor.getValue()) / (ceil.getKey() - floor.getKey()) * (dataPoint - floor.getKey()) + floor.getValue();
    }

    public void requestIdle() {
        setpoint = 200;
        requestShoot = false;
    }

    public void requestShoot(double rpm) {
        setpoint = rpm;
        requestShoot = true;
    }

    // get time S
    private double getTime() {
        return RobotController.getFPGATime() / 1.0E6;
    }

    public boolean atSetPoint() {
        return Math.abs(setpoint - getVelocity()) < 100;
    }

    public void start() {
        isStoped = false;
    }

    public void stop() {
        isStoped = true;
    }

    public void periodic() {
        SmartDashboard.putNumber("shooter rpm", getVelocity());
        SmartDashboard.putBoolean("shooter can hoot", canShoot());
        SmartDashboard.putBoolean("thumbs up apm", atSetPoint());

        // if (!isStoped) {
        ShooterState lastSystemState = systemState;
        SmartDashboard.putString("Shooter State", systemState.toString());

        if (systemState == ShooterState.IDLE) {

            // output
            setFlywheelRPM(setpoint);

            // state change
            if (requestShoot == true) {
                systemState = ShooterState.APPROACHING_SETPOINT;
            }
        } else if (systemState == ShooterState.APPROACHING_SETPOINT) {

            // output
            setFlywheelRPM(setpoint);

            // state change
            if (atSetPoint()) {
                systemState = ShooterState.STABALIZING;
            } else if (requestShoot == false) {
                systemState = ShooterState.IDLE;
            }
        } else if (systemState == ShooterState.STABALIZING) {

            // output
            setFlywheelRPM(setpoint);

            // state change
            if (getTime() - timeLastStateChange >= 0.25) {
                systemState = ShooterState.AT_SETPOINT;
            } else if (!atSetPoint()) {
                systemState = ShooterState.APPROACHING_SETPOINT;
            } else if (requestShoot == false) {
                systemState = ShooterState.IDLE;
            }
        } else if (systemState == ShooterState.AT_SETPOINT) {

            // output
            setFlywheelRPM(setpoint);
            // state change
            if (!atSetPoint()) {
                systemState = ShooterState.APPROACHING_SETPOINT;
            } else if (requestShoot == false) {
                systemState = ShooterState.IDLE;
            }
        }
        if (lastSystemState != systemState) {
            timeLastStateChange = getTime();
        }
    } /*
       * else {
       * SmartDashboard.putString("Shooter State", "OFF");
       * setFlywheelRPM(0.0);
       * }
       */

    public enum ShooterState {
        IDLE,
        APPROACHING_SETPOINT,
        STABALIZING, // 0.25 seconds are waited stablizing before we shoot
        AT_SETPOINT
    }
}