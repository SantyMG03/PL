import java.util.*;

%%

%{
    public Map<String,String> symbols = new HashMap<>();
%}

%type String

%state IF

%%

<YYINITIAL> {
    /*
    Empieza por #define, seguido de espacio(s),
    un identificador (nombre del símbolo),
    espacio(s) y el valor del símbolo (se toma como String)
    */
    #define[ \t]+[A-Za-z_][A-Za-z0-9_]*[ \t][A-Za-z0-9_]+ { 
        String[] parts = yytext().trim().split("\\s+");
        symbols.put(parts[1], parts[2]);
    }

    #define[ \t]+[A-Za-z0-9_]*[ \t][A-Za-z0-9_]+ { 
        return "ERROR_DEFINICION";
    }

    #ifdef[ \t]+[A-Za-z_][A-Za-z0-9_]*[ \t][A-Za-z0-9_]+ { 
        String[] parts = yytext().trim().split("\\s+");
        if (symbols.containsKey(parts[1])) {
            yybegin(IF);
        }   
    }

    #ifdef[ \t]+[A-Za-z0-9_]*[ \t][A-Za-z0-9_]+ { 
        return "ERROR_IFDEF";  
    }
    
    "$\{"[A-Za-z_][A-Za-z0-9_]*"}" {
        String k = yytext().substring(2, yytext().length()-1);
        String v = symbols.get(k);
        if (v != null) {
            return v;
        } else {
            return "ID_NO_DEFINIDO";
        }
    }

    #endif      { return "ERROR_ENDIF_SIN_IF"; }

    <<EOF>>     { return null; }

    [^\n]+      |
    \n          { return yytext(); }
}

<IF> {
    #endif      { yybegin(YYINITIAL); }
    
    "$\{"[A-Za-z_][A-Za-z0-9_]*"}" {
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

    [^\n]+      |
    \n          { return yytext(); }
}