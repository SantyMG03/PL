
public class rmv {
    public static void main(String arg[]) {
        if (arg.length>0) {
            try {
                Yylex.main(new String[]{arg[0]});
	            if (arg.length>1 && arg[1].equals("-debug")) {
	            	TablaSimbolos.dump();
	            }
	        } catch (Exception e) {
	        } 
        }
    }

}
