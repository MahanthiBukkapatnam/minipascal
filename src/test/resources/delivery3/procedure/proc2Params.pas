program DemoProcedure;

procedure PrintTwo(x: integer; y: integer);
begin
  writeln('First parameter (integer): ', x);
  writeln('Second parameter (integer): ', y);
end;

var
  a: integer;
  b: integer;

begin
  a := 42;
  b := 52;

  PrintTwo(a, b);
end.