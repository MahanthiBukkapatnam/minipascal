program SampleFullTest;
var
    i, n       : integer;
    ch        : char;
    flag      : boolean;
    sum       : integer;
    values    : array[1..5] of integer;
begin
    n := 10;

    { For loop: double each element }
    for i := 1 to 5 do
    begin
        n := n * 2;
    end;

    write(n);
end.
