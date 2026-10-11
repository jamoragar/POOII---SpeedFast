package util;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public final class Validaciones {
    private Validaciones() { }
    public static String texto(String valor,String campo,int max) {
        String v=valor==null?"":valor.trim();
        if(v.isEmpty() || v.codePointCount(0,v.length())>max) throw new IllegalArgumentException(campo+" es obligatorio y admite hasta "+max+" caracteres.");
        return v;
    }
    public static String rut(String valor) {
        String v=texto(valor,"RUT",12).replace(".","").toUpperCase();
        if(!v.matches("[0-9]{7,8}-[0-9K]")) throw new IllegalArgumentException("RUT inválido. Usa el formato 12345678-9.");
        return v;
    }
    public static String correo(String valor) {
        String v=texto(valor,"Correo",100);
        if(!v.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) throw new IllegalArgumentException("Correo inválido.");
        return v;
    }
    public static int stock(String valor) {
        try { int n=Integer.parseInt(valor.trim()); if(n<0) throw new NumberFormatException();return n; }
        catch(NumberFormatException ex) { throw new IllegalArgumentException("El stock debe ser un entero no negativo."); }
    }
    public static LocalDate fecha(String valor) {
        try {
            if(!valor.matches("\\d{4}-\\d{2}-\\d{2}")) throw new IllegalArgumentException();
            LocalDate fecha=LocalDate.parse(valor,DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT));
            if(fecha.getYear()<1000) throw new IllegalArgumentException();return fecha;
        } catch(RuntimeException ex) { throw new IllegalArgumentException("Fecha inválida. Usa yyyy-MM-dd y una fecha existente."); }
    }
    public static void clave(char[] valor) {
        if(valor==null || valor.length<8 || valor.length>64) throw new IllegalArgumentException("La contraseña debe tener entre 8 y 64 caracteres.");
    }
}
