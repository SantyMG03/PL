import java.io.*;
import java.util.Scanner;

public class Calculadora {

    public static void main(String[] args) {
        InputStream inStream = System.in;
        OutputStream outStream = System.out;

        try {
            if (args.length >= 1) {
                inStream = new FileInputStream(args[0]);
            }

            if (args.length >= 2) {
                outStream = new FileOutputStream(args[1]);
            }

            Scanner sc = new Scanner(inStream);
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(outStream), true);

            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                try {
                    int res = eval(line);
                    pw.println(res);
                } catch (ArithmeticException e) {
                    pw.println("Division por cero");
                } catch (IllegalArgumentException e) {
                    pw.println("Expresion invalida -> " + line);
                }
            }

            sc.close();
            pw.flush();
            if (outStream != System.out) outStream.close();
            if (inStream != System.in) inStream.close();

        } catch (IOException e) {
            System.err.println("Error de E/S: " + e.getMessage());
        }
    }

    private static int eval(String line) {
        line = line.replaceAll("\\s+", "");

        String regex = "(-?\\d+)([+\\-*/])(-?\\d+)";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher m = p.matcher(line);

        if (!m.matches()) {
            throw new IllegalArgumentException();
        }

        int a = Integer.parseInt(m.group(1));
        String op = m.group(2);
        int b = Integer.parseInt(m.group(3));

        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/": {
                if (b == 0) throw new ArithmeticException();
                return a / b;
            }
            default: throw new IllegalArgumentException();
        }
    }
}