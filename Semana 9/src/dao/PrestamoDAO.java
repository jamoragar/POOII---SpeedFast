package dao;
import modelo.Prestamo;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
public interface PrestamoDAO extends CrudDAO<Prestamo> {
    boolean existeActivo(Connection cn,int estudiante,int libro,int excluir) throws SQLException;
    List<Prestamo> porEstudiante(Connection cn,int estudiante) throws SQLException;
}
