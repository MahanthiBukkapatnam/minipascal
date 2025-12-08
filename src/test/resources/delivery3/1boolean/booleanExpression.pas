program SampleFullTest;
var
    i, n       : integer;
    ch        : char;
    flag      : boolean;
    sum       : integer;
    values    : array[1..5] of integer;
begin
    { Boolean expression involving user input and computed values }
    ch := 'A';
    flag := (sum > 20) and (ch = 'A') or (values[3] < 10);
    writeln;
    writeln('Final boolean flag: ', flag);
end.
