import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

class Parser {

// Tokens
public final static int EOF = Yylex.EOF;
public final static int NUMERO = Yylex.NUMERO ;         
public final static int MAS = Yylex.MAS ;
public final static int MENOS = Yylex.MENOS ;
public final static int POR = Yylex.POR ;
public final static int DIV = Yylex.DIV ;
public final static int AP = Yylex.AP; // Abre parentesis
public final static int CP = Yylex.CP; // Cierra parentesis
public final static int NOPER = Yylex.NOPER;
public final static int MAXPROF = Yylex.MAXPROF;
public final static int EVAL = Yylex.EVAL;

private final static int INDEFINIDO = -1;
    
private static int token; // token que se acaba de leer
private static int valor; // valor del token (si es un numero)
private static Yylex lex;
private static int contadorOperadores; // contador para nOper
private static int profundidadMaxima; // profundidad máxima para maxProf
private static int valorEvaluado; // valor evaluado para eval
private static int yylex() {
	int token = 0;
	try {
		token = lex.yylex();
        if (token == NUMERO) {
            valor = lex.yylval;
        } else {
            valor = INDEFINIDO;
        }
	} catch (IOException e) {
		System.out.println("ERROR");
		System.exit(0);
	}
	return token;
}

private void error() {
    System.out.println("ERROR");
    System.exit(0);
}

public static void main(String[] arg) {
    try {
        if (arg.length > 0) {
            lex = new Yylex(new FileReader(arg[0]));
        } else {
            lex = new Yylex(new InputStreamReader(System.in));
        }
        token = yylex();
        Parser parser = new Parser();
        boolean esNOper = parser.programa();
        if (token == EOF && !esNOper) {
            System.out.println("CORRECTO");
        } else if (token != EOF && !esNOper) {
            parser.error();
        }
    } catch (IOException e) {
        System.out.println("ERROR");
    }
}

// Método principal que decide entre expresión normal o función nOper/maxProf/eval
// S -> E | NOPER AP E CP | MAXPROF AP E CP | EVAL AP E CP
private boolean programa() {
    if (token == NOPER) {
        // Es nOper( <exp> )
        token = yylex(); // consumir NOPER
        if (token == AP) {
            token = yylex(); // consumir '('
            contadorOperadores = 0; // reiniciar contador
            expresion();
            if (token == CP) {
                token = yylex(); // consumir ')'
                if (token == EOF) {
                    System.out.println("NOPER=" + contadorOperadores);
                    return true; // indica que fue nOper
                } else {
                    error();
                }
            } else {
                error();
            }
        } else {
            error();
        }
    } else if (token == MAXPROF) {
        // Es maxProf( <exp> )
        token = yylex(); // consumir MAXPROF
        if (token == AP) {
            token = yylex(); // consumir '('
            int prof = calcularProfundidadMaxima();
            profundidadMaxima = prof > 1 ? prof - 2 : 0; // restar 2 para ajustar
            if (token == CP) {
                token = yylex(); // consumir ')'
                if (token == EOF) {
                    System.out.println("MAXPROF=" + profundidadMaxima);
                    return true; // indica que fue maxProf
                } else {
                    error();
                }
            } else {
                error();
            }
        } else {
            error();
        }
    } else if (token == EVAL) {
        // Es eval( <exp> )
        token = yylex(); // consumir EVAL
        if (token == AP) {
            token = yylex(); // consumir '('
            valorEvaluado = evaluarExpresion();
            if (token == CP) {
                token = yylex(); // consumir ')'
                if (token == EOF) {
                    System.out.println("EVAL=" + valorEvaluado);
                    return true; // indica que fue eval
                } else {
                    error();
                }
            } else {
                error();
            }
        } else {
            error();
        }
    } else {
        // Es una expresión normal
        expresion();
    }
    return false; // no fue nOper ni maxProf ni eval
}

// Método para reconocer una expresión en notación prefija
// E -> OPERADOR E E | NUMERO
private void expresion() {
    switch (token) {
        case MAS:
        case MENOS:
        case POR:
        case DIV:
            // Es un operador, debe seguir: operador expresion expresion
            contadorOperadores++; // incrementar contador
            token = yylex(); // consumir el operador
            expresion();     // primera expresión
            expresion();     // segunda expresión
            break;
        case NUMERO:
            // Es un número, expresión terminal
            token = yylex(); // consumir el número
            break;
        default:
            // Token no esperado
            error();
    }
}

// Método para evaluar una expresión y devolver su valor
private int evaluarExpresion() {
    switch (token) {
        case MAS:
            // Es suma: + expr1 expr2
            token = yylex(); // consumir el operador
            int val1_suma = evaluarExpresion(); // evaluar primera expresión
            int val2_suma = evaluarExpresion(); // evaluar segunda expresión
            return val1_suma + val2_suma;
            
        case MENOS:
            // Es resta: - expr1 expr2
            token = yylex(); // consumir el operador
            int val1_resta = evaluarExpresion(); // evaluar primera expresión
            int val2_resta = evaluarExpresion(); // evaluar segunda expresión
            return val1_resta - val2_resta;
            
        case POR:
            // Es multiplicación: * expr1 expr2
            token = yylex(); // consumir el operador
            int val1_mult = evaluarExpresion(); // evaluar primera expresión
            int val2_mult = evaluarExpresion(); // evaluar segunda expresión
            return val1_mult * val2_mult;
            
        case DIV:
            // Es división: / expr1 expr2
            token = yylex(); // consumir el operador
            int val1_div = evaluarExpresion(); // evaluar primera expresión
            int val2_div = evaluarExpresion(); // evaluar segunda expresión
            return val1_div / val2_div;
            
        case NUMERO:
            // Es un número, expresión terminal
            int valorNumero = valor; // obtener el valor del número
            token = yylex(); // consumir el número
            return valorNumero;
            
        default:
            // Token no esperado
            error();
            return 0;
    }
}

// Método para calcular la profundidad máxima de pila necesaria
private int calcularProfundidadMaxima() {
    switch (token) {
        case MAS:
        case MENOS:
        case POR:
        case DIV:
            // Es un operador, debe seguir: operador expresion expresion
            token = yylex(); // consumir el operador
            
            // Calcular profundidad de la primera expresión
            int prof1 = calcularProfundidadMaxima();
            
            // Calcular profundidad de la segunda expresión  
            int prof2 = calcularProfundidadMaxima();
            
            // Para un operador, la profundidad máxima es el máximo entre:
            // - prof1: profundidad máxima al evaluar primera expresión
            // - prof2 + 1: profundidad al evaluar segunda expresión + resultado de la primera
            // Luego sumamos 1 porque el resultado del operador también cuenta
            return Math.max(prof1, prof2 + 1) + 1;
            
        case NUMERO:
            // Es un número, expresión terminal
            token = yylex(); // consumir el número
            return 1; // un número ocupa un espacio en la pila
            
        default:
            // Token no esperado
            error();
            return 0;
    }
}

// Método para reconocer una expresión calculando la profundidad de pila
// E -> OPERADOR E E | NUMERO
private void expresionConProfundidad(int profundidadActual) {
    switch (token) {
        case MAS:
        case MENOS:
        case POR:
        case DIV:
            // Es un operador, debe seguir: operador expresion expresion
            token = yylex(); // consumir el operador
            
            // Para un operador, evaluamos la primera expresión
            expresionConProfundidad(profundidadActual);
            
            // Después de la primera expresión, tenemos un valor en la pila
            // Para la segunda expresión, la profundidad se incrementa
            expresionConProfundidad(profundidadActual + 1);
            
            // Actualizar profundidad máxima considerando que la segunda expresión 
            // necesita profundidad + 1
            if (profundidadActual + 1 > profundidadMaxima) {
                profundidadMaxima = profundidadActual + 1;
            }
            break;
        case NUMERO:
            // Es un número, expresión terminal
            // Actualizar profundidad máxima si es necesario
            if (profundidadActual > profundidadMaxima) {
                profundidadMaxima = profundidadActual;
            }
            token = yylex(); // consumir el número
            break;
        default:
            // Token no esperado
            error();
    }
}

}
