
program DemoProcedure;

procedure PrintOne(a: integer);
begin
  writeln('First parameter (integer): ', a);
end;

var
  x: integer;
  ch: char;

begin
  x := 42;
  ch := 'A';

  PrintOne(x);
end.