program Demo;
var
  a, b, c, d, z : integer;
begin
  a := 1;
  b := 2;
  c := 3;
  d := 4;

  z := a + b - c * d;
  writeln(z);
  z := a - b + c / d;
  writeln(z);
  z := a * b + c / d;
  writeln(z);
  z := a / b - c * d;
  writeln(z);
  z := a + b * c - d;
  writeln(z);
  z := a - b / c + d;
  writeln(z);
  z := a * b - c / d;
  writeln(z);
  z := a / b + c * d;
  writeln(z);
end.
