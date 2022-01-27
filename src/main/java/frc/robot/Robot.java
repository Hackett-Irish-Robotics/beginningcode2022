/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoSink;
//import edu.wpi.first.cscore.VideoSource;
import edu.wpi.first.cscore.VideoSource.ConnectionStrategy;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DoubleSolenoid;
//import edu.wpi.first.networktables.NetworkTableEntry;
//import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.RobotDrive;
//import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.motorcontrol.Victor;
import edu.wpi.first.wpilibj.XboxController;
//import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.Timer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  // Input and Ouput
  public static OI m_oi;


  Command m_autonomousCommand, autoRightStartCommand, autoLeftStartCommand, autoSlalom, autoSlalomLoop, autoBarrel, autoBounce;
  SendableChooser<Command> m_chooser = new SendableChooser<>();

  UsbCamera camera1;
  UsbCamera camera2;
  VideoSink server;
  NetworkTableEntry cameraSelection;
  CameraServer camServer;
  
  MecanumDrive robotDrive;
  Victor frontLeft, frontRight, backLeft, backRight;

  XboxController xbox1;
  XboxController xbox2;
  
  Victor shooterLeft;
  Victor shooterRight;

  Victor shooter1;
  Victor shooter2;
  
  
  DoubleSolenoid climb;

  Timer t;



  // Limelight stuff
  NetworkTable table;
  NetworkTableEntry tx, ty, ta, tv;
  double limeX, limeY, limeArea, limeHasTargetFloat;
  Boolean limeHasTarget;


  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    camera1 = CameraServer.startAutomaticCapture(0);
    camera2 = CameraServer.startAutomaticCapture(1);
    //camera1 = new UsbCamera("cam1", 0);
    //System.out.println(camera1.isEnabled());
    //camera2 = new UsbCamera("cam2", 1);
   // camServer = CameraServer.getInstance();
    server = CameraServer.getServer();
    //System.out.println(camera1.isEnabled());
    //System.out.println(camera2.isEnabled());

    //camera1.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
    //camera2.setConnectionStrategy(ConnectionStrategy.kKeepOpen);

    cameraSelection = NetworkTableInstance.getDefault().getTable("").getEntry("CameraSelection");
    //VideoSource source = camera1;
    CameraServer.addSwitchedCamera("Camera View");
    CameraServer.getVideo(camera1);

    xbox1 = new XboxController(RobotMap.xboxController1);
    xbox2 = new XboxController(RobotMap.xboxController2);

    frontLeft = new Victor(RobotMap.leftFrontMotor);
    frontRight = new Victor(RobotMap.rightFrontMotor);
    backLeft = new Victor(RobotMap.leftBackMotor);
    backRight = new Victor(RobotMap.rightBackMotor);

    shooterLeft = new Victor(RobotMap.leftShooterMotor);
    shooterRight = new Victor(RobotMap.rightShooterMotor);

    shooter1 = new Victor(RobotMap.shooterMotor1);
    shooter2 = new Victor(RobotMap.shooterMotor2);
    



    robotDrive = new MecanumDrive(frontLeft, backLeft, frontRight, backRight);
    //robotDrive.setMaxOutput(.25);

    // Limelight stuff
    table = NetworkTableInstance.getDefault().getTable("limelight");
    tx = table.getEntry("tx");
    ty = table.getEntry("ty");
    ta = table.getEntry("ta");
    tv = table.getEntry("tv");

    // Instantiate our input/output.
    m_oi = new OI();

    // chooser.addOption("My Auto", new MyAutoCommand());
    autoRightStartCommand = new Command(){
    
      @Override
      protected boolean isFinished() {
        // Auto-generated method stub
        return false;
      }

      @Override
      protected void execute() {
        double cT = t.get();
        if (cT < 1) {
          robotDrive.driveCartesian(0, -0.25, 0);
        }
        else if (cT > 1 && cT < 3) {
          robotDrive.driveCartesian(0, 0, 0);
        }
        else if (cT > 3 && cT < 5) {
          robotDrive.driveCartesian(0.25, 0, 0);
        }
        else if (cT > 5 && cT < 6) {
          robotDrive.driveCartesian(0, -0.25, 0);
        }
        else if (cT > 6 && cT < 8) {
          robotDrive.driveCartesian(0, 0, -0.25);
        }
        else if (cT > 8 && cT < 8.25) {
          robotDrive.driveCartesian(0, 0.75, 0);
        }
        else if (cT > 8.25 && cT < 9) {
          robotDrive.driveCartesian(-0.25, 0, 0);
        }
        else if (cT > 9 && cT < 10) {
          robotDrive.driveCartesian(0, -0.25, 0);
        }
        else {
          robotDrive.driveCartesian(0, 0, 0);
        }
      }
    };

    autoLeftStartCommand = new Command(){
    
      @Override
      protected boolean isFinished() {
        // Auto-generated method stub
        return false;
      }

      @Override
      protected void execute() {
        double cT = t.get();
        if (cT < 1) {
          robotDrive.driveCartesian(0, 0.25, 0);
        }
        else if (cT > 1 && cT < 3) {
          robotDrive.driveCartesian(0, 0, 0);
        }
        else if (cT > 3 && cT < 5) {
          robotDrive.driveCartesian(0.25, 0, 0);
        }
        else if (cT > 5 && cT < 6) {
          robotDrive.driveCartesian(0, -0.25, 0);
        }
        else if (cT > 6 && cT < 8) {
          robotDrive.driveCartesian(0, 0, -0.25);
        }
        else if (cT > 8 && cT < 8.25) {
          robotDrive.driveCartesian(0, 0.75, 0);
        }
        else if (cT > 8.25 && cT < 9) {
          robotDrive.driveCartesian(-0.25, 0, 0);
        }
        else if (cT > 9 && cT < 10) {
          robotDrive.driveCartesian(0, -0.25, 0);
        }
        else {
          robotDrive.driveCartesian(0, 0, 0);
        }
      }
    };

    autoSlalom = new Command(){  
      @Override
      protected boolean isFinished() {
        // Auto-generated method stub
        return false;
      }
      @Override
      protected void execute() {
        //Declare and initialize driveCart time intervals for
        // double[] interval = {1.5, 3.4, 4.4, 3.4, 1.45, 3.4, 1.45, 3.4, 4.3, 3.2, 1.2, 0.3};

        double[] interval = {0.9, 1.7, 3.05, 2, 1.1, 1.7, 1.15, 2.3, 3.0, 2.3, 1, 0.15};

        //double[] time = new double[12];
        /*int j;
        for(j = 0; j < interval.length; j++){
          time[j] += interval[j];
        }*/

        double[] time = new double[12];
        for(int j = 0; j < interval.length; j++){
          if (j == 0){
            time[j] = interval[j];
          }
          else{
            time[j] = time[j-1] + interval[j];
          }
        }

        // double[] time = {1, 3, 6, 8.1, 9.1, 11, 12, 14, 17, 18.9, 19.9, 20.2};

        //interval of time inbetween actions
        double rotation = -0.033;
        double speed = 0.5;
        double cT = t.get();
      
        //forward
        if (cT > 0 && cT < time[0]) {
          robotDrive.driveCartesian(0, speed, rotation);
        }
        //left
        else if (cT > time[0] && cT < time[1]) {
          robotDrive.driveCartesian(-1.3*speed, 0, 1.2*rotation);
        }
        //forward
        else if (cT > time[1] && cT < time[2]) {
          robotDrive.driveCartesian(0, speed, 1.1*rotation);
        }
        //right
        else if (cT > time[2] && cT < time[3]) {
          robotDrive.driveCartesian(speed, 0, 0.7*rotation);
        }
        //forward
        else if (cT > time[3] && cT < time[4]) {
          robotDrive.driveCartesian(0, speed, 1.3*rotation);
        }
        //left
        else if (cT > time[4] && cT < time[5]) {
          robotDrive.driveCartesian(-speed, 0, 0.8*rotation);
        }
        //backwards
        else if (cT > time[5] && cT < time[6]) {
          robotDrive.driveCartesian(0, -speed, 0);
        }
        //right
        else if (cT > time[6] && cT < time[7]) {
          robotDrive.driveCartesian(speed, 0, 0.4*rotation);
        }
        //backwards
        else if (cT > time[7] && cT < time[8]) {
          robotDrive.driveCartesian(0, -speed, 0.5*rotation);
        }
        //left
        else if (cT > time[8] && cT < time[9]) {
          robotDrive.driveCartesian(-speed, 0, rotation);
        }
        //backwards
        else if (cT > time[9] && cT < time[10]) {
          robotDrive.driveCartesian(0, -speed, -rotation);
        }
        //braking
        else if (cT > time[10] && cT < time[11]) {
          robotDrive.driveCartesian(0, speed, 0);
        }
        else {
          robotDrive.driveCartesian(0, 0, 0);
        }
      }
    };

    autoBounce = new Command(){
      @Override
      protected boolean isFinished() {
        return false;
      }
      @Override
      protected void execute() {
        double speed = 0.5;
        double zrotation = -0.03;
        double cT = t.get();

        // double[] stopwatch = {
        //   1.2,//this is the first one, vertical
        //   2.4,//horizontal
        //   3.6,//horizontal
        //   3.2,//vertical
        //   4.0,//horizontal
        //   4.4,//vertical
        //   5.8,//horizontal
        //   7.0,//horizontal
        //   7.4,//vertical
        //   8.4,//horizontal
        //   9.2,//horizontal
        //   9.5,//vertical
        //   9.7 //vertical
        // };

        double[] interval = {2.2, 1.3, 1.35, 1.85, 1.1, 3.3, 2.2, 2.3, 6.5, 2.0, 1.15, 2.5, 0.1};

        double[] time = new double[13];
        for(int j = 0; j < interval.length; j++){
          if (j == 0){
            time[j] = interval[j];
          }
          else{
            time[j] = time[j-1] + interval[j];
          }
        }

        //robot starts 90 degrees to the left
        //right
        if (cT > 0 && cT < time[0]) {
          robotDrive.driveCartesian(speed, 0, 0);
        }
        //forward
        else if (cT > time[0] && cT < time[1]) {
          robotDrive.driveCartesian(0, speed, zrotation);
        }
        //backwards
        else if (cT > time[1] && cT < time[2]) {
          robotDrive.driveCartesian(0, -speed, 1.2*zrotation);
        }
        //right
        else if (cT > time[2] && cT < time[3]) {
          robotDrive.driveCartesian(speed, 0, zrotation);
        }
        //backwards
        else if (cT > time[3] && cT < time[4]) {
          robotDrive.driveCartesian(0, -speed, zrotation);
        }
        //right
        else if (cT > time[4] && cT < time[5]) {
          robotDrive.driveCartesian(speed, 0, 0.4*zrotation);
        }
        //forward
        else if (cT > time[5] && cT < time[6]) {
          robotDrive.driveCartesian(0, speed, 1.5*zrotation);
        }
        //backwards
        else if (cT > time[6] && cT < time[7]) {
          robotDrive.driveCartesian(0, -speed, zrotation);
        }
        //right
        else if (cT > time[7] && cT < time[8]) {
          robotDrive.driveCartesian(speed, 0, 0.3*zrotation);
        }
        //forward
        else if (cT > time[8] && cT < time[9]) {
          robotDrive.driveCartesian(0, speed, 1.7*zrotation);
        }
        //backwards
        else if (cT > time[9] && cT < time[10]) {
          robotDrive.driveCartesian(0, -speed, -0.1*zrotation);
        }
        //right
        else if (cT > time[10] && cT < time[11]) {
          robotDrive.driveCartesian(speed, 0, 0);
        }
        //left
        else if (cT > time[11] && cT < time[12]) {
          robotDrive.driveCartesian(-speed, 0, 0);
        }
        //no more movement
        else {
          robotDrive.driveCartesian(0, 0, 0);
        }
      };
    };

    autoBarrel = new Command(){
      @Override
      protected boolean isFinished() {
        // Auto-generated method stub
        return false;
      }
      @Override
      protected void execute() {
        //Declare and initialize driveCart time intervals
        //double[] interval = {3.1, 2.95, 1.8, 2.9, 3.3, 2.8, 1.8, 2.8, 2.7, 2.4, 2.1, 0.8, 4.2};
        double[] interval = {2.5, 1.7, 1.2, 1.8, 2.6, 1.9, 1.05, 2.1, 1.7, 2.35, 1.3, 1.7, 3.4, 0.2};

        double[] time = new double[14];
        for(int j = 0; j < interval.length; j++){
          if (j == 0){
            time[j] = interval[j];
          }
          else{
            time[j] = time[j-1] + interval[j];
          }
        }

        // Elapsed Time
        // double[] time = {2.9, 4.6, 5.8, 7.7, 10.3, 12, 13.2, 14.9, 16.8, 18.2, 19.5, 21, 25.7, 26};

        //interval of time inbetween actions
        //double wait = 0.75; 

        double speed = 0.5;
        double rotation = -0.035; //0.03 maybe
        double cT = t.get();

        //path of robot
        if (cT > 0 && cT < time[0]) {
          robotDrive.driveCartesian(0, speed, 1.1*rotation);
        }
        else if (cT > time[0] && cT < time[1]){
          robotDrive.driveCartesian(speed, 0, rotation);
        }
        else if (cT > time[1] && cT < time[2]){
          robotDrive.driveCartesian(0, -speed, 0);
        }
        else if (cT > time[2] && cT < time[3]){
          robotDrive.driveCartesian(-speed, 0, 1.1*rotation);
        }
        else if (cT > time[3] && cT < time[4]){
          robotDrive.driveCartesian(0, speed, 1.4*rotation);
        }
        else if (cT > time[4] && cT < time[5]){
          robotDrive.driveCartesian(-speed, 0, rotation);
        }
        else if (cT > time[5] && cT < time[6]){
          robotDrive.driveCartesian(0, -speed, 0);
        }
        else if (cT > time[6] && cT < time[7]){
          robotDrive.driveCartesian(speed, 0, 0);
        }
        else if (cT > time[7] && cT < time[8]){
          robotDrive.driveCartesian(0, speed, rotation);
        }
        else if (cT > time[8] && cT < time[9]){
          robotDrive.driveCartesian(speed, 0, 0);
        }
        else if (cT > time[9] && cT < time[10]){
          robotDrive.driveCartesian(0, -speed, rotation);
        }
        else if (cT > time[10] && cT < time[11]){
          robotDrive.driveCartesian(-speed, 0, 1.3*rotation);
        }
        else if (cT > time[11] && cT < time[12]){
          robotDrive.driveCartesian(0, -speed, 1.3*rotation);
        }
        //braking
        else if (cT > time[12] && cT < time[13]){
          robotDrive.driveCartesian(0, speed, 0); //may want to change to full speed for braking
        }
        else {
          robotDrive.driveCartesian(0, 0, 0);
        }
      }
    };

    

    m_chooser.addOption("Auto Right Start", autoRightStartCommand);
    m_chooser.addOption("Auto Left Start", autoLeftStartCommand);
    m_chooser.addOption("Auto Slalom Start", autoSlalom);
    m_chooser.addOption("Auto Barrel Start", autoBarrel);
    m_chooser.addOption("Auto Bounce Start", autoBounce);
    SmartDashboard.putData("Auto mode", m_chooser);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Limelight stuff
    //read values periodically
    limeX = tx.getDouble(0.0);
    limeY = ty.getDouble(0.0);
    limeArea = ta.getDouble(0.0);
    limeHasTargetFloat = tv.getDouble(0.0);
    if (limeHasTargetFloat == 1) {
      limeHasTarget = true;
    }
    else {
      limeHasTarget = false;
    }

    //post to smart dashboard periodically
    SmartDashboard.putNumber("LimelightX", limeX);
    SmartDashboard.putNumber("LimelightY", limeY);
    SmartDashboard.putNumber("LimelightArea", limeArea);
    SmartDashboard.putNumber("LimeHasTarget", limeHasTargetFloat);

    //Xbox controller start button switches camera to camera 1
    if (xbox2.getStartButton()) {
      
      camera1.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
      camera2.setConnectionStrategy(ConnectionStrategy.kForceClose);
      
      
    } 
    // Xbox controller back buttons switches camera to camera 2
    else if (xbox2.getBackButton()) {
      camera2.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
      camera1.setConnectionStrategy(ConnectionStrategy.kForceClose);
        
    }
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   * You can use it to reset any subsystem information you want to clear when
   * the robot is disabled.
   */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString code to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional commands to the
   * chooser code above (like the commented example) or additional comparisons
   * to the switch structure below with additional strings & commands.
   */

  // A shorter and easier move function to not worry about inverting (NEEDS TESTING)
  public void setMove(double forward, double right, double twistRight) {
    robotDrive.driveCartesian(right, -forward, twistRight);
  }

  public enum directions {
    Forward, Reverse, Left, Right
  }

  public void moveDir(directions dir, double speed) {
    switch (dir) {
      case Forward:
        setMove(0, speed, 0);
        break;
      case Reverse:
      setMove(0, -speed, 0);
        break;
      case Left:
      setMove(-speed, 0, 0);
        break;
      case Right:
        setMove(speed, 0, 0); 
        break;
      default:
        break;
    }
  }
  
  

  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_chooser.getSelected();

    robotDrive.setSafetyEnabled(false);

    t = new Timer();

    t.reset();
    t.start();

    /*
    robotDrive.driveCartesian(0, -0.25, 0);
    Timer.delay(1);
    robotDrive.driveCartesian(0, 0, 0);
    /*

    /*
     * String autoSelected = SmartDashboard.getString("Auto Selector",
     * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
     * = new MyAutoCommand(); break; case "Default Auto": default:
     * autonomousCommand = new ExampleCommand(); break; }
     */

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.start();
    }
  }

  /*
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();

    /*
    double cT = t.get();
    if (cT < 1) {
      robotDrive.driveCartesian(0, -0.25, 0);
    }
    else if (cT > 1 && cT < 3) {
      robotDrive.driveCartesian(0, 0, 0);
    }
    else if (cT > 3 && cT < 5) {
      robotDrive.driveCartesian(0.25, 0, 0);
    }
    else if (cT > 5 && cT < 6) {
      robotDrive.driveCartesian(0, -0.25, 0);
    }
    else if (cT > 6 && cT < 8) {
      robotDrive.driveCartesian(0, 0, -0.25);
    }
    else if (cT > 8 && cT < 8.25) {
      robotDrive.driveCartesian(0, 0.75, 0);
    }
    else if (cT > 8.25 && cT < 9) {
      robotDrive.driveCartesian(-0.25, 0, 0);
    }
    else if (cT > 9 && cT < 10) {
      robotDrive.driveCartesian(0, -0.25, 0);
    }
    else {
      robotDrive.driveCartesian(0, 0, 0);
    }
    */
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();

    robotDrive.setSafetyEnabled(false);

    // the slider (throttle) on the joystick sets speedAdj, which allows for real time speed limiting
    // speed cap is an extra option for a hard-coded speed limit that is applied to the throttle
    //double stickSlider = stick.getThrottle();
    double speedCap = 1;
    double spinCap = .69;
    //double speedAdj = speedCap * (1 - ((stickSlider + 1) / 2));

    // For precise movement: if trigger is pressed, spinning is blocked
   /*
    if (stick.getTrigger()) {
      spinCap = 0;
    }
    else {
      spinCap = 0.69;
    }

    // For precise movement: if thumb button is presses, moving is blocked
    if (stick.getRawButton(2)) {
      speedAdj = 0;
    }
*/
    //System.out.println(speedAdj);
    robotDrive.driveCartesian(speedCap*xbox1.getRawAxis(0), -speedCap*xbox1.getRawAxis(1), spinCap*xbox1.getRawAxis(4));

    // Limelight aiming testing
    // X-value adjusting (twisting)
    if (xbox1.getBButton()) {
      if (!limeHasTarget) {
        robotDrive.driveCartesian(0, 0, 0.2);
      }
      else {
        if (Math.abs(limeX) > 0.1) {
          if (limeX > 0) {
            robotDrive.driveCartesian(0, 0, 0.1+(limeX/44.7));
          }
          else {
            robotDrive.driveCartesian(0, 0, -0.1+(limeX/44.7));
          }
        }
      }   
    }

    // Area-based adjusting (distance from target)
    // 1.893410
    if (xbox1.getXButton()) {
      double error = limeArea - 1.89;
      if ((Math.abs(error) > 0.1) && limeHasTarget) {
        if (error > 0.5) {
          robotDrive.driveCartesian(0, -0.3, 0);
        }
        else if (error > 0.25 && error < 0.5) {
          robotDrive.driveCartesian(0, -0.2, 0);
        }
        else if (error > 0.1 && error < 0.25) {
          robotDrive.driveCartesian(0, -0.1, 0);
        }
        else {
          robotDrive.driveCartesian(0, 0.2, 0);
        }
        //robotDrive.driveCartesian(0, 0, ((limeX > 0) ? 0.1 : -0.1)+(limeX/44.7));
      }
    }

    // Xbox controller A button runs the intake
    if (xbox2.getAButton())
    {
       shooterLeft.set(1);    
       shooterRight.set(-1);

    }
    // Xbox controller B Button reverses intake (in case ball gets stuck in intake)
    else if (xbox2.getBButton())
    {
      shooterLeft.set(-0.2);    
      shooterRight.set(0.2);
    }
    // Else the motor stops
    else
    {
      shooterLeft.set(0);
      shooterRight.set(0);
    }

    // Xbox controller B button moves conveyer up 
    if (xbox2.getXButton()) 
    {
     // conveyer.setSpeed(-1);
    }
    // Xbox controller Y button moves conveyer down
    else if (xbox2.getYButton())
    {
     // conveyer.setSpeed(1);
    }
    // Else the motor stops
    else
    {
     // conveyer.setSpeed(0);
    }

    // Xbox controller left bumper runs shooter
    if (xbox2.getLeftBumper()) {
     // shooter1.setSpeed(0.8);
      //shooter2.setSpeed(-0.8);
    }
    // Xbox controller right bumper stops shooter
    else if (xbox2.getRightBumper())
    {
     // shooter1.setSpeed(0);
     // shooter2.setSpeed(0);
    }

    if (xbox1.getYButton()) 
    {
      climb.set(DoubleSolenoid.Value.kForward);
    }
    else if (xbox1.getAButton())
    {
      climb.set(DoubleSolenoid.Value.kReverse);
    }
    //MOVED TO ROBOT PERIODIC SO CAMERAS CAN BE USED WHEN TELEOP DISABLED
    //Xbox controller start button switches camera to camera 1
    /*
    if (xbox.getStartButton()) {
      
      camera1.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
      camera2.setConnectionStrategy(ConnectionStrategy.kForceClose);
      
      
    } 
    // Xbox controller back buttons switches camera to camera 2
    else if (xbox.getBackButton()) {
      camera2.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
      camera1.setConnectionStrategy(ConnectionStrategy.kForceClose);
        
    }
    */
    
  }


  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    
  }
}
