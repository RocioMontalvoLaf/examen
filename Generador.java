public class Generador {
    private static int t = 0;
    private static int l = 0;
    private static int index = 0;
    private static int tAsigArray = 0;

    public static String nuevaVariable() {
        return "t" + Integer.toString(t++);
    }

    public static String getCurrentVariable() {
        int aux = t-1;
        return "t" + Integer.toString(aux);
    }

    public static String nuevaEtiqueta() {
        return "L" + Integer.toString(l++);
    }

    public static String getCurrentEtiqueta() {
        int aux = l-1;
        return "L" + Integer.toString(aux);
    }

    public static String getPastEtiqueta() {
        int aux = l-2;
        return "L" + Integer.toString(aux);
    }

    public static String getindex() {
        return Integer.toString(index++);
    }
    public static int getCurrentIndex() {
        int aux = index;
        return aux;
    }
    public static void resetIndex() {
        index = 0;
    }
    public static String getVarArray() {
        tAsigArray = t++;
        return "t" + Integer.toString(tAsigArray);
    }
    public static String getCurrentVarArray() {
        return "t" + Integer.toString(tAsigArray);
    }

}