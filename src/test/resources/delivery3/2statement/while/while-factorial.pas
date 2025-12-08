program SampleFullTest;
var
    n       :   integer;
    i       :   integer;
    fact    :   integer;
begin
    n := 5;        { change this }
    fact := 1;
    i := 1;

    while i <= n do
    begin
        fact := fact * i;
        i := i + 1;
    end;

    write(fact);
end.
