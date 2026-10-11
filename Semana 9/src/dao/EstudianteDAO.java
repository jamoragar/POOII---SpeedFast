package dao;
import modelo.Estudiante;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
public interface EstudianteDAO extends CrudDAO<Estudiante> {
    Estudiante findByRut(Connection cn,String rut) throws SQLException;
}
