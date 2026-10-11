package modelo;
import java.util.List;
/** Resultado de solo lectura; no contiene contraseñas ni objetos JDBC abiertos. */
public record Reporte(String[] columnas,List<Object[]> filas) { }
