public class Yytoken {
    private int token;
    private String lexema;

    public Yytoken(int token, String lexema) {
         this.token = token;
         this.lexema = lexema;
    }

    public int getToken()  {
         return token;
    }

    public String getLexema() {
         return lexema;
    }

    public String toString() {
         return "<"+token+","+lexema+">";
    }
}