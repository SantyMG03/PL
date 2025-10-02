%%
%%
\n          {return new Yytoken(Yytoken.EOL, yytext());}
[^ \t\r\n]+ {return new Yytoken(Yytoken.WORD, yytext());}
[ \t\r]+    {return new Yytoken(Yytoken.CHARACTER, yytext());}