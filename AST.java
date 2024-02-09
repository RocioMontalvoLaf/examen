
public class AST {	
	public String raiz;   
	public AST izq;
	public AST der;
	public String v;
	public String f;
	
	public AST (String r, AST i, AST d){
		raiz = r;
		izq = i;
		der = d;
	}
	
	public String gc(){
		String res = "";
		String left = "";
		String right = "";
		String temp = "";
		String aux = "";
		String et = "";
		String et1 = "";
		String et2 = "";
		String s = "";
		TablaSimbolos.Tipo tipo;

		switch(raiz) {
			case "ini":
				izq.gc();
				break;
			case "sent":
				izq.gc();
				if(der != null){
					der.gc();
				}
				break;
			case "printcond":
				izq.gc();
				et = izq.v; //et si se cumple la condición
				et1 = izq.f;	//et si no se cumple la condición
				et2 = Generador.nuevaEtiqueta(); //et final y salida
				PLXC.out.println(et + ":");//se cumple la condicion
				PLXC.out.println("\twritec 116;\n\twritec 114;\n\twritec 117;\n\twritec 101;\n\twritec 10;");
				PLXC.out.println("\tgoto " + et2 + ";");
				PLXC.out.println(et1 + ":");//no se cumple la condicion
				PLXC.out.println("\twritec 102;\n\twritec 97;\n\twritec 108;\n\twritec 115;\n\twritec 101;\n\twritec 10;");
				PLXC.out.println(et2 + ":");//salida
				break;

			case "print":
				left = izq.gc();
				if (izq.raiz.equals("cadena")){
					left = "$" + Generador.getCurrentVariable();					
				}
				if (TablaSimbolos.tipo(left) == TablaSimbolos.Tipo.STRING){
					temp = "$" + Generador.getCurrentVariable(); //$t0 (array de Char	)
					String ltemp = temp; 
					if(!izq.raiz.equals("cadena")){
						temp = left;
						ltemp = "$" + temp;
					}
					String v1 = "$" + Generador.nuevaVariable(); //$t1
					String v2 = "$" + Generador.nuevaVariable(); //$t2
					et = Generador.nuevaEtiqueta(); //L0
					et1 = Generador.nuevaEtiqueta(); //L1
					et2 = Generador.nuevaEtiqueta(); //L2
					PLXC.out.println("\t" + v1 + " = 0;");
					PLXC.out.println(et + ":");
					PLXC.out.println("\tif (" + v1 + " < " + ltemp + "_length) goto " + et1 + ";");
					PLXC.out.println("\tgoto " + et2 + ";");
					PLXC.out.println(et1 + ":");
					PLXC.out.println("\t" + v2 + " = " + temp + "[" + v1 + "];");
					PLXC.out.println("\twritec " + v2 + ";");
					PLXC.out.println("\t" + v1 + " = " + v1 + " + 1;");
					PLXC.out.println("\tgoto " + et + ";");
					PLXC.out.println(et2 + ":");
					PLXC.out.println("\twritec 10;");
				}else if (TablaSimbolos.tipo(left) == TablaSimbolos.Tipo.BOOLEAN){	
					et = Generador.nuevaEtiqueta(); //et si se cumple la condición
					et1 = Generador.nuevaEtiqueta();	//et si no se cumple la condición
					et2 = Generador.nuevaEtiqueta(); //et final y salida	
					PLXC.out.println("\tif (" + left + " == 1) goto " + et + ";");
					PLXC.out.println("\tgoto " + et1 + ";");
					PLXC.out.println(et + ":");//se cumple la condicion
					PLXC.out.println("\twritec 116;\n\twritec 114;\n\twritec 117;\n\twritec 101;\n\twritec 10;");
					PLXC.out.println("\tgoto " + et2 + ";");
					PLXC.out.println(et1 + ":");//no se cumple la condicion
					PLXC.out.println("\twritec 102;\n\twritec 97;\n\twritec 108;\n\twritec 115;\n\twritec 101;\n\twritec 10;");
					PLXC.out.println(et2 + ":");//salida
				}else{
					aux = "print";
					if (TablaSimbolos.tipo(left) == TablaSimbolos.Tipo.ARRAY_CHAR || izq.raiz.equals("castChar")){
						aux += "c";
					}
					if(TablaSimbolos.esArray(left)){
						temp = Generador.nuevaVariable();	
						for (int i = 0; i < TablaSimbolos.getTamanio(left); i++){
							PLXC.out.println("\t" + "$" + temp + " = " + left + "[" + i + "];");
							PLXC.out.println("\t"+ aux + " $" + temp + ";");
						}
					}else{
						if(!TablaSimbolos.estaIdent(left)){
							Errores.noDeclarada(left);
						}else{
							if(TablaSimbolos.tipo(left) == TablaSimbolos.Tipo.CHAR || izq.raiz.equals("castChar")){
								PLXC.out.println("\tprintc " + left + ";");
							}else{
								PLXC.out.println("\tprint " + left + ";");
							}			
						}
					}
				}
				break;
			case "printArray":
				temp = Generador.nuevaVariable();
				left = izq.gc();
				tipo = TablaSimbolos.tipo(left);
				String temp2 = "$" + Generador.nuevaVariable();
				TablaSimbolos.insertar(temp2, tipo);
				int index = Generador.getCurrentIndex();
				for (int i = 0; i < index; i++){
					PLXC.out.println("\t" + temp2 + " = " + temp + "[" + i + "];");
					if(tipo == TablaSimbolos.Tipo.CHAR){
						PLXC.out.println("\tprintc " + temp2 + ";");
					}else{
						PLXC.out.println("\tprint " + temp2 + ";");
					}
				}
				Generador.resetIndex();
				break;
			case "pArrayIni":
				temp = "$" + Generador.getCurrentVariable();
				left = izq.gc(); //valor a asignar
				aux = Generador.getindex(); //indice actual
				PLXC.out.println("\t" + temp + "[" + aux + "] = " + left + ";");
				TablaSimbolos.insertar(temp, TablaSimbolos.tipo(left));
				if (der != null) {
					der.gc(); //siguiente valor
				}
				res = temp;
				break;
			case "cadena":
				right = der.raiz;
				temp = "$" + Generador.nuevaVariable();
				TablaSimbolos.insertar(temp, TablaSimbolos.Tipo.STRING);
				right = limpiaCadena(right);
				for (int i = 0; i < right.length(); i++){
					aux = String.valueOf((int) right.charAt(i));
					PLXC.out.println("\t" + temp + "[" + i + "] = " + aux + ";");
					TablaSimbolos.declararTamanio(temp, i+1);					
				}
				PLXC.out.println("\t" + temp + "_length = " + TablaSimbolos.getTamanio(temp) + ";");
				break;

			case "num":
				res += izq.raiz;
				if(!TablaSimbolos.estaIdent(res)){
					TablaSimbolos.insertar(res, TablaSimbolos.Tipo.INT);
				}
				break;
			case "real":
				res += izq.raiz;
				if(!TablaSimbolos.estaIdent(res)){
					TablaSimbolos.insertar(res, TablaSimbolos.Tipo.FLOAT);
				}
				break;
			case "castChar":
				aux = izq.gc();
				res += aux;
				//TablaSimbolos.setTipo(aux, TablaSimbolos.Tipo.CHAR);
				break;	
			case "castInt":
				aux = izq.gc();
				aux = ("(int) " + aux);
				v = Generador.nuevaVariable();
				PLXC.out.println("\t$" + v + " = " + aux + ";");
				res = "$" + v;
				TablaSimbolos.insertar(res, TablaSimbolos.Tipo.FLOAT);
				break;
			case "castFloat":
				aux = izq.gc();
				aux = ("(float) " + aux);
				v = Generador.nuevaVariable();
				PLXC.out.println("\t$" + v + " = " + aux + ";");
				res = "$" + v;
				TablaSimbolos.insertar(res, TablaSimbolos.Tipo.FLOAT);							
				break;
			case "length":
				aux = izq.raiz;
				if(TablaSimbolos.tipo(aux) == TablaSimbolos.Tipo.ARRAY_CHAR 
					|| TablaSimbolos.tipo(aux) == TablaSimbolos.Tipo.ARRAY_INT 
					|| TablaSimbolos.tipo(aux) == TablaSimbolos.Tipo.ARRAY_FLOAT){
					res = "$" + aux + "_length";
					TablaSimbolos.insertar(res, TablaSimbolos.Tipo.INT);
				}
				break;
			case "arrayInt":
				aux = izq.raiz;
				Integer tam = Integer.parseInt(der.raiz);				
				if(!TablaSimbolos.estaIdent(aux)){
					TablaSimbolos.insertar(aux, TablaSimbolos.Tipo.ARRAY_INT);
					TablaSimbolos.declararTamanio(aux, tam);
				}else{
					Errores.varDeclarada(aux);
				}
				PLXC.out.println("\t" + "$" + aux + "_length = " + tam + ";");
				break;
			case "arrayChar":
				aux = izq.raiz;
				tam = Integer.parseInt(der.raiz);
				if(!TablaSimbolos.estaIdent(aux)){
					TablaSimbolos.insertar(aux, TablaSimbolos.Tipo.ARRAY_CHAR);
					TablaSimbolos.declararTamanio(aux, tam);
				}else{
					Errores.varDeclarada(aux);
				}
				PLXC.out.println("\t" + "$" + aux + "_length = " + tam + ";");
				break;
			case "arrayFloat":
				aux = izq.raiz;
				tam = Integer.parseInt(der.raiz);
				if(!TablaSimbolos.estaIdent(aux)){
					TablaSimbolos.insertar(aux, TablaSimbolos.Tipo.ARRAY_FLOAT);
					TablaSimbolos.declararTamanio(aux, tam);
				}else{
					Errores.varDeclarada(aux);
				}
				PLXC.out.println("\t" + "$" + aux + "_length = " + tam + ";");
				break;	
			case "ascii":
				res += izq.raiz;
				if(!(res.charAt(0) == '\\' && res.charAt(1)=='u')){ //ASCII
					if(res.length() == 1){
						s = String.valueOf((int) res.charAt(0));
					}else{
						char especial = res.charAt(0);
						switch(especial){
							case 'n':
								s = "10";
								break;
							case 't':
								s = "9";
								break;
							case 'r':
								s = "13";
								break;
							case '\\':
								s = "92";
								break;
							case '\'':
								s = "39";
								break;
							case '\"':
								s = "34";
								break;
							case 'f':
								s = "12";
								break;
							case 'b':
								s = "8";
								break;
						}
					}
				}else { //UNICODE
					s = Integer.decode("0x" + res.substring(2,6)).toString();
				} 
				if (!TablaSimbolos.estaIdent(s)){
					TablaSimbolos.insertar(s, TablaSimbolos.Tipo.CHAR);
				/* }else if (TablaSimbolos.tipo(s) == TablaSimbolos.Tipo.CHAR){
					Errores.varDeclarada(s); */
				}else {
					TablaSimbolos.setTipo(s, TablaSimbolos.Tipo.CHAR);
				}
				res = s;
				break;
			case "por":
				left = izq.gc();
				right = der.gc();
				tipo = TablaSimbolos.tipo(left);
				temp = Generador.nuevaVariable();
				if (tipo == TablaSimbolos.Tipo.FLOAT || TablaSimbolos.tipo(right) == TablaSimbolos.Tipo.FLOAT){
					tipo = TablaSimbolos.Tipo.FLOAT;
					TablaSimbolos.setTipo(left, TablaSimbolos.Tipo.FLOAT);
					TablaSimbolos.setTipo(right, TablaSimbolos.Tipo.FLOAT);
					PLXC.out.println("\t$" + temp + " = "+ left + " *r " + right + ";" );
				}else{
					PLXC.out.println("\t$" + temp + " = " + left + " * " + right + ";" );}
				if (tipo == TablaSimbolos.Tipo.CHAR){
					tipo = TablaSimbolos.Tipo.INT;
				}				
				TablaSimbolos.insertar("$" + temp, tipo);
				res += "$" + temp;				
				break;
			case "mas":
				left = izq.gc();
				right = der.gc();
				temp = Generador.nuevaVariable();				
				if((der.raiz.equals("castInt") || izq.raiz.equals("castInt"))){
					tipo = TablaSimbolos.Tipo.INT;
				}else{
					tipo = resuelveTipo(right, left);
				}
				TablaSimbolos.setTipo(left, tipo);
				TablaSimbolos.setTipo(right, tipo);
				if(tipo == TablaSimbolos.Tipo.FLOAT){
					PLXC.out.println("\t$" + temp + " = "+ left + " +r " + right + ";" );
				}else{
					PLXC.out.println("\t$" + temp + " = " + left + " + " + right + ";" );
				}
				TablaSimbolos.insertar("$" + temp, tipo);
				res += "$" + temp;				
				break;
			case "menos":
				left = izq.gc();
				right = der.gc();
				tipo = TablaSimbolos.tipo(left);
				temp = Generador.nuevaVariable();
				if (tipo == TablaSimbolos.Tipo.FLOAT || TablaSimbolos.tipo(right) == TablaSimbolos.Tipo.FLOAT){
					tipo = TablaSimbolos.Tipo.FLOAT;
					TablaSimbolos.setTipo(left, TablaSimbolos.Tipo.FLOAT);
					TablaSimbolos.setTipo(right, TablaSimbolos.Tipo.FLOAT);
					PLXC.out.println("\t$" + temp + " = "+ left + " -r " + right + ";" );
				}else{
					PLXC.out.println("\t$" + temp + " = " + left + " - " + right + ";" );
				}	
				if (tipo == TablaSimbolos.Tipo.CHAR){
					tipo = TablaSimbolos.Tipo.INT;
				}			
				TablaSimbolos.insertar("$" + temp, tipo);
				res += "$" + temp;				
				break;
			case "menosUnario":
				aux = izq.gc();
				left = "-" + aux;
				temp = Generador.nuevaVariable();
				PLXC.out.println("\t$" + temp + " = " + left + ";");
				TablaSimbolos.insertar("$" + temp, TablaSimbolos.tipo(aux));
				res += "$" + temp;
				break;
			case "asig":
				left = izq.raiz;
				right = der.gc();
				if(der.raiz.equals("asigBool")){
					v = Generador.nuevaEtiqueta();
					f = Generador.nuevaEtiqueta();
					et = Generador.nuevaEtiqueta(); // etiqueta de salida
					PLXC.out.println("\tif ( 1 == " + right + ") goto " + v + ";");
					PLXC.out.println("\tgoto " + f + ";");
					PLXC.out.println(v + ":");
					PLXC.out.println("\t" + left + " = 1;");
					PLXC.out.println("\tgoto " + et + ";");
					PLXC.out.println(f + ":");
					PLXC.out.println("\t" + left + " = 0;");
					PLXC.out.println(et + ":");
					res = left;
				}else{ 
					if (!TablaSimbolos.estaIdent(left)){
						Errores.noDeclarada(left);
					}
					if (TablaSimbolos.tipo(left) != TablaSimbolos.tipo(right) && !comprobarCasteo(left, der.raiz, right)){
						Errores.noTipo(); //No hace la comprobación en caso de booleano
					} else {
						PLXC.out.println("\t" + left + " = " + right + ";");
					} if (!der.raiz.equals("asig")){
						res = right;
					} else {
						res = left;
					}	
				}			
				break;
			case "arrayAsig":
				left = izq.gc(); //identificador
				if(left.equals("")){
					left = izq.raiz;
				}			
				aux = izq.izq.gc(); //indice
				if(aux.equals("")){
					aux = izq.izq.raiz;
				}
				tipo = TablaSimbolos.tipo(left);
				v = Generador.nuevaEtiqueta();
				f = Generador.nuevaEtiqueta();
				PLXC.out.println("\tif (" + aux + " < 0) goto " + v + ";");
				PLXC.out.println("\tif ("+ TablaSimbolos.getTamanio(left) + " < " + aux + ") goto " + v + ";");
				PLXC.out.println("\tif ("+ TablaSimbolos.getTamanio(left) + " == " + aux + ") goto " + v + ";");
				PLXC.out.println("\tgoto " + f + ";");
				PLXC.out.println(v + ":");
				PLXC.out.println("\terror;\n\thalt;");
				PLXC.out.println(f + ":");
				right = der.gc(); //valor
				if(TablaSimbolos.tipo(left) == TablaSimbolos.Tipo.ARRAY_FLOAT && TablaSimbolos.tipo(right) == TablaSimbolos.Tipo.INT){
					right = "(float) " + right;
					temp = Generador.nuevaVariable();
					PLXC.out.println("\t$" + temp + " = " + right + ";");
					right = "$" + temp;
					TablaSimbolos.insertar(right, TablaSimbolos.Tipo.FLOAT);
				}
				if(comprobarTipoArray(tipo, right)){
					PLXC.out.println("\t" + left + "[" + aux + "] = " + right + ";");
				}
				break;
			case "arrayPos":
				left = izq.raiz; //identificador");
				right = der.gc(); //indice
				if (right.equals("")){
					right = der.raiz;
				}
				temp = Generador.nuevaVariable();
				v = Generador.nuevaEtiqueta();
				f = Generador.nuevaEtiqueta();
				tipo = resuelveTipo(TablaSimbolos.tipo(left));
				PLXC.out.println("\tif (" + right + " < 0) goto " + v + ";");
				PLXC.out.println("\tif ("+ TablaSimbolos.getTamanio(left) + " < " + right + ") goto " + v + ";");
				PLXC.out.println("\tif ("+ TablaSimbolos.getTamanio(left) + " == " + right + ") goto " + v + ";");
				PLXC.out.println("\tgoto " + f + ";");
				PLXC.out.println(v + ":");
				PLXC.out.println("\terror;\n\thalt;");
				PLXC.out.println(f + ":");
				PLXC.out.println("\t" + "$" + temp + " = " + left + "[" + right + "];");
				TablaSimbolos.insertar("$" + temp, tipo);
				res += "$" + temp;
				break;
			case "iniArray":
				left = izq.raiz; //identificador
				temp = Generador.getVarArray();
				der.gc();
				aux = Generador.getVarArray();
				for (int i = 0; i < Generador.getCurrentIndex(); i++){
					PLXC.out.println("\t" + "$" + aux + " = " + "$" + temp + "[" + i + "];");
					PLXC.out.println("\t" + left + "[" + i + "] = $" + aux + ";");
				}
				Generador.resetIndex();
				break;
			case "arrayIni":
				temp = Generador.getCurrentVarArray();
				left = izq.gc(); //valor a asignar	
				PLXC.out.println("\t" + "$" + temp + "[" + Generador.getindex() + "]" + " = " + left + ";");
				if(der != null) der.gc();						
				break;
			case "iniAsigArrayCh":
				izq.gc();
				der.gc();
				break;
			case "div":
				right = der.gc();
				left = izq.gc();
				tipo = TablaSimbolos.tipo(left);
				temp = Generador.nuevaVariable();
				if (tipo == TablaSimbolos.Tipo.FLOAT || TablaSimbolos.tipo(right) == TablaSimbolos.Tipo.FLOAT){
					tipo = TablaSimbolos.Tipo.FLOAT;
					TablaSimbolos.setTipo(left, TablaSimbolos.Tipo.FLOAT);
					TablaSimbolos.setTipo(right, TablaSimbolos.Tipo.FLOAT);
					PLXC.out.println("\t$" + temp + " = "+ left + " /r " + right + ";" );
				}else{
					PLXC.out.println("\t$" + temp + " = " + left + " / " + right + ";" );
				}
				if (tipo == TablaSimbolos.Tipo.CHAR){
					tipo = TablaSimbolos.Tipo.FLOAT;
				}			
				TablaSimbolos.insertar("$" + temp, tipo);
				res += "$" + temp;				
				break;
			case "ident":
				res += izq.raiz;
				break;
			case "if":
				izq.gc();				
				if(izq.raiz.equals("implica")||izq.raiz.equals("forall")){
					if (der != null){
						der.v = izq.v;
						der.f = izq.f;
						der.gc();
					}
				}else{
					PLXC.out.println( izq.v + ":");
					if (der != null){
						der.v = izq.v;
						der.f = izq.f;
						der.gc();
					}
				}								
				break;
			case "forall":
				v = Generador.nuevaEtiqueta();
				f = Generador.nuevaEtiqueta();
				PLXC.out.println("\t" + izq.raiz + " = 0;");
				PLXC.out.println(v + ":");
				der.gc();
				String derv, derf;
				derv = der.v;
				derf = der.f;
				PLXC.out.println(derv + ":");
				PLXC.out.println("\tif (1 == " + izq.raiz + ") goto " + f + ";");
				PLXC.out.println("\t" + izq.raiz + " = 1;");
				PLXC.out.println("\tgoto " + v + ";");
				PLXC.out.println(f + ":");
				v = derv;
				f = derf;
				break;
			case "forallInt":
				left = izq.raiz; //identificador
				String from, to;
				from = izq.izq.gc(); //desde
				to = izq.der.gc(); //hasta
				TablaSimbolos.insertar(left, TablaSimbolos.Tipo.INT);
				PLXC.out.println("\t" + left + " = " + from + ";");
				et = Generador.nuevaEtiqueta();
				PLXC.out.println(et + ":");
				et1 = Generador.nuevaEtiqueta();
				PLXC.out.println("\tif (" + to + " < " + left + ") goto " + et1 + ";");
				der.gc();
				PLXC.out.println(der.v + ":");
				PLXC.out.println("\t" + left + " = " + left + " + 1;");
				PLXC.out.println("\tgoto " + et + ";");
				v = et1;
				f = der.f;				
				break;
			case "forallIntStep":
				left = izq.raiz; //identificador
				String from1, to1, step;
				from1 = izq.izq.gc(); //desde
				to1 = izq.der.gc(); //hasta
				TablaSimbolos.insertar(left, TablaSimbolos.Tipo.INT);
				PLXC.out.println("\t" + left + " = " + from1 + ";");
				et = Generador.nuevaEtiqueta();
				PLXC.out.println(et + ":");
				et1 = Generador.nuevaEtiqueta();
				PLXC.out.println("\tif (" + to1 + " < " + left + ") goto " + et1 + ";");
				der.der.gc();
				PLXC.out.println(der.der.v + ":");
				step = der.izq.gc(); //step 
				PLXC.out.println("\t" + left + " = " + left + " + " + step + ";");
				PLXC.out.println("\tgoto " + et + ";");
				v = et1;	
				f = der.der.f;	
				break;
			case "igual":
				left = izq.gc();
				right = der.gc();
				v = Generador.nuevaEtiqueta();
				f = Generador.nuevaEtiqueta();
				PLXC.out.println("\tif (" + left + " == " + right + ") goto " + v + ";");
				PLXC.out.println("\tgoto " + f + ";");
				break;
			case "bool":
				left = izq.gc();
				if (left == null || left.equals("")){
					left = izq.raiz;
				}				
				if (!TablaSimbolos.estaIdent(left)){
					TablaSimbolos.insertar(left, TablaSimbolos.Tipo.BOOLEAN);
				}
				v = Generador.nuevaEtiqueta();
				f = Generador.nuevaEtiqueta();
				PLXC.out.println("\tif ( 1 == " + left + ") goto " + v + ";");
				PLXC.out.println("\tgoto " + f + ";");				
				break;
			case "boolnot":
				izq.gc();
				v = izq.f;
				f = izq.v;
				break;
			case "else":
				String l = Generador.nuevaEtiqueta();
				izq.gc();
				PLXC.out.println("\tgoto " + l + ";");
				PLXC.out.println( f + ":");
				if(der!=null){
					der.gc();
				}
				PLXC.out.println( l + ":");
				break;
			case "menorIgual":
				left = izq.gc();
				right = der.gc();
				v = Generador.nuevaEtiqueta();
				f = Generador.nuevaEtiqueta();
				PLXC.out.println("\tif (" + right + " < " + left + ") goto " + f +";");
				PLXC.out.println("\tgoto " + v + ";");
				break;
			case "mayorIgual":
				left = izq.gc();
				right = der.gc();
				v = Generador.nuevaEtiqueta();
				f = Generador.nuevaEtiqueta();
				PLXC.out.println("\tif (" + left + " < " + right + ") goto " + f +";");
				PLXC.out.println("\tgoto " + v + ";");
				break;
			case "distinto":
				left = izq.gc();
				right = der.gc();
				v = Generador.nuevaEtiqueta();
				f = Generador.nuevaEtiqueta();
				PLXC.out.println("\tif (" + left + " == " + right + ") goto " + f + ";");
				PLXC.out.println("\tgoto " + v + ";");
				break;
			case "and":
				izq.gc();
				PLXC.out.println(izq.v + ":");
				der.gc();
				PLXC.out.println(izq.f + ":");
				PLXC.out.println("\tgoto " + der.f + ";");
				v = der.v;
				f = der.f;
				break;
			case "or":
				izq.gc();
				PLXC.out.println(izq.f + ":");
				der.gc();
				PLXC.out.println(izq.v + ":");
				PLXC.out.println("\tgoto " + der.v + ";");
				v = der.v;
				f = der.f;
				break;
			case "implica":
				izq.gc();
				if (izq.raiz.equals("true") || izq.raiz.equals("false")){
					izq.v = Generador.nuevaEtiqueta();
					izq.f = Generador.nuevaEtiqueta();
				}
				PLXC.out.println(izq.v + ":");
				der.gc();
				if (der.raiz.equals("true") || der.raiz.equals("false")){
					der.v = Generador.nuevaEtiqueta();
					der.f = Generador.nuevaEtiqueta();
				}
				if(!der.raiz.equals("implica")){
					PLXC.out.println(der.v + ":");
				}
				PLXC.out.println("\tgoto " + izq.f + ";");
				PLXC.out.println(izq.f + ":");
				f = der.f;
				v = der.v;
				break;
			case "not":
				izq.gc();
				v = izq.f;
				f = izq.v;
				if(v == null && f == null){ //Si no se ha asignado etiqueta
					v = Generador.nuevaEtiqueta();
					f = Generador.nuevaEtiqueta();
				}
				break;
			case "mayor":
				left = izq.gc();
				right = der.gc();
				v = Generador.nuevaEtiqueta();
				f = Generador.nuevaEtiqueta();
				PLXC.out.println("\tif (" + right + " < " + left + ") goto " + v + ";");
				PLXC.out.println("\tgoto " + f + ";");
				break;
			case "menor":
				left = izq.gc();
				right = der.gc();
				v = Generador.nuevaEtiqueta();
				f = Generador.nuevaEtiqueta();
				PLXC.out.println("\tif (" + left + " < " + right + ") goto " + v + ";");
				PLXC.out.println("\tgoto " + f + ";");
				break;
			case "int":
				izq.gc();
				break;
			case "char":
				izq.gc();
				break;
			case "float":
				izq.gc();
				break;	
			case "string":
				left = izq.gc();
				TablaSimbolos.insertar(left, TablaSimbolos.Tipo.STRING);
				break;
			case "boolean":
				left = izq.gc();
				TablaSimbolos.insertar(left, TablaSimbolos.Tipo.BOOLEAN);
				break;
			case "boolIdent":
				right = der.raiz;
				TablaSimbolos.insertar(der.raiz, TablaSimbolos.Tipo.BOOLEAN);
				if (izq != null){
					izq.gc();
				}
				break;
			case "floatIdent":
				TablaSimbolos.insertar(der.raiz, TablaSimbolos.Tipo.FLOAT);
				if (izq != null){
					izq.gc();
				}
				break;
			case "asigString":
				left = izq.raiz; //identificador (a)
				String left_length = "$" + left + "_length";
				TablaSimbolos.insertar(left, TablaSimbolos.Tipo.STRING);	
				izq.izq.gc();
				temp = "$" + Generador.getCurrentVariable(); //$t0
				aux = "$" + Generador.nuevaVariable(); //$t1
				String aux2 = "$" + Generador.nuevaVariable(); //$t2
				PLXC.out.println("# Asignar array " + left + " <- " + temp);
				PLXC.out.println("\t" + "$" + left + "_length = 0;");
				PLXC.out.println("\t" + aux + " = 0;");
				et = Generador.nuevaEtiqueta(); //L0
				et1 = Generador.nuevaEtiqueta(); //L1
				et2 = Generador.nuevaEtiqueta(); //L2
				PLXC.out.println(et + ":");
				PLXC.out.println("\tif (" + aux + " < " + temp + "_length) goto " + et1 + ";");
				PLXC.out.println("\tgoto " + et2 + ";");
				PLXC.out.println(et1 + ":");
				PLXC.out.println("\t" + aux2 + " = " + temp + "["+aux+"];");
				PLXC.out.println("\t" + left + "[" + left_length + "] = " + aux2 + ";");
				PLXC.out.println("\t" + left_length + " = " + left_length + " + 1;");
				PLXC.out.println("\t" + aux + " = " + aux + " + 1;");
				PLXC.out.println("\tgoto " + et + ";");
				PLXC.out.println(et2 + ":");
				TablaSimbolos.sustituirKeyTamanio(left, temp);
				res = left;
				if (der != null) der.gc();
				break;

			case "asigChar":
				right = der.raiz;
				if (!TablaSimbolos.estaIdent(right)){
					TablaSimbolos.insertar(right, TablaSimbolos.Tipo.CHAR);
				}else if (TablaSimbolos.tipo(right) == TablaSimbolos.Tipo.CHAR){
					Errores.varDeclarada(right);
				}else {
					TablaSimbolos.setTipo(right, TablaSimbolos.Tipo.CHAR);
				}
				PLXC.out.println("\t" + right + " = " + der.izq.gc() + ";");
				if(izq != null){
					izq.gc();
				}
				break;
			case "asigInt":
				right = der.raiz;
				if (!TablaSimbolos.estaIdent(right)){
					TablaSimbolos.insertar(right, TablaSimbolos.Tipo.INT);
				}else if (TablaSimbolos.tipo(right) == TablaSimbolos.Tipo.INT){
					Errores.varDeclarada(right);
				}else {
					TablaSimbolos.setTipo(right, TablaSimbolos.Tipo.INT);
				}
				PLXC.out.println("\t" + right + " = " + der.izq.gc() + ";");
				if(izq != null){
					izq.gc();
				}
				break;
			case "asigFloat":
				right = der.raiz;
				if (!TablaSimbolos.estaIdent(right)){
					TablaSimbolos.insertar(right, TablaSimbolos.Tipo.FLOAT);
				}else if (TablaSimbolos.tipo(right) == TablaSimbolos.Tipo.FLOAT){
					Errores.varDeclarada(right);
				}else {
					TablaSimbolos.setTipo(right, TablaSimbolos.Tipo.FLOAT);
				}
				PLXC.out.println("\t" + right + " = " + der.izq.gc() + ";");
				if(izq != null){
					izq.gc();
				}
				break;
			case "asigBool":
				right = der.raiz; //raiz AST siguiente
				left = izq.raiz; // identificador
				if (!(right.equals("true") || right.equals("false"))){ // condicion
					right = der.gc();
					et = der.v; //et si se cumple la condición
					et1 = der.f;	//et si no se cumple la condición
					et2 = Generador.nuevaEtiqueta(); //et final y salida
					PLXC.out.println(et + ":");//se cumple la condicion
					PLXC.out.println("\t" + left + " = 1;");
					PLXC.out.println("\tgoto " + et2 + ";");
					PLXC.out.println(et1 + ":");//no se cumple la condicion
					PLXC.out.println("\t" + left + " = 0;");
					PLXC.out.println(et2 + ":");
				}else if(right.equals("true")){
					right = "1";
					PLXC.out.println("\t" + left + " = " + right + ";");
				}else{
					right = "0";
					PLXC.out.println("\t" + left + " = " + right + ";");
				}
				res = left;				
				break;
			case "intIdent":
				TablaSimbolos.insertar(der.raiz, TablaSimbolos.Tipo.INT);
				if (izq != null){
					izq.gc();
				}
				break;
			case "charIdent":
				if (!TablaSimbolos.estaIdent(der.raiz)){
					TablaSimbolos.insertar(der.raiz, TablaSimbolos.Tipo.CHAR);
				}else if (TablaSimbolos.tipo(der.raiz) == TablaSimbolos.Tipo.CHAR){
					Errores.varDeclarada(der.raiz);
				}else {
					TablaSimbolos.setTipo(der.raiz, TablaSimbolos.Tipo.CHAR);
				}
				if(izq != null){
					izq.gc();
				}
				break;
			case "while":
			    et = Generador.nuevaEtiqueta();
				PLXC.out.println(et + ":");
				izq.gc();
				PLXC.out.println(izq.v + ":");
				der.gc();
				PLXC.out.println("\tgoto " + et + ";");
				PLXC.out.println(izq.f + ":");
				break;
			case "doWhile":
				et = Generador.nuevaEtiqueta();
				PLXC.out.println(et + ":");
				der.gc();
				izq.gc();
				PLXC.out.println(izq.v + ":");
				PLXC.out.println("\tgoto " + et + ";");
				PLXC.out.println(izq.f + ":");
				break;
			case "for":			    
				aux = izq.gc();
				if(izq.raiz.equals("expFor")) PLXC.out.println(izq.v + ":");
				der.gc(); //sentencia
				PLXC.out.println("\tgoto " + aux + ";");
				PLXC.out.println(izq.f + ":");
				break;
			case "expFor":
				if (der.izq != null) {
					der.izq.gc(); //aux.izq
				}
				et = Generador.nuevaEtiqueta();
				PLXC.out.println(et + ":");
				izq.gc(); //expfor
				et2 = Generador.nuevaEtiqueta();
				PLXC.out.println(et2 + ":");
				if (der.der!=null) {
					der.der.gc(); //aux.der
				}
				PLXC.out.println("\tgoto " + et + ";");
				v = izq.v;
				f = izq.f;
				s = et2;
				res = s;
				break;
		}
		
		return res;
	}

