/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

//import java.util.concurrent.Flow.Publisher;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
  // For example to map the left and right motors, you could define the
  // following variables to use with your drivetrain subsystem.
  // public static int leftMotor = 1;
  // public static int rightMotor = 2;

  // If you are using multiple modules, make sure to define both the port
  // number and the module. For example you with a rangefinder:
  // public static int rangefinderPort = 1;
  // public static int rangefinderModule = 1;

  // Victors on the RoboRio
  // We need to make sure these are always linked correctly or
  // we could damage the motors.

    //Tells the program that there is a drive motor connected to their respective victors (the ports on the RoboRio)
    public static final int leftFrontMotor = 1;
    public static final int rightFrontMotor = 2;
    public static final int leftBackMotor = 3;
    public static final int rightBackMotor = 4;

    //Tells the program that there is a shooter motor connected to their respective victors (the ports on the RoboRio)
    public static final int leftShooterMotor = 6;
    public static final int rightShooterMotor = 5;
    
    //Tells the program that there is a drive motor connected to their respective victors (the ports on the RoboRio)
    public static final int intakeMotor = 7;
    
    //Tells the program that there are two different controllers connected
    public static final int xboxController1 = 1;
    public static final int xboxController2 = 2;




  





}
