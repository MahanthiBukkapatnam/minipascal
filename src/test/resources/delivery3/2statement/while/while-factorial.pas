program SampleFullTest;
var
    n       :   integer;
    i       :   integer;
    fact    :   integer;
begin

    {
        For 1 to 10, factorials values

        1! = 1
        2! = 2
        3! = 6
        4! = 24
        5! = 120
        6! = 720
        7! = 5040
        8! = 40320
        9! = 362880
        10! = 3628800
    }

    n := 8;        { change this }
    fact := 1;
    i := 1;

    while i <= n do
    begin
        fact := fact * i;
        i := i + 1;
    end;

    write(fact);
end.