	private String limpiaCadena(String cadena) {
		StringBuilder res = new StringBuilder();
		
		for (int i = 0; i < cadena.length(); i++) {
			if (i > 1 && (res.charAt(res.length() - 1) == '\\' && isSpecialCharacter(cadena.charAt(i)))) {
				res.deleteCharAt(res.length() - 1);
				res.append(cadena.charAt(i));
			} else {
				res.append(cadena.charAt(i));
			}
		}
		return res.toString();
	}

	private boolean isSpecialCharacter(char c) {
		return c == '\\' || c == '\"' || c == '\'';
	}
	private TablaSimbolos.Tipo resuelveTipo (TablaSimbolos.Tipo tipo){
		TablaSimbolos.Tipo result = TablaSimbolos.Tipo.INT;
		switch (tipo) {
			case ARRAY_CHAR:
				return TablaSimbolos.Tipo.CHAR;
			case ARRAY_INT:
				return TablaSimbolos.Tipo.INT;
			case ARRAY_FLOAT:
				return TablaSimbolos.Tipo.FLOAT;
			default:
				break;
		}
		return result;
	}
	private TablaSimbolos.Tipo resuelveTipo(String right, String left) {
		TablaSimbolos.Tipo tipo = TablaSimbolos.Tipo.INT;
		if(TablaSimbolos.tipo(left) == TablaSimbolos.tipo(right)){
			if(TablaSimbolos.tipo(left) == TablaSimbolos.Tipo.ARRAY_CHAR){
				tipo = TablaSimbolos.Tipo.CHAR;
			}else if(TablaSimbolos.tipo(left) == TablaSimbolos.Tipo.ARRAY_INT){	
				tipo = TablaSimbolos.Tipo.INT;
			}else {
				tipo = TablaSimbolos.tipo(left);
			}
		}else if(((TablaSimbolos.tipo(left) == TablaSimbolos.Tipo.FLOAT) && (TablaSimbolos.tipo(right) != TablaSimbolos.Tipo.STRING))
				|| ((TablaSimbolos.tipo(right) == TablaSimbolos.Tipo.FLOAT) && (TablaSimbolos.tipo(left) != TablaSimbolos.Tipo.STRING))){	
			tipo = TablaSimbolos.Tipo.FLOAT;
		}else if((TablaSimbolos.tipo(left) == TablaSimbolos.Tipo.CHAR) && (TablaSimbolos.tipo(right) == TablaSimbolos.Tipo.INT) 
				|| (TablaSimbolos.tipo(left) == TablaSimbolos.Tipo.INT) && (TablaSimbolos.tipo(right) == TablaSimbolos.Tipo.CHAR)){
			tipo = TablaSimbolos.Tipo.INT;
		}else{
			Errores.noTipo();
		}
		return tipo;
	}

