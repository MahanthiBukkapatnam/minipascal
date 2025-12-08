program SampleFullTest;
var
    age     :   integer;
    flag    :   boolean;
begin
    age := 10;
    flag := false;

    if age >= 18 then
        flag := true;

    write(age);
    write(flag);
end.
