program SampleFullTest;
var
    age     : integer;
    i, n    : integer;
    ch      : char;
    flag    : boolean;
    sum     : integer;
begin
    sum     := 30;
    age     := 30;
    flag    := (sum > 20) and (age < 10) or ( sum >= 30 );
    writeln(flag);
end.
