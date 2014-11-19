with Ada.Text_IO;	use Ada.Text_IO;

--implementation of package methods
package body MissileCommand is

	procedure up
	is
	begin
		Put_Line("Turret moved up by 1 step...");
	end up;

	procedure down
	is
	begin
		Put_Line("Turret moved up by 1 step...");
	end down;

	procedure left
	is
	begin
		Put_Line("Turret moved up by 1 step...");
	end left;

	procedure right
	is
	begin
		Put_Line("Turret moved up by 1 step...");
	end right;

	procedure fire
	is
	begin
		Put_Line("Launched!");
	end fire;

end MissileCommand;