import java.io.*;

public class JCom {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Jcom <filename>");
            System.exit(1);
        }

        try {
            FileReader fr = new FileReader(args[0]);
            Yylex lexer = new Yylex(fr);
            lexer.yylex();
            System.out.println("//  " +lexer.getSingleLine());
            System.out.println("/*  " +lexer.getMultiLine());
            System.out.println("/** " +lexer.getJavadoc());
        } catch (IOException e) {
        }
    }
}