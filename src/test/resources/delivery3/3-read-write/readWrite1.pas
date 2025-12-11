program SampleFullTest;
var
    i, n       : integer;
    ch        : char;
    flag      : boolean;
    sum       : integer;
    values    : array[1..5] of integer;
begin
    { Read a character and an integer from user }
    write('Enter a character: ');
    readln(ch);
    write('Enter an integer (n): ');
    readln(n);
    flag := true;
    sum := 0;

    { Initialize the array using complex expressions}
    values[1] := 2 * 3 + 1;             { 7 }
    values[2] := (10 div 2) + n;        { 5 + n }
    values[3] := values[1] + values[2] * 2;
    values[4] := n * 2;
    values[5] := values[3] - values[1];
    writeln;
    writeln('--- Initial Values ---');
    writeln('Character: ', ch);
    writeln('Boolean flag: ', flag);
    writeln('n = ', n);

    { While loop: compute sum of array values }
    i := 1;
    while i <= 5 do
    begin
        sum := sum + values[i];
        i := i + 1;
    end;
    writeln('Sum of array values: ', sum);


    { For loop: double each element }
    for i := 1 to 5 do
        values[i] := values[i] * 2;
    writeln;
    writeln('--- Array After Doubling ---');
    for i := 1 to 5 do
    begin
        writeln('values[', i, '] = ', values[i]);
    end;


    { Boolean expression involving user input and computed values }
    flag := (sum > 20) and (ch = 'A') or (values[3] < 10);
    writeln;
    writeln('Final boolean flag: ', flag);
end.
