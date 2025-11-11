
%%

%{
        public int yylval; // Variable que contiene el valor del numero que se acaba de leer
    
        public final static int EOF=-1;
        public final static int MAS = 1;
        public final static int MENOS = 2;
        public final static int POR = 3;
        public final static int DIV = 4;
        public final static int AP=5; // Abre parentesis
        public final static int CP=6; // Cierra parentesis
        public final static int NUMERO = 7;
        public final static int NOPER = 101;
        public final static int MAXPROF = 102;
        public final static int EVAL = 103;
%}

%int


%%
nOper                     { return NOPER; }
maxProf                   { return MAXPROF; }
eval                      { return EVAL; }
\-?0|[1-9][0-9]*          {
                            yylval = new Integer(yytext());
                            return NUMERO;
                          }
\(                        { return AP; }
\)                        { return CP; }
\+                        { return MAS; }
\-                        { return MENOS; }
\*                        { return POR; }
\/                        { return DIV; }
\/\/.*                    {}
[^]                       {}

