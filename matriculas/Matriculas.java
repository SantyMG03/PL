import java.io.*;

public class Matriculas {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Uso: java Main <fichero>");
            System.exit(1);
        }

        try (FileReader fr = new FileReader(args[0])) {
            Yylex lexer = new Yylex(fr);
            String token;

            while ((token = lexer.yylex()) != null) {
                System.out.print(token);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Fichero no encontrado: " + args[0]);
        }
    }
}
