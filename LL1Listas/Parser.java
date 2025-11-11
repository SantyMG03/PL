import java.io.FileReader;
import java.io.IOException;

class Parser {

    public final static int EOF = Yylex.EOF;
    public final static int NUMERO = Yylex.NUMERO ;         
    public final static int COMA = Yylex.COMA;
    public final static int AC = Yylex.AC; // Abre corchete
    public final static int CC = Yylex.CC; // Cierra corchete
    public final static int AP = Yylex.AP; // Abre parentesis
    public final static int CP = Yylex.CP; // Cierra parentesis
    public final static int NELEM = Yylex.NELEM;         
    public final static int MAXLENGTH = Yylex.MAXLENGTH;         
    public final static int MAXDEPTH = Yylex.MAXDEPTH;         
        
    private static int token;
    private static Yylex lex;
    private static int contadorElementos; // Variable para contar elementos
    private static int profundidadMaxima; // Variable para profundidad máxima
    private static int profundidadActual; // Variable para profundidad actual
    private static int longitudMaxima; // Variable para longitud máxima
    private static int yylex() {
        try {
            return lex.yylex();
        } catch (IOException e) {
            System.out.println("ERROR");
            System.exit(0);
            return EOF;
        }
    }

    public static void main(String[] arg) {
        if (arg.length>0) {
            try {
                lex = new Yylex(new FileReader(arg[0]));
                token = yylex();
                
                // Determinar el tipo de entrada
                if (token == NELEM) {
                    // Es una función nElem
                    if (parseNElem() && token == EOF) {
                        System.out.println("NELEM=" + contadorElementos);
                    } else {
                        System.out.println("ERROR");
                    }
                } else if (token == MAXDEPTH) {
                    // Es una función maxDepth
                    if (parseMaxDepth() && token == EOF) {
                        System.out.println("MAXDEPTH=" + profundidadMaxima);
                    } else {
                        System.out.println("ERROR");
                    }
                } else if (token == MAXLENGTH) {
                    // Es una función maxLength
                    if (parseMaxLength() && token == EOF) {
                        System.out.println("MAXLENGTH=" + longitudMaxima);
                    } else {
                        System.out.println("ERROR");
                    }
                } else if (token == AC) {
                    // Es una lista simple
                    if (parseLista() && token == EOF) {
                        System.out.println("CORRECTO");
                    } else {
                        System.out.println("ERROR");
                    }
                } else {
                    System.out.println("ERROR");
                }
            } catch (IOException e) {
                System.out.println("ERROR");
            } 
        } else {
            System.out.println("ERROR");
        }
    }

    /// .... A completar .....

    // Función principal para parsear una lista
    private static boolean parseLista() {
        return parseListaInterna(true); // Primera llamada reinicia contador
    }

