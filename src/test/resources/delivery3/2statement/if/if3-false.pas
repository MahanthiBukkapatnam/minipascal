program SampleFullTest;
var
    age     :   integer;
    flag    :   boolean;
begin
    age     :=  10;
    flag    :=  false;

    if age < 90 then
        begin
            flag := false;
        end
    else
        begin
            flag := true;
        end;

    write(flag);

end.
