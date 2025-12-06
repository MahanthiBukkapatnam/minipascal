program SampleFullTest;
var
    age     :   integer;
    flag    :   boolean;
begin
    age := 10;

    if age < 90 then
        begin
            writeln('You are not yet 90 years old');
        end
    else
        begin
            writeln('You qualify for a special price');
        end;
end.
