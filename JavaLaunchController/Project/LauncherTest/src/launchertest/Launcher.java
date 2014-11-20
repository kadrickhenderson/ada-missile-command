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
    
    public void initDevice() {
        device = USB.getDevice(vendorID , deviceID);
        try {
            device.open(1, 0, -1);
            device.controlMsg(0x21 , 0x09 , 0 , 0 , LEDON, commandLength, 2000, false);
            device.controlMsg(0x21 , 0x09 , 0 , 0 , STOP, commandLength, 2000, false);
        } catch (USBException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void stopDevice() {
        if(device != null) {
            if(device.isOpen()) {
                try {
                    device.controlMsg(0x21 , 0x09 , 0 , 0 , LEDOFF, commandLength, 2000, false);
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
        processCommand(command, 100); //assumes default step size of 100ms. To overload this, send ms as integer
    }
    public void processCommand(String command, int persist) {
        long currMil = System.currentTimeMillis();
        try {
            switch(command) {
                case "UP":
                    device.controlMsg(0x21 , 0x09 , 0 , 0 , UP, commandLength, 2000, false);
                    break;
                case "DOWN":
                    device.controlMsg(0x21 , 0x09 , 0 , 0 , DOWN, commandLength, 2000, false);
                    break;
                case "LEFT":
                    device.controlMsg(0x21 , 0x09 , 0 , 0 , LEFT, commandLength, 2000, false);
                    break;
                case "RIGHT":
                    device.controlMsg(0x21 , 0x09 , 0 , 0 , RIGHT, commandLength, 2000, false);
                    break;
                case "FIRE":
                    //firing needs around 2.5 seconds
                    //hence override value of persist
                    persist = 2500;
                    device.controlMsg(0x21 , 0x09 , 0 , 0 , FIRE, commandLength, persist, false);
                    break;
                default:
                    device.controlMsg(0x21 , 0x09 , 0 , 0 , STOP, commandLength, 2000, false);
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
