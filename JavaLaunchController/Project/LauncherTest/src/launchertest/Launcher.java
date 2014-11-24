package launchertest;

import ch.ntb.usb.Device;
import ch.ntb.usb.USB;
import ch.ntb.usb.USBException;


/**
 * This class provides some functions to move/fire/changeLED
 * References used : http://coffeefueledcreations.com/blog/?p=131
 * @author stg44x2
 */
public class Launcher {
    private static final int verticalStepTime = 50; //ms
    private static final int horizontalStepTime = 100; //ms
    private static final int verticalSteps = 9;
    private static final int horizontalSteps = 40;
    private static final double verticalStepSize = 110 / verticalSteps;
    private static final double horizontalStepSize = 270 / horizontalSteps;
    
    private int currentVertical; //extreme down is 0. extreme up is 9-1 = 8
    private int currentHorizontal; //extreme left is 0, extreme right is 36-1 = 35
    
    private static final short vendorID = 0x2123;
    private static final short deviceID = 0x1010;
    
    private static final int commandLength = 8;
    private static final byte[] STOP = {0x02, 0x20, 0x00,0x00,0x00,0x00,0x00,0x00};
    private static final byte[] LEDON = {0x03, 0x01, 0x00,0x00,0x00,0x00,0x00,0x00};
    private static final byte[] LEDOFF = {0x03, 0x00, 0x00,0x00,0x00,0x00,0x00,0x00};
    private static final byte[] UP = {0x02, 0x02, 0x00,0x00,0x00,0x00,0x00,0x00};
    private static final byte[] DOWN = {0x02, 0x01, 0x00,0x00,0x00,0x00,0x00,0x00};
    private static final byte[] LEFT = {0x02, 0x04, 0x00,0x00,0x00,0x00,0x00,0x00};
    private static final byte[] RIGHT = {0x02, 0x08, 0x00,0x00,0x00,0x00,0x00,0x00};
    private static final byte[] FIRE = {0x02, 0x10, 0x00,0x00,0x00,0x00,0x00,0x00};
    
    private Device device;

    public Launcher() {
        device = null;
    }
    
    public void targetTurret(int x, int y) {
        //x and y are 0 to 300
        System.out.println("Targeting at x = "+x+" | y = "+y);
        //check whether x and y are within the targeting arc
        TargetSpecifications target;
        target = checkTargetDistance(x,y);
        if(target.targetInRange) {
            System.out.println("Target is in range....");
            //use the static variables defined in class
            //to calculate how many vertical and horizontal steps are needed
            //from current position step
            int requiredVertical = (int) Math.abs(target.distance / verticalStepSize);
            int requiredHorizontal = (int) Math.abs(target.angle / horizontalStepSize);
            
            //fix per trail runs
            requiredHorizontal -= 1;
            
            System.out.println("Moving from CV="+currentVertical+",CH="+currentHorizontal+
                    " to NV="+requiredVertical+",NH="+requiredHorizontal);
            
            //point to required vertical
            if(requiredVertical > currentVertical) {
                continuousMove("UP", requiredVertical - currentVertical, verticalStepTime);
                currentVertical = requiredVertical;
            }
            if(requiredVertical < currentVertical) {
                continuousMove("DOWN", currentVertical - requiredVertical, verticalStepTime);
                currentVertical = requiredVertical;
            }
            
            //point to required horizontal
            if(requiredHorizontal < currentHorizontal) {
                continuousMove("LEFT", currentHorizontal - requiredHorizontal, horizontalStepTime);
                currentHorizontal = requiredHorizontal;
            }
            if(requiredHorizontal > currentHorizontal) {
                continuousMove("RIGHT", requiredHorizontal - currentHorizontal, horizontalStepTime);
                currentHorizontal = requiredHorizontal;
            }
        }
        else {
            System.out.println("Target is out of range....");
        }
    }
    
    private void continuousMove(String command, int repetitions, int persist) {
        int i;
        long current;
        long waitUntil;
        for(i = 0 ; i < repetitions ; i++) {
            processCommand(command, persist);
            current = System.currentTimeMillis();
            waitUntil = current + 200; //wait for 200ms
            while(waitUntil > System.currentTimeMillis()) {
                //do nothing!
            }
        }
    }
    
