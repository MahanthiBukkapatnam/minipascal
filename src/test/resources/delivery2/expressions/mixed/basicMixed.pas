program Demo;
var
  a, b, c, z : integer;
begin
  a := 1;
  b := 2;
  c := 3;
  z := a + b - c;
  writeln(z);
  z := a - b + c;
  writeln(z);
  z := a * b + c;
  writeln(z);
  z := a + b * c;
  writeln(z);
  z := a * b - c;
  writeln(z);
  z := a - b * c;
  writeln(z);
  z := a / b + c;
  writeln(z);
  z := a + b / c;
  writeln(z);
  z := a / b - c;
  writeln(z);
  z := a - b / c;
  writeln(z);
  z := a * b / c;
  writeln(z);
  z := a / b * c;
  writeln(z);
end.

