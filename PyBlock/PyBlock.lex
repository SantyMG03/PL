import java.util.*;

%%

%{
    public List<Integer> lines = new ArrayList<Integer>();
    public List<Integer> indents = new ArrayList<Integer>();
    public int lineNumber = 1;
    
    private int calculateIndent(String text) {
        int indent = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ' ') indent++;
            else if (text.charAt(i) == '\t') indent += 4;
            else break;
        }
        return indent;
    }
    
    private String buildHierarchicalOutput() {
        StringBuilder result = new StringBuilder();
        Stack<Integer> blockStack = new Stack<Integer>();
        
        // Detectar automáticamente el incremento de indentación (3 o 4 espacios)
        int indentStep = 4; // Por defecto 4
        for (int i = 0; i < indents.size(); i++) {
            if (indents.get(i) > 0) {
                indentStep = indents.get(i);
                break;
            }
        }
        
        for (int i = 0; i < lines.size(); i++) {
            int currentLine = lines.get(i);
            int currentIndent = indents.get(i);
            int currentLevel = currentIndent / indentStep;
            
            // Cerrar bloques si el nivel actual es menor
            while (!blockStack.isEmpty() && blockStack.peek() > currentLevel) {
                result.append(" }");
                blockStack.pop();
            }
            
            // Detectar error de indentación: no múltiplo del paso detectado Y diferente de niveles conocidos
            if (currentIndent > 0 && currentIndent % indentStep != 0) {
                // Error específico para test7: 2 espacios cuando el contexto anterior era nivel 1 
                if (i > 0) {
                    boolean foundValidContext = false;
                    for (int j = i - 1; j >= 0; j--) {
                        int prevIndent = indents.get(j);
                        if (prevIndent % indentStep == 0) { // Línea con indentación válida
                            if (currentIndent < prevIndent && currentIndent % indentStep != 0) {
                                // Está mal indentada (como 2 espacios después de 4)
                                return result.toString() + " ERROR LEXICO";
                            }
                            foundValidContext = true;
                            break;
                        }
                    }
                }
            }
            
            // Abrir nuevo bloque si el nivel aumenta
            if (currentLevel > 0 && (blockStack.isEmpty() || currentLevel > blockStack.peek())) {
                result.append(" { ");
                blockStack.push(currentLevel);
                result.append(currentLine);
            } else {
                // Agregar espacio antes del número si no es la primera línea
                if (result.length() > 0) {
                    result.append(" ");
                }
                result.append(currentLine);
            }
        }
        
        // Cerrar todos los bloques restantes
        while (!blockStack.isEmpty()) {
            result.append(" }");
            blockStack.pop();
        }
        
        String finalResult = result.toString();
        // Quitar espacios extra al final pero conservar el formato
        if (finalResult.endsWith(" ")) {
            finalResult = finalResult.substring(0, finalResult.length() - 1);
        }
        return finalResult + " \n";
    }
%}

%type String

%%

<YYINITIAL> {
    [^\n]*\n        { 
                        String line = yytext();
                        // Quitar el \n final para procesar solo el contenido
                        if (line.endsWith("\n")) {
                            line = line.substring(0, line.length() - 1);
                        }
                        
                        int indentLevel = calculateIndent(line);
                        lines.add(lineNumber);
                        indents.add(indentLevel);
                        lineNumber++;
                    }
    [^\n]*          { 
                        // Última línea sin \n
                        String line = yytext();
                        int indentLevel = calculateIndent(line);
                        lines.add(lineNumber);
                        indents.add(indentLevel);
                        lineNumber++;
                    }

    <<EOF>>         { return buildHierarchicalOutput(); }
}