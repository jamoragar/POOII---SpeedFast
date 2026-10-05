package util;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public final class Validaciones {
    private Validaciones() { }
    public static String texto(String valor, String campo) {
        String limpio = valor.trim();
        if (limpio.isEmpty() || limpio.codePointCount(0, limpio.length()) > 100)
            throw new IllegalArgumentException(campo + " es obligatorio y admite hasta 100 caracteres.");
        return limpio;
    }
    public static LocalDate fecha(String valor) {
        try {
            if (!valor.matches("\\d{4}-\\d{2}-\\d{2}")) throw new IllegalArgumentException();
            LocalDate fecha = LocalDate.parse(valor, DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT));
            if (fecha.getYear() < 1000) throw new IllegalArgumentException();
            return fecha;
        } catch (RuntimeException ex) { throw new IllegalArgumentException("Fecha inválida. Usa yyyy-MM-dd (año desde 1000)."); }
    }
    public static LocalTime hora(String valor) {
        try {
            if (!valor.matches("\\d{2}:\\d{2}:\\d{2}")) throw new IllegalArgumentException();
            return LocalTime.parse(valor, DateTimeFormatter.ofPattern("HH:mm:ss").withResolverStyle(ResolverStyle.STRICT));
        } catch (RuntimeException ex) { throw new IllegalArgumentException("Hora inválida. Usa HH:mm:ss, entre 00:00:00 y 23:59:59."); }
    }
}
