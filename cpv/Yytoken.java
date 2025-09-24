public class Yytoken {
    public static int A = 127;
    public static int B = 128;
    public static int C = 129;
    public static int D = 130;

    private int token;
    private int valor;

    public Yytoken(int token, int valor) {
        this.token = token;
        this.valor = valor;
    }

    public Yytoken(int token, String lexema) {
        this(token, Integer.parseInt(lexema));
    }

    public int getToken()  {
        return token;
    }

    public int getValor() {
         return valor;
    }

    @Override
    public String toString() {
         return "<"+token+","+valor+">";
    }
}