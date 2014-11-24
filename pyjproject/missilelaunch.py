#Missile launch pad controls
from tkinter import *
from tkinter import ttk
from py4j.java_gateway import JavaGateway

class LaunchControlls:
    def __init__(self, master):
        #Java connection
        gateway = JavaGateway()
        launcher = gateway.entry_point.getLauncher()

        #safety switch
        self.safety = 'ON'
        
        #place java calls in these methods
        def move_left(*args):
            #print (self.safety)
            if self.safety == 'OFF':
                launcher.processCommand("LEFT")
                #print ("move left!")
            else:
                print ("Can't move. Safety is on.")

        def move_right(*args):
            #print (self.safety)
            if self.safety == 'OFF':
                launcher.processCommand("RIGHT")
                #print ("move right!")
            else:
                print ("Can't move. Safety is on.")

        def move_up(*args):
            #print (self.safety)
            if self.safety == 'OFF':
                launcher.processCommand("UP")
                #print ("move up!")
            else:
                print ("Can't move. Safety is on.")

        def move_down(*args):
            #print (self.safety)
            if self.safety == 'OFF':
                launcher.processCommand("DOWN")
                #print ("move down!")
            else:
                print ("Can't move. Safety is on.")
            
        def fire(*args):
            #print (self.safety)
            if self.safety == 'OFF':
                launcher.processCommand("FIRE")
                print ("FIRE!")
            else:
                print ("Can't fire. Safety is on.")

        def arm(*args):
            launcher.initDevice()
            self.safety = 'OFF'
            print ("Device armed...ready to fire!")
            #print (self.safety)

        def safe(*args):
            launcher.stopDevice()
            self.safety = 'ON'
            print ("Safety On. Missiles offline")
            #print (self.safety)
            
        #Header frame for grid
        #Create a lattitude, longitude coordinate system that suggest or shows the x and y coordinates in a small window
        self.frame_grid = ttk.Frame(master)
        self.frame_grid.pack(padx = 10, pady = 10, side = LEFT)
        
        self.canvas_grid = Canvas(self.frame_grid, width = 300, height = 300, background = 'black')
        self.canvas_grid.grid(row = 0, column = 0)
        #--------------------------------------------GRID------------------------------------------------
        #Vertical lines
        linev1 = self.canvas_grid.create_line(0,0,0,303, fill = '#00FF00', width = 2)
        linev2 = self.canvas_grid.create_line(30,0,30,303, fill = '#00FF00', width = 2)
        linev3 = self.canvas_grid.create_line(60,0,60,303, fill = '#00FF00', width = 2)
        linev4 = self.canvas_grid.create_line(90,0,90,303, fill = '#00FF00', width = 2)
        linev5 = self.canvas_grid.create_line(120,0,120,303, fill = '#00FF00', width = 2)
        linev6 = self.canvas_grid.create_line(150,0,150,303, fill = '#00FF00', width = 2)
        linev7 = self.canvas_grid.create_line(180,0,180,303, fill = '#00FF00', width = 2)
        linev8 = self.canvas_grid.create_line(210,0,210,303, fill = '#00FF00', width = 2)
        linev9 = self.canvas_grid.create_line(240,0,240,303, fill = '#00FF00', width = 2)
        linev10 = self.canvas_grid.create_line(270,0,270,303, fill = '#00FF00', width = 2)

        #Horizontal lines
        lineh1 = self.canvas_grid.create_line(0,0,303,0, fill = '#00FF00', width = 2)
        lineh2 = self.canvas_grid.create_line(0,30,303,30, fill = '#00FF00', width = 2)
        lineh3 = self.canvas_grid.create_line(0,60,303,60, fill = '#00FF00', width = 2)
        lineh4 = self.canvas_grid.create_line(0,90,303,90, fill = '#00FF00', width = 2)
        lineh5 = self.canvas_grid.create_line(0,120,303,120, fill = '#00FF00', width = 2)
        lineh6 = self.canvas_grid.create_line(0,150,303,150, fill = '#00FF00', width = 2)
        lineh7 = self.canvas_grid.create_line(0,180,303,180, fill = '#00FF00', width = 2)
        lineh8 = self.canvas_grid.create_line(0,210,303,210, fill = '#00FF00', width = 2)
        lineh9 = self.canvas_grid.create_line(0,240,303,240, fill = '#00FF00', width = 2)
        lineh10 = self.canvas_grid.create_line(0,270,303,270, fill = '#00FF00', width = 2)

        #Targetting zone
        targetZone = self.canvas_grid.create_arc(0,0,303,303, fill = '#FF0000', width = 1, style = 'pieslice', extent = 270, start = -45)
        voidZone = self.canvas_grid.create_arc(112,110,192,190, fill = '#000000', width = 2, style = 'pieslice', extent = 270, start = -45)
        #--------------------------------------------END GRID------------------------------------------------
        def target_coordinates(event):
            global location
            self.canvas_grid.create_oval(event.x-5, event.y-5, event.x+5, event.y+5, fill="red", width=2)
            #print("num: {}".format(event.num))
        
            #print("x: {}".format(event.x))
            #print("y: {}".format(event.y))

            launcher.targetTurret(event.x,event.y)
            
            location = event
            
        self.canvas_grid.bind('<ButtonPress-1>', target_coordinates)
        
        #Missiles Display
        self.frame_missiles = ttk.Frame(master)
        self.frame_missiles.pack(side = TOP, padx=20, pady=20)
        
        self.canvas_missiles = Canvas(self.frame_missiles)
        self.canvas_missiles.pack(side = RIGHT)
        
        #Missile images
        self.missile1 = PhotoImage(file = 'missile1.png')
        self.missile2 = PhotoImage(file = 'missile2.png')
        self.missile3 = PhotoImage(file = 'missile3.png')
        self.missile4 = PhotoImage(file = 'missile4.png')
        
        ttk.Label(self.canvas_missiles, image = self.missile1).grid(row = 0, column = 0)
        ttk.Label(self.canvas_missiles, image = self.missile2).grid(row = 0, column = 1)
        ttk.Label(self.canvas_missiles, image = self.missile3).grid(row = 0, column = 2)
        ttk.Label(self.canvas_missiles, image = self.missile4).grid(row = 0, column = 3)

        #Body Frame for controlls
        self.frame_content = ttk.Frame(master)
        self.frame_content.pack(padx=40,pady=40, side = LEFT)
        self.canvas_controls = Canvas(self.frame_content)
        
        self.crosshairLogo = PhotoImage(file = 'crosshairs.png').subsample(1,1)
        self.left = PhotoImage(file = 'left.png').subsample(1,1)
        self.right = PhotoImage(file = 'right.png').subsample(1,1)
        self.up = PhotoImage(file = 'up.png').subsample(1,1)
        self.down = PhotoImage(file = 'down.png').subsample(1,1)
        self.arm = PhotoImage(file = 'arm.png').subsample(1,1)
        self.safe = PhotoImage(file = 'safe.png').subsample(1,1)

        Button(self.frame_content, image = self.arm, command=arm).grid(row = 1, column = 0, columnspan = 2, pady = 10)
        Button(self.frame_content, image = self.safe, command=safe).grid(row = 2, column = 0, columnspan = 2, pady = 10)
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
    root.geometry("545x430")
    root.iconbitmap('Dod-seal.ico')
    root.resizable(width=FALSE, height=FALSE)
    root.wm_title("Department of Defense")
    #root.attributes("-alpha",0.5)

    root.mainloop()
    
if __name__ == "__main__": main()

