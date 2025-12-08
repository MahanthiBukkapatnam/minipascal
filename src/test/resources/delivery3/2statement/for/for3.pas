program SampleFullTest;
var
    i, j, k, n       : integer;
begin
    n := 10;

    j := 1;
    k := 10;

    for i := j to k do
    begin
        n := n * 3;
        write(n);
    end;

    write(n);
end.
