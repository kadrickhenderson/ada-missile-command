with Ada.Text_IO;
package MissileCommand is

--This is a basic set of methods for controlling the launcher
--Note : No parentheses because these basic methods do not take any arguments
procedure up;
procedure down;
procedure left;
procedure right;
procedure fire;

--TODO : add advanced methods which take input of co-ordinates probably? And will use the basic methods internally
--At this point it will make sense to make the basic methods as private

end MissileCommand;