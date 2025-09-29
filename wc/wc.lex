%%
%%
\n          {return new Yytoken(Yytoken.EOL, yytext());}
[a-zA-Z]+   {return new Yytoken(Yytoken.WORD, yytext());}
[ \t\r]+    {return new Yytoken(Yytoken.CHARACTER, yytext());}
[^]         {}