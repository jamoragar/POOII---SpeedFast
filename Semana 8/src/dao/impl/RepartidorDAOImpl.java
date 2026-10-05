package dao.impl;
import dao.RepartidorDAO;
import modelo.*;
import util.ConexionDB;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/** Consultas parametrizadas; cada operación cierra conexión, sentencia y resultado. */
public class RepartidorDAOImpl implements RepartidorDAO {
    @Override
    public int create(Repartidor entidad) throws SQLException {
        String sql = "INSERT INTO repartidores (nombre) VALUES (?)";
        try (Connection cn = ConexionDB.conectar();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entidad.getNombre());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
                throw new SQLException("No se obtuvo el ID generado.");
            }
        }
    }

    @Override
    public List<Repartidor> readAll() throws SQLException {
        String sql = "SELECT id, nombre FROM repartidores ORDER BY id";
        List<Repartidor> lista = new ArrayList<>();
        try (Connection cn = ConexionDB.conectar(); PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(new Repartidor(rs.getInt("id"), rs.getString("nombre")));
        }
        return lista;
    }

    @Override
    public boolean update(Repartidor entidad) throws SQLException {
        String sql = "UPDATE repartidores SET nombre = ? WHERE id = ?";
        try (Connection cn = ConexionDB.conectar(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, entidad.getNombre());
            ps.setInt(2, entidad.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM repartidores WHERE id = ?";
        try (Connection cn = ConexionDB.conectar(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
