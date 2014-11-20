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
    private static final byte[] FIRE = {0x02, 0x10, 0x00,0x00,0x00,0x00,0x00,0x00};
    
    public void init() {
        Device device;
        device = USB.getDevice(vendorID , deviceID);
        try {
            device.open(1, 0, -1);
            device.controlMsg(0x21 , 0x09 , 0 , 0 , DOWN, commandLength, 2000, false);
            long currMil = System.currentTimeMillis();
            //execute for 0.1 seconds
            currMil += 100; //miliseconds
            while(currMil > System.currentTimeMillis()) {
                //do nothing!
            }
            device.controlMsg(0x21 , 0x09 , 0 , 0 , STOP, commandLength, 2000, false);
            device.close();
        } catch (USBException ex) {
            System.out.println("ERROR!");
            ex.printStackTrace();
        }
    }
}
