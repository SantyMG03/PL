%%
%%

0|[1-9][0-9]*			{ return new Yytoken(Yytoken.TOKEN_CTE_ENTERO, yytext()); }
[0-7]+				{ return new Yytoken(Yytoken.TOKEN_CTE_ENTERO, yytext()); }
0x[0-7a-fA-F]+			{ return new Yytoken(Yytoken.TOKEN_CTE_ENTERO, yytext()); }
(0|[1-9][0-9]*)[Ll]		{ return new Yytoken(Yytoken.TOKEN_CTE_ENTERO_LARGO, yytext()); }
[0-7]+[Ll]			{ return new Yytoken(Yytoken.TOKEN_CTE_ENTERO_LARGO, yytext()); }
0x[0-7a-fA-F]+[Ll]		{ return new Yytoken(Yytoken.TOKEN_CTE_ENTERO_LARGO, yytext()); }
0[0-9]+				{ return new Yytoken(Yytoken.TOKEN_ERROR, yytext()); }
0*x[a-zA-Z0-9]*			{ return new Yytoken(Yytoken.TOKEN_ERROR, yytext()); }
[0-9]+[Dd]				|
[0-9]+\.[0-9]*[Dd]?			|
[0-9]*\.[0-9]+[Dd]?			|
[0-9]+[Ee][+-]?[0-9]+[Dd]?		|
[0-9]+\.[0-9]*[Ee][+-]?[0-9]+[Dd]?	|
[0-9]*\.[0-9]+[Ee][+-]?[0-9]+[Dd]?	{ return new Yytoken(Yytoken.TOKEN_CTE_REAL_LARGO, yytext()); }
[0-9.EeDd+-]+				{ return new Yytoken(Yytoken.TOKEN_ERROR, yytext()); }
[0-9]+[Ff]+				|
[0-9]+\.[0-9]*[Ff]			|
[0-9]*\.[0-9]+[Ff]			|
[0-9]+[Ee][+-]?[0-9]+[Ff]		|
[0-9]+\.[0-9]*[Ee][+-]?[0-9]+[Ff]	|
[0-9]*\.[0-9]+[Ee][+-]?[0-9]+[Ff]	{ return new Yytoken(Yytoken.TOKEN_CTE_REAL_CORTO, yytext()); }
[^] 					{}

