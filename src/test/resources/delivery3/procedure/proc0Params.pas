program DemoProcedure;

procedure PrintProc();
begin
  writeln('Hello');
end;

var
  x: integer;
  ch: char;

begin
  x := 42;
  ch := 'A';

  PrintProc();
end.