    // Función auxiliar para parsear listas (con control del contador)
    private static boolean parseListaInterna(boolean reiniciarContador) {
        if (token == AC) { // '['
            token = yylex(); // consume '['
            if (reiniciarContador) {
                contadorElementos = 0; // Reiniciar contador solo en la lista principal
            }
            
            if (token == CC) { // ']' - lista vacía
                token = yylex(); // consume ']'
                return true;
            } else {
                // Lista con elementos
                if (parseElementos()) {
                    if (token == CC) { // ']'
                        token = yylex(); // consume ']'
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Función para parsear elementos (números o listas anidadas separados por comas)
    private static boolean parseElementos() {
        if (parseElemento()) {
            return parseRestElementos();
        }
        return false;
    }

    // Función para parsear un elemento individual (número o lista anidada)
    private static boolean parseElemento() {
        if (token == NUMERO) {
            contadorElementos++; // Incrementar contador para números
            token = yylex(); // consume NUMERO
            return true;
        } else if (token == AC) { // Lista anidada
            // NO incrementar contador para listas anidadas, solo parsearlas
            return parseListaInterna(false); // No reiniciar contador para listas anidadas
        }
        return false;
    }

    // Función para parsear el resto de elementos (coma y más elementos)
    private static boolean parseRestElementos() {
        if (token == COMA) { // ','
            token = yylex(); // consume ','
            if (parseElemento()) {
                return parseRestElementos(); // recursión
            }
            return false; // error: coma sin elemento después
        }
        // Si no hay coma, es el final de los elementos (épsilon)
        return true;
    }

    // Función para parsear nElem(lista)
    private static boolean parseNElem() {
        if (token == NELEM) {
            token = yylex(); // consume 'nElem'
            if (token == AP) { // '('
                token = yylex(); // consume '('
                if (parseLista()) {
                    if (token == CP) { // ')'
                        token = yylex(); // consume ')'
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Función para parsear maxDepth(lista)
    private static boolean parseMaxDepth() {
        if (token == MAXDEPTH) {
            token = yylex(); // consume 'maxDepth'
            if (token == AP) { // '('
                token = yylex(); // consume '('
                profundidadMaxima = 0; // Reiniciar profundidad máxima
                profundidadActual = 0; // Reiniciar profundidad actual
                if (parseListaConProfundidad()) {
                    if (token == CP) { // ')'
                        token = yylex(); // consume ')'
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Función para parsear maxLength(lista)
    private static boolean parseMaxLength() {
        if (token == MAXLENGTH) {
            token = yylex(); // consume 'maxLength'
            if (token == AP) { // '('
                token = yylex(); // consume '('
                longitudMaxima = 0; // Reiniciar longitud máxima
                if (parseListaConLongitud()) {
                    if (token == CP) { // ')'
                        token = yylex(); // consume ')'
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Función específica para parsear listas calculando profundidad
    private static boolean parseListaConProfundidad() {
        if (token == AC) { // '['
            profundidadActual++; // Incrementar profundidad al entrar en una lista
            if (profundidadActual > profundidadMaxima) {
                profundidadMaxima = profundidadActual; // Actualizar máximo
            }
            token = yylex(); // consume '['
            
            if (token == CC) { // ']' - lista vacía
                token = yylex(); // consume ']'
                profundidadActual--; // Decrementar al salir de la lista
                return true;
            } else {
                // Lista con elementos
                if (parseElementosConProfundidad()) {
                    if (token == CC) { // ']'
                        token = yylex(); // consume ']'
                        profundidadActual--; // Decrementar al salir de la lista
                        return true;
                    }
                }
            }
            profundidadActual--; // Decrementar en caso de error
        }
        return false;
    }

    // Función para parsear elementos calculando profundidad
    private static boolean parseElementosConProfundidad() {
        if (parseElementoConProfundidad()) {
            return parseRestElementosConProfundidad();
        }
        return false;
    }

    // Función para parsear un elemento individual calculando profundidad
    private static boolean parseElementoConProfundidad() {
        if (token == NUMERO) {
            token = yylex(); // consume NUMERO
            return true;
        } else if (token == AC) { // Lista anidada
            return parseListaConProfundidad(); // Parsear lista anidada
        }
        return false;
    }

    // Función para parsear resto de elementos calculando profundidad
    private static boolean parseRestElementosConProfundidad() {
        if (token == COMA) { // ','
            token = yylex(); // consume ','
            if (parseElementoConProfundidad()) {
                return parseRestElementosConProfundidad(); // recursión
            }
            return false; // error: coma sin elemento después
        }
        // Si no hay coma, es el final de los elementos (épsilon)
        return true;
    }

    // Variable temporal para la longitud actual de una lista
    private static int longitudActual;

    // Función específica para parsear listas calculando longitud
    private static boolean parseListaConLongitud() {
        if (token == AC) { // '['
            token = yylex(); // consume '['
            int longitudAnterior = longitudActual; // Guardar longitud anterior
            longitudActual = 0; // Reiniciar contador para esta lista
            
            if (token == CC) { // ']' - lista vacía
                token = yylex(); // consume ']'
                if (longitudActual > longitudMaxima) {
                    longitudMaxima = longitudActual; // Actualizar máximo
                }
                longitudActual = longitudAnterior; // Restaurar longitud anterior
                return true;
            } else {
                // Lista con elementos
                if (parseElementosConLongitud()) {
                    if (token == CC) { // ']'
                        token = yylex(); // consume ']'
                        longitudActual = longitudAnterior; // Restaurar longitud anterior
                        return true;
                    }
                }
            }
            longitudActual = longitudAnterior; // Restaurar en caso de error
        }
        return false;
    }

    // Función para parsear elementos calculando longitud
    private static boolean parseElementosConLongitud() {
        // Contar el primer elemento
        longitudActual++;
        if (longitudActual > longitudMaxima) {
            longitudMaxima = longitudActual; // Actualizar máximo
        }
        if (parseElementoConLongitud()) {
            return parseRestElementosConLongitud();
        }
        return false;
    }

    // Función para parsear un elemento individual calculando longitud
    private static boolean parseElementoConLongitud() {
        if (token == NUMERO) {
            token = yylex(); // consume NUMERO
            return true;
        } else if (token == AC) { // Lista anidada
            return parseListaConLongitud(); // Parsear lista anidada
        }
        return false;
    }

    // Función para parsear resto de elementos calculando longitud
    private static boolean parseRestElementosConLongitud() {
        if (token == COMA) { // ','
            longitudActual++; // Incrementar contador de elementos
            if (longitudActual > longitudMaxima) {
                longitudMaxima = longitudActual; // Actualizar máximo
            }
            token = yylex(); // consume ','
            if (parseElementoConLongitud()) {
                return parseRestElementosConLongitud(); // recursión
            }
            return false; // error: coma sin elemento después
        }
        // Si no hay coma, actualizar máximo con la longitud actual
        if (longitudActual > longitudMaxima) {
            longitudMaxima = longitudActual;
        }
        return true;
    }

}