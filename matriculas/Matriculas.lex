%%

%class Yylex
%unicode
%public
%type String

%%

[A-Z]{1,2}-?[1-9][0-9]{0,5}             { return "S1"; }
[A-Z]{1,2}-?[0-9]{4}-?[A-PR-Z]{1,2}     { return "S2"; }
[0-9]{4}-?[B-DF-HJ-NPR-TV-Z]{3}         { return "S3"; }
([, \t\n\r]+)                            { return yytext(); }
[^, \t\n\r]+                              { return "X"; } // bloque inválido
