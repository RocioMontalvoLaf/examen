public class Errores {

	public static void noDeclarada(String id){
		
		PLXC.out.println("# ERROR: Variable '" + id + "' no ha sido declarada");
		PLXC.out.println("\terror;");
		PLXC.out.println("\thalt;\n");
		PLXC.out.println("# ---------------- listado de la tabla de simbolos --------------------\n");
		TablaSimbolos.imprimirValores();
		PLXC.out.println("\n# ---------------------------------------------------------------------");
		System.exit(0);
	}

	public static void varDeclarada(String id){
		 
		PLXC.out.println("# ERROR: Variable '" + id + "' ya ha sido declarada");
		PLXC.out.println("\terror;");
		PLXC.out.println("\thalt;\n");
		PLXC.out.println("# ---------------- listado de la tabla de simbolos --------------------\n");
		TablaSimbolos.imprimirValores();
		PLXC.out.println("\n# ---------------------------------------------------------------------");
		System.exit(0);
	}
	
	public static void noVar(String id){
		
		PLXC.out.println("# ERROR: Variable '" + id + "' no ha sido declarada");
		PLXC.out.println("\terror;");
		PLXC.out.println("\thalt;\n");
		PLXC.out.println("# ---------------- listado de la tabla de simbolos --------------------\n");
		TablaSimbolos.imprimirValores();
		PLXC.out.println("\n# ---------------------------------------------------------------------");
		System.exit(0);
	}
	
	public static void noTipo(){
		 
		PLXC.out.println("# ERROR: Error de tipos ");
		PLXC.out.println("\terror;");
		PLXC.out.println("\thalt;\n");
		PLXC.out.println("# ---------------- listado de la tabla de simbolos --------------------\n");
		TablaSimbolos.imprimirValores();
		PLXC.out.println("\n# ---------------------------------------------------------------------");
		System.exit(0);
	}
	
	public static void noFor(){
		PLXC.out.println("ERROR: Error de tipos en for");  
		PLXC.out.println("# ERROR: Error de tipos en for");
		PLXC.out.println("\terror;");
		PLXC.out.println("\thalt;\n");
		System.exit(0);
	}
	
	public static void noArray(){
		PLXC.out.println("ERROR: Las dimensiones de los arrays no encajan");  
		PLXC.out.println("# ERROR: Las dimensiones de los arrays no encajan");
		PLXC.out.println("\terror;");
		PLXC.out.println("\thalt;\n");
		System.exit(0);
	}
}
