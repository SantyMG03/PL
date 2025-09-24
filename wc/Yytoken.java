public class Yytoken {

    private String lexema;

    public Yytoken(String lexema) {
        this.lexema = lexema;
    }

    public String getLexema() {
        return lexema;
    }

    public String toString() {
         return "<"+lexema+">";
    }
}