    private TargetSpecifications checkTargetDistance(int x, int y) {
        TargetSpecifications targetSpec = new TargetSpecifications();
        targetSpec.targetInRange = false;
        
        int centerX = 150;
        int centerY = 150;
        int radius = 150;
        
        //get euclidean distance
        targetSpec.distance = Math.sqrt(Math.pow(centerX - x, 2) + Math.pow(centerY - y, 2));
        if(targetSpec.distance > 40 && targetSpec.distance < 150) {
            //check if angle is between -45 to 
            //break down this angle in 4 quadrants
            //first quadrant (clockwise from bottom left
            if(x <= 150 && y > 150) {
                targetSpec.angle = Math.acos((radius - x)/targetSpec.distance);
                targetSpec.angle = Math.toDegrees(targetSpec.angle);
                if(targetSpec.angle >= 0 && targetSpec.angle <= 45) {
                    targetSpec.targetInRange = true;
                }
                //convert angle to range between 0 and 270
                //for first quadrant it is subtracted from 45
                targetSpec.angle = 45 - targetSpec.angle;
            }
            //second quadrant
            else if(x <= 150 && y <= 150) {
                targetSpec.angle = Math.acos((radius - x)/targetSpec.distance);
                targetSpec.angle = Math.toDegrees(targetSpec.angle);
                if(targetSpec.angle >= 0 && targetSpec.angle <= 90) {
                    targetSpec.targetInRange = true;
                }
                //convert angle to range between 0 and 270
                //for second quadrant add 45
                targetSpec.angle = targetSpec.angle + 45;
            }
            //third quadrant
            else if(x > 150 && y <= 150) {
                targetSpec.angle = Math.acos((x - radius)/targetSpec.distance);
                targetSpec.angle = Math.toDegrees(targetSpec.angle);
                if(targetSpec.angle >= 0 && targetSpec.angle <= 90) {
                    targetSpec.targetInRange = true;
                }
                //convert angle to range between 0 and 270
                //for third quadrant add 45+90 to subtraction of angle from 90
                targetSpec.angle = (90 - targetSpec.angle) + 45 + 90;
            }
            //fourth quadrant
            else if(x > 150 && y > 150) {
                targetSpec.angle = Math.acos((x - radius)/targetSpec.distance);
                targetSpec.angle = Math.toDegrees(targetSpec.angle);
                if(targetSpec.angle >= 0 && targetSpec.angle <= 45) {
                    targetSpec.targetInRange = true;
                }
                //convert angle to range between 0 and 270
                //for third quadrant add 45+90+90
                targetSpec.angle = targetSpec.angle + 45 + 90 + 90;
            }
            System.out.println("Distance = "+targetSpec.distance+" | Angle = "+targetSpec.angle);
            //Forget about the voidZone now. and convert distance to range 0 to 110
            targetSpec.distance = targetSpec.distance - 40;
        }
        return targetSpec;
    }
    
    public void resetTurret() {
        //move to extreme left and down
        //then move right about 1 sec to right and 0.2 sec up
        processCommand("RIGHT", 7000);
        processCommand("DOWN", 2000);
        processCommand("LEFT", 2900);
        //processCommand("UP", 100);
        currentVertical = 0;
        currentHorizontal = (int) horizontalSteps / 2; //it is at midway right now
    }
    
    public void initDevice() {
        try {
            device = USB.getDevice(vendorID , deviceID);
            device.open(1, 0, -1);
            device.controlMsg(0x21 , 0x09 , 0 , 0 , LEDON, commandLength, 2000, false);
            device.controlMsg(0x21 , 0x09 , 0 , 0 , STOP, commandLength, 2000, false);
            resetTurret();
        } catch (USBException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void stopDevice() {
        if(device != null) {
            if(device.isOpen()) {
                try {
                    device.controlMsg(0x21 , 0x09 , 0 , 0 , LEDOFF, commandLength, 2000, false);
                    device.controlMsg(0x21 , 0x09 , 0 , 0 , STOP, commandLength, 2000, false);
                    device.close();
                } catch (USBException ex) {
                    System.out.println(ex.getMessage());
                } finally {
                    device = null;
                }
            }
        }
    }
    
    public void processCommand(String command) {
        if(command.equals("UP") || command.equals("DOWN")) {
            processCommand(command, 50); //assumes default step size of 50ms. To overload this, send ms as integer
        }
        else{
            processCommand(command, 100); //assumes default step size of 100ms. To overload this, send ms as integer
        }
    }
    public void processCommand(String command, int persist) {
        long currMil = System.currentTimeMillis();
        try {
            switch(command) {
                case "UP":
                    //System.out.println("Moving up for "+persist+"ms...");
                    device.controlMsg(0x21 , 0x09 , 0 , 0 , UP, commandLength, persist, false);
                    break;
                case "DOWN":
                    //System.out.println("Moving down for "+persist+"ms...");
                    device.controlMsg(0x21 , 0x09 , 0 , 0 , DOWN, commandLength, persist, false);
                    break;
                case "LEFT":
                    //System.out.println("Moving left for "+persist+"ms...");
                    device.controlMsg(0x21 , 0x09 , 0 , 0 , LEFT, commandLength, persist, false);
                    break;
                case "RIGHT":
                    //System.out.println("Moving right for "+persist+"ms...");
                    device.controlMsg(0x21 , 0x09 , 0 , 0 , RIGHT, commandLength, persist, false);
                    break;
                case "FIRE":
                    //firing needs around 3.5 seconds
                    //hence override value of persist
                    persist = 3500;
                    //System.out.println("Firing for "+persist+"ms...");
                    device.controlMsg(0x21 , 0x09 , 0 , 0 , FIRE, commandLength, persist, false);
                    break;
                default:
                    device.controlMsg(0x21 , 0x09 , 0 , 0 , STOP, commandLength, persist, false);
                    break;
            }
            //persist this command
            currMil += persist; //miliseconds
            while(currMil > System.currentTimeMillis()) {
                //do nothing!
            }
            //issue STOP
            device.controlMsg(0x21 , 0x09 , 0 , 0 , STOP, commandLength, 2000, false);
        }
        catch(USBException e) {
            System.out.println(e.getMessage());
        }
    }
}
