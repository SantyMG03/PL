import java.io.FileReader;
import java.io.IOException;

public class cpv {

    protected static int sumaA = 0;
    protected static int sumaB = 0;
    protected static int sumaC = 0;
    protected static int sumaD = 0;

    public static int getA() {
        return sumaA;
    }

    public static int getB() {
        return sumaB;
    }

    public static int getC() {
        return sumaC;
    }

    public static int getD() {
        return sumaD;
    }

    public static void main(String[] args) {
        try {
            Yylex lex = new Yylex(new FileReader(args[0]));
            Yytoken yytoken = null;

            while ((yytoken = lex.yylex()) != null) {
                if (yytoken.getToken() == Yytoken.A) sumaA++;
                else if (yytoken.getToken() == Yytoken.B) sumaB++;
                else if (yytoken.getToken() == Yytoken.C) sumaC++;
                else sumaD++;
            }

            System.out.println("A "+ getA());
            System.out.println("B "+ getB());
            System.out.println("C "+ getC());
            System.out.println("D "+ getD());
        } catch (IOException e) {
        }
    }
}