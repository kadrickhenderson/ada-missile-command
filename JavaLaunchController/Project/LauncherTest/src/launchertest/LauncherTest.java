package launchertest;

import java.util.Scanner;

/**
 *
 * @author Arjun Devane
 */
public class LauncherTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Launcher launcher = new Launcher();
        launcher.initDevice();
        //get user input until q is issued
        Scanner r = new Scanner(System.in);
        
        char command = 0;
        while(command != 'q') {
            System.out.print("Enter command....");
            command = r.next().charAt(0);
            switch(command) {
                case 'u' : launcher.processCommand("UP"); break;
                case 'd' : launcher.processCommand("DOWN"); break;
                case 'l' : launcher.processCommand("LEFT"); break;
                case 'r' : launcher.processCommand("RIGHT"); break;
                case 'f' : launcher.processCommand("FIRE"); break;
            }
        }
        launcher.stopDevice();
        System.out.println("Program Complete...");
    }
    
}