	private boolean comprobarTipoArray(TablaSimbolos.Tipo tipo, String right) {
		boolean result = false;
		if(tipo == TablaSimbolos.Tipo.ARRAY_INT && TablaSimbolos.tipo(right) == TablaSimbolos.Tipo.INT){
			result = true;
		}else if(tipo == TablaSimbolos.Tipo.ARRAY_CHAR && TablaSimbolos.tipo(right) == TablaSimbolos.Tipo.CHAR){
			result = true;
		}else if(tipo == TablaSimbolos.Tipo.ARRAY_CHAR && TablaSimbolos.tipo(right) == TablaSimbolos.Tipo.INT){
			result = true;
		}else if(tipo == TablaSimbolos.Tipo.ARRAY_FLOAT && TablaSimbolos.tipo(right) == TablaSimbolos.Tipo.FLOAT){
			result = true;
		}else{
			Errores.noTipo();
		}
		return result;
	}

	private boolean comprobarCasteo(String left, String raiz, String derecha) {
		boolean result = false;
		if(raiz.equals("castChar")){
			if(TablaSimbolos.tipo(left) == TablaSimbolos.Tipo.CHAR || TablaSimbolos.tipo(left) == TablaSimbolos.Tipo.INT){
				result = true;
			}else if(TablaSimbolos.tipo(left) == TablaSimbolos.Tipo.ARRAY_CHAR || TablaSimbolos.tipo(left) == TablaSimbolos.Tipo.ARRAY_INT){
				result = true;
			}
		}else if(raiz.equals("castInt")){
			if(TablaSimbolos.tipo(left) == TablaSimbolos.Tipo.INT){
				result = true;
			}else if(TablaSimbolos.tipo(left) == TablaSimbolos.Tipo.ARRAY_INT){
				result = true;
			}
		}else if(raiz.equals("castFloat")){
			if(TablaSimbolos.tipo(left) == TablaSimbolos.Tipo.FLOAT){
				result = true;
			}else if(TablaSimbolos.tipo(left) == TablaSimbolos.Tipo.ARRAY_FLOAT){
				result = true;
			}
		}else if(TablaSimbolos.tipo(left) == TablaSimbolos.Tipo.FLOAT && TablaSimbolos.tipo(derecha) == TablaSimbolos.Tipo.INT){
			result = true;
		}else if(TablaSimbolos.tipo(left) == TablaSimbolos.Tipo.BOOLEAN){
			result = true;
			if(TablaSimbolos.tipo(derecha) == TablaSimbolos.Tipo.INT){
				result = false;
			}
		}
		return result;
	}

}

