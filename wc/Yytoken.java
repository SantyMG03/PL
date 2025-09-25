public class Yytoken {

    public static int EOL = 127;
    public static int WORD = 128;

    private String lexema;
    private int token;

    public Yytoken(int token, String lexema) {
        this.lexema = lexema;
        this.token = token;
    }

    public int getToken() {
        return token;
    }

    public String getLexema() {
        return lexema;
    }

    public String toString() {
        return "<"+token+","+lexema+">";
    }
}
