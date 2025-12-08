program Demo;
var
  x, y, z : integer;
begin
  readln(x);
  readln(y);

  x := 100;
  y := 200;

  z := (x + y) * 2;   {  (100 + 200 ) * 2 =  (300) * 2  = 600 }
  writeln(z)
end.