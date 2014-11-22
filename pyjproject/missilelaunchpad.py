#Missile launch pad controls
from tkinter import *
from tkinter import ttk
from py4j.java_gateway import JavaGateway

class LaunchControlls:
    def __init__(self, master):
        #Java connection
        gateway = JavaGateway()
        launcher = gateway.entry_point.getLauncher()
        launcher.initDevice()
		
        #place java calls in these methods
        def move_left(*args):
            launcher.processCommand("LEFT")
            print ("move left!")

        def move_right(*args):
            launcher.processCommand("RIGHT")
            print ("move right!")

        def move_up(*args):
            launcher.processCommand("UP")
            print ("move up!")

        def move_down(*args):
            launcher.processCommand("DOWN")
            print ("move down!")

        def fire(*args):
            launcher.processCommand("FIRE")
            print ("FIRE!")
            
        #Header frame
        self.frame_header = ttk.Frame(master)
        self.frame_header.pack()
        
        self.logo = PhotoImage(file = 'Dod-seal.png').subsample(3,3)
        ttk.Label(self.frame_header, image = self.logo).grid(row = 1, column = 1, rowspan = 2, padx=20, pady=20)
        ttk.Label(self.frame_header, wraplength = 250,
                  text = ("The mission of the Department of Defense is to provide the military forces needed to"
                          " deter war and to protect the security of our country. The department's headquarters is at the Pentagon. "), justify = CENTER).grid(row = 4, column = 1)

        #Missiles Display
        self.canvas_missiles = Canvas(master)
        self.canvas_missiles.pack(padx=20, pady=20)
        
        #Missile images
        self.missile1 = PhotoImage(file = 'missile1.png')
        self.missile2 = PhotoImage(file = 'missile2.png')
        self.missile3 = PhotoImage(file = 'missile3.png')
        self.missile4 = PhotoImage(file = 'missile4.png')
        
        k = ttk.Label(self.canvas_missiles, image = self.missile1).grid(row = 0, column = 0)
        ttk.Label(self.canvas_missiles, image = self.missile2).grid(row = 0, column = 1)
        ttk.Label(self.canvas_missiles, image = self.missile3).grid(row = 0, column = 2)
        ttk.Label(self.canvas_missiles, image = self.missile4).grid(row = 0, column = 3)

        #Body Frame
        self.frame_content = ttk.Frame(master)
        self.frame_content.pack()
        
        self.crosshairLogo = PhotoImage(file = 'crosshairs.png').subsample(1,1)
        self.left = PhotoImage(file = 'left.png').subsample(1,1)
        self.right = PhotoImage(file = 'right.png').subsample(1,1)
        self.up = PhotoImage(file = 'up.png').subsample(1,1)
        self.down = PhotoImage(file = 'down.png').subsample(1,1)
        
        Button(self.frame_content, image = self.up, command=move_up).grid(row = 4, column = 1, columnspan = 2)
        Button(self.frame_content, image = self.left, command=move_left).grid(row = 5, column = 0)
        Button(self.frame_content, image = self.crosshairLogo, command=fire).grid(row = 5, column = 1, columnspan = 2, padx=10, pady=10)
        Button(self.frame_content, image = self.right, command=move_right).grid(row =5, column = 3)
        Button(self.frame_content, image = self.down, command=move_down).grid(row =6, column = 1, columnspan = 2)

        #for keyboard bindings
        master.bind('<space>', fire)
        master.bind('<Up>', move_up)
        master.bind('<Left>', move_left)
        master.bind('<Right>', move_right)
        master.bind('<Down>', move_down)
        
def main():
    #root tk window
    root = Tk()
    launcherpad = LaunchControlls(root)
    root.geometry("260x615")
    root.resizable(width=FALSE, height=FALSE)
    root.wm_title("Department of Defense")

    #intermediate progress bar
    progressbar = ttk.Progressbar(root, orient = HORIZONTAL, length = 200)
    progressbar.pack(padx=5, pady=5)
    progressbar.config(mode = 'indeterminate')
    progressbar.start()
    
    root.mainloop()
    
if __name__ == "__main__": main()

