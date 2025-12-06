program SampleFullTest;
var
    age     :   integer;
    flag    :   boolean;
begin
    age := 100;

    if age < 90 then
        writeln('You are not yet 90 years old')
    else
        begin
            writeln('You qualify for a special price');
        end
    ;
end.
