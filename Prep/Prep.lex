import java.util.*;

%%

%{
    public Map<String,String> symbols = new HashMap<>();
%}

%type String
%unicode

%state IF
%state NOTIF

%%

<YYINITIAL> {
    /*
    Empieza por #define, seguido de espacio(s),
    un identificador (nombre del símbolo),
    espacio(s) y el valor del símbolo (se toma como String)
    */
    ^#define[ \t]+[A-Za-z][A-Za-z0-9_]*[ \t]+.*\n { 
        String[] parts = yytext().trim().split("\\s+", 3);
        symbols.put(parts[1], parts[2]);
    }

    ^#define[ \t]+[0-9_]+[A-Za-z0-9_]*[ \t]+[A-Za-z0-9_]+ { 
        return "ERROR_DEFINICION";
    }

    ^#ifdef[ \t]+[A-Za-z_][A-Za-z0-9_]*.*\n { 
        String[] parts = yytext().trim().split("\\s+");
        if (symbols.containsKey(parts[1])) {
            yybegin(IF);
        } else {
            yybegin(NOTIF);
        }
    }

    #ifdef[ \t]+[A-Za-z0-9_]*[ \t][A-Za-z0-9_]+ { 
        return "ERROR_IFDEF";  
    }
    
    \$\{[A-Za-z_][A-Za-z0-9_]*\} {
        String k = yytext().substring(2, yytext().length()-1);
        String v = symbols.get(k);
        if (v != null) {
            return v;
        } else {
            return "ID_NO_DEFINIDO";
        }
    }

    ^#endif[ \t]+.*\n      { return "ERROR_ENDIF_SIN_IF\n"; }
    ^#endif\n              { return "ERROR_ENDIF_SIN_IF\n"; }

    <<EOF>>     { return null; }

    .           |
    \n          { return yytext(); }
}

<IF> {
    ^#endif[ \t]+.*\n    { yybegin(YYINITIAL); }
    ^#endif\n            { yybegin(YYINITIAL); }
    
    \$\{[A-Za-z_][A-Za-z0-9_]*\} {
        String k = yytext().substring(2, yytext().length()-1);
        String v = symbols.get(k);
        if (v != null) {
            return v;
        } else {
            return "ID_NO_DEFINIDO";
        }
    }

    #ifdef[ \t]+[A-Za-z0-9_]*[ \t][A-Za-z0-9_]+ { 
        return "ERROR_IF_ANIDADO";  
    }

    #define[ \t]+[A-Za-z_][A-Za-z0-9_]*[ \t][A-Za-z0-9_]+ |
    #define[ \t]+[A-Za-z0-9_]*[ \t][A-Za-z0-9_]+ { 
        return "ERROR_DEFINE_EN_IF";
    }

    .           |
    \n          { return yytext(); }
}

<NOTIF> {
    ^#endif[ \t]+.*\n    { yybegin(YYINITIAL); }
    ^#endif\n            { yybegin(YYINITIAL); }

    .           |
    \n          {  }
}