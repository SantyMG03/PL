import java.io.*;

%%

%class Yylex
%standalone

LETTER = [a-zA-Z_]
DIGIT = [0-9]
VARNAME = {LETTER}({LETTER}|{DIGIT})*
NUMBER = {DIGIT}+
QUOTE = \"
STRING = {QUOTE}[^\"]*{QUOTE}
VALUE = [^;\n \t=]+
COMPLEX_VALUE = ([^;\n \t\\]|\\.)+

%{
    // Función para expandir variables dentro de una cadena, respetando escapes
    private String expandVariables(String text) {
        if (text == null) return "";
        
        StringBuilder result = new StringBuilder();
        int i = 0;
        
        while (i < text.length()) {
            if (i < text.length() - 1 && text.charAt(i) == '\\' && text.charAt(i + 1) == '$') {
                // \$ escapado - mantener como \$ literal y el nombre de variable
                result.append("\\$");
                i += 2;
                // Copiar el nombre de variable que sigue
                while (i < text.length() && 
                       (Character.isLetterOrDigit(text.charAt(i)) || text.charAt(i) == '_')) {
                    result.append(text.charAt(i));
                    i++;
                }
            } else if (i < text.length() - 1 && text.charAt(i) == '$') {
                // Encontramos una variable no escapada
                int start = i + 1;
                int end = start;
                
                // Buscar el final del nombre de variable
                while (end < text.length() && 
                       (Character.isLetterOrDigit(text.charAt(end)) || text.charAt(end) == '_')) {
                    end++;
                }
                
                if (end > start) {
                    String varName = text.substring(start, end);
                    String value = TablaSimbolos.get("$" + varName);
                    if (value != null) {
                        result.append(value);
                    }
                    i = end;
                } else {
                    result.append(text.charAt(i));
                    i++;
                }
            } else {
                result.append(text.charAt(i));
                i++;
            }
        }
        
        return result.toString();
    }
    
    // Función para procesar caracteres de escape
    private String processEscapes(String text) {
        if (text == null) return "";
        
        StringBuilder result = new StringBuilder();
        int i = 0;
        
        while (i < text.length()) {
            if (i < text.length() - 1 && text.charAt(i) == '\\') {
                char nextChar = text.charAt(i + 1);
                switch (nextChar) {
                    case ';':
                        result.append(';');
                        i += 2;
                        break;
                    case '"':
                        result.append('"');
                        i += 2;
                        break;
                    case '$':
                        result.append('$');
                        i += 2;
                        break;
                    case '\\':
                        result.append('\\');
                        i += 2;
                        break;
                    default:
                        // Si no es un escape reconocido, mantener el backslash
                        result.append('\\');
                        i++;
                        break;
                }
            } else {
                result.append(text.charAt(i));
                i++;
            }
        }
        
        return result.toString();
    }
    
    // Función combinada para cadenas: expandir variables y procesar escapes
    private String expandAndEscape(String text) {
        // Para cadenas: primero expandir variables (respetando \$), luego procesar otros escapes
        text = expandVariables(text);
        text = processEscapes(text);
        return text;
    }
%}

%%

{VARNAME}={STRING}\n? { 
    String text = yytext().replaceAll("\n$", "");
    String[] parts = text.split("=", 2);
    String value = parts[1];
    if (value.startsWith("\"") && value.endsWith("\"")) {
        value = value.substring(1, value.length() - 1);
        // Para cadenas, necesitamos manejo especial de escapes y variables
        value = expandAndEscape(value);
    }
    TablaSimbolos.put(parts[0], value);
    // No imprimir nada - eliminar la declaración y el salto de línea
}

{VARNAME}={COMPLEX_VALUE}\n? { 
    String text = yytext().replaceAll("\n$", "");
    String[] parts = text.split("=", 2);
    String value = parts[1];
    // Para valores complejos, procesar escapes y expandir variables
    value = processEscapes(value);
    value = expandVariables(value);
    TablaSimbolos.put(parts[0], value);
    // No imprimir nada - eliminar la declaración y el salto de línea
}

; { 
    // No imprimir punto y coma que separa declaraciones de variables
}

\${VARNAME} { 
    String var = yytext(); // Keep the $ prefix
    String value = TablaSimbolos.get(var);
    if (value == null) value = "";
    System.out.print(value);
}

{STRING} {
    String text = yytext();
    if (text.startsWith("\"") && text.endsWith("\"")) {
        text = text.substring(1, text.length() - 1);
        text = expandVariables(text);
        System.out.print("\"" + text + "\"");
    } else {
        System.out.print(yytext());
    }
}

. { 
    System.out.print(yytext()); 
}

\n { 
    System.out.print(yytext()); 
}
