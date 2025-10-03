import java.io.*;

public class PyBlock {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Uso: java Pyblock <archivo.py>");
            System.exit(1);
        }
        
        String fileName = args[0];
        
        try {
            // Crear el lexer
            Yylex lexer = new Yylex(new FileReader(fileName));
            
            // Procesar el archivo y obtener el resultado
            String result = lexer.yylex();
            
            // Mostrar el resultado
            System.out.print(result);
            
        } catch (FileNotFoundException e) {
            System.err.println("Error: No se pudo encontrar el archivo " + fileName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error de E/S: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
