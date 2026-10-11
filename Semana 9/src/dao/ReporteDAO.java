package dao;
import modelo.Reporte;
import java.sql.Connection;
import java.sql.SQLException;
public interface ReporteDAO {
    Reporte masPrestados(Connection cn) throws SQLException;
    Reporte historial(Connection cn,int estudiante) throws SQLException;
    Reporte activos(Connection cn) throws SQLException;
}
