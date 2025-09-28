import java.io.*;

%%

%class Yylex

LETTER = [a-zA-Z_]
DIGIT = [0-9]
VARNAME = {LETTER}({LETTER}|{DIGIT})*
NUMBER = {DIGIT}+
QUOTE = \"
STRING = {QUOTE}[^\"]*{QUOTE}

%%

{VARNAME}={NUMBER} { 
    String[] parts = yytext().split("=");
    TablaSimbolos.put(parts[0], parts[1]);
}

{VARNAME}={STRING} { 
    String[] parts = yytext().split("=", 2);
    String value = parts[1];
    if (value.startsWith("\"") && value.endsWith("\"")) {
        value = value.substring(1, value.length() - 1);
    }
    TablaSimbolos.put(parts[0], value);
}

\${VARNAME} { 
    String var = yytext().substring(1); // Remove $
    String value = TablaSimbolos.get("$" + var);
    if (value == null) value = "";
    System.out.print(value);
}

. { 
    System.out.print(yytext()); 
}

\n { 
    System.out.print(yytext()); 
}
