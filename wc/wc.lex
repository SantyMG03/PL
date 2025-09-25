%%
%%
[\n\r]+     {return new Yytoken(Yytoken.EOL, yytext());}
[a-zA-Z]+   {return new Yytoken(Yytoken.WORD, yytext());}
[^]         {}