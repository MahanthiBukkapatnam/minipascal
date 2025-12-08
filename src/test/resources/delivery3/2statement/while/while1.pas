program SampleFullTest;
var
    i       :   integer;
    sum     :   integer;
begin

    i := 1;
    while i <= 5 do
    begin
        sum := sum + i;
        i := i + 1;
    end;
    { writeln('Sum of array values: ', sum); }
    write(sum);
end.
