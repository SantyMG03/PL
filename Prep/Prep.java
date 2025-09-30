
import java.io.IOException;


public class Prep {
    public static void main (String[] args) {
        if (args.length != 1) {
            System.err.println("Uso: java Prep <fichero>");
            System.exit(1);
        }

        try {
            String file = args[0];
            Yylex lexer = new Yylex(new java.io.FileReader(file));
            String token;
            while ((token = lexer.yylex()) != null) {
                System.out.print(token);
            }
        } catch (IOException e) {
        }
    }
}