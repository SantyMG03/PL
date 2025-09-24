%%
%%
[a-zA-Z]+ {return new Yytoken(yytext());}
[^]         {}