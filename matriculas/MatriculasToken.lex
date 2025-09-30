%%

%class Yylex
%unicode
%public
%type Yytoken

%%
[A-Z]{1,2}-?[1-9][0-9]{0,5}             { return new Yytoken(Yytoken.S1, yytext()); }
[A-Z]{1,2}-?[0-9]{4,4}-?[A-PR-Z]{1,2}   { return new Yytoken(Yytoken.S2, yytext()); }
[0-9]{4,4}-?[B-DF-HJ-NPR-TV-Z]{3,3}     { return new Yytoken(Yytoken.S3, yytext()); }
([, \t\n\r])+                           { return new Yytoken(Yytoken.SEPARADOR, yytext()); }
[^, \t\n\r]+                            { return new Yytoken(Yytoken.INVALIDO, yytext()); }
[^]                                     {}
