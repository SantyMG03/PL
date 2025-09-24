import java.io.*;

public class wc {
    public static void main(String arg[]) {
        Yylex lex = null;
        try {
            if (arg.length>0) {
                lex = new Yylex(new FileReader(arg[0]));
            } else {
                lex = new Yylex(new BufferedReader( new InputStreamReader( System.in )));
            }
            if (lex!=null) {
                Yytoken yytoken = null;
                int c = 0;
                while ((yytoken = lex.yylex()) != null  ) {
                    c++;
                }
                System.out.println(c);
            }
        } catch (IOException e) {
            System.out.println("Error al abrir el fichero de entrada");
        }
    }

}