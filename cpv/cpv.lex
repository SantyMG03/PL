%%
%%

[^a-zA-Z]   {       }
[a-zA-Z]*[aeiouAEIOU][aeiouAEIOU]([a-zA-Z]*[aeiouAEIOU]+)*  {return new Yytoken(Yytoken.A, "0");}
[a-zA-Z]*[aeiouAEIOU][aeiouAEIOU][a-zA-Z]*                  {return new Yytoken(Yytoken.C, "0");}
[a-zA-Z]*[aeiouAEIOU]+                                      {return new Yytoken(Yytoken.B, "0");}
[a-zA-Z]*                                                   {return new Yytoken(Yytoken.D, "0");}