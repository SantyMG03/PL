import java.io.*;

public class wc {

    protected static int words = 0;     // Cuenta la cantidad de palabras
    protected static int lines = 0;     // Cuenta la cantidad de lineas
    protected static int c = 0;         // Cuenta la cantidad de caracteres

    public static int getWords() {
        return words;
    }

    public static int getLines() {
        return lines;
    }

    public static int getC() {
        return c;
    }

    public static void main(String arg[]) {
        try {
            Yylex lex = new Yylex(new FileReader(arg[0]));
            Yytoken yytoken = null;
            while ((yytoken = lex.yylex()) != null) {
                if (yytoken.getToken() == Yytoken.WORD) {
                    words++;
                    c += lex.yylength();
                }
                else if (yytoken.getToken() == Yytoken.EOL) lines++;
            } 
            System.out.println(getLines()+"\t"+getWords()+"\t"+getC());
        } catch (IOException e) {
        }
        
    }
}