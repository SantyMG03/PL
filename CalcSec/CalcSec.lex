import java.util.*;

%%

%int

Numero = [0-9]+
Operador = [\+\-\*\/]

%{
    List<Integer> lista = new LinkedList<>();
    String operador;

    public void operar(List pila, String op) {
        int a, b, auxa, auxb, res = 0;

        a = (int) pila.get(pila.size() - 2);
        b = (int) pila.get(pila.size() - 1);
        auxa = pila.size()-2;
        auxb = pila.size()-1;

        switch (op) {
            case "+":
                res = a + b;
                break;
            case "-":
                res = a - b;
                break;
            case "*":
                res = a * b;
                break;
            case "/":
                res = a / b;
                break;
            default:
                System.err.println("Error: operador:" + op);
                break;
        }
        pila.set(auxa, res);
        pila.remove(auxb);
    }
%}

%xstate OPERACION
%xstate PARENTESIS

%%

<YYINITIAL> {
    {Numero}    {   lista.add(Integer.parseInt(yytext()));}

    {Operador}  {   operador = yytext(); 
                    yybegin(OPERACION);}

    \(          {   yybegin(PARENTESIS);}

    \)          {   if(lista.size() > 1) {
                        operar(lista, "*");
                        };}

    \;          {   System.out.println(lista.get(0)); 
                    lista.clear();}

    [^]         {}
}

<OPERACION> {
    {Numero}    {   lista.add(Integer.parseInt(yytext()));
                    operar(lista, operador);
                    yybegin(YYINITIAL);}
}

<PARENTESIS> {
    {Numero}    {   lista.add(Integer.parseInt(yytext()));}

    {Operador}  {   operador = yytext(); 
                    yybegin(OPERACION);}
    
     \(          {   yybegin(PARENTESIS);}

    \)          {   if(lista.size() > 1) {
                        operar(lista, "*");
                        };}

    \;          {   System.out.println(lista.get(0)); 
                    lista.clear();}

    [^]         {}
}
