import java.io.*;
import java.util.*;

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
                    int res = evaluate(line);
                    pw.println(res);
                } catch (ArithmeticException e) {
                    pw.println("Error: Division por cero");
                } catch (IllegalArgumentException e) {
                    pw.println("Error: Expresion invalida -> " + line);
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

    private static int evaluate(String line) {
        line = line.replaceAll("\\s+", ""); // Quita todos los espacios sobrantes

        List<String> l = infixToPostfix(line);

        return evalList(l);
    }

    private static List<String> infixToPostfix(String expr) {
        List<String> output = new ArrayList<>();
        Deque<Character> stack = new ArrayDeque<>();

        StringBuilder num = new StringBuilder();
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);

            if (Character.isDigit(c) || (c == '-' && (i == 0 || "+-*/(".indexOf(expr.charAt(i - 1)) >= 0))) {
                // parte de un número (incluye negativos)
                num.append(c);
            } else if ("+-*/".indexOf(c) >= 0) {
                if (num.length() > 0) {
                    output.add(num.toString());
                    num.setLength(0);
                }
                while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(c)) {
                    output.add(String.valueOf(stack.pop()));
                }
                stack.push(c);
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                if (num.length() > 0) {
                    output.add(num.toString());
                    num.setLength(0);
                }
                while (!stack.isEmpty() && stack.peek() != '(') {
                    output.add(String.valueOf(stack.pop()));
                }
                if (stack.isEmpty() || stack.pop() != '(') {
                    throw new IllegalArgumentException(); // parentesis desbalanceados
                }
            } else {
                throw new IllegalArgumentException();
            }
        }
        if (num.length() > 0) {
            output.add(num.toString());
        }
        while (!stack.isEmpty()) {
            char op = stack.pop();
            if (op == '(' || op == ')') {
                throw new IllegalArgumentException(); // parentesis desbalanceados
            }
            output.add(String.valueOf(op));
        }
        return output;
    }

    private static int evalList(List<String> tokens) {
        Deque<Integer> stack = new ArrayDeque<>();
        for (String token: tokens) {
            if (token.matches("-?\\d+")) {
                stack.push(Integer.parseInt(token));
            } else {
                int b = stack.pop();
                int a = stack.pop();
                switch (token) {
                    case "+": stack.push(a + b); break;
                    case "-": stack.push(a - b); break;
                    case "*": stack.push(a * b); break;
                    case "/":
                        if (b == 0) throw new ArithmeticException();
                        stack.push(a / b);
                        break;
                    default: throw new IllegalArgumentException();
                }
            }
        }
        return stack.pop();
    }

    private static int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        return 0;
    }
}