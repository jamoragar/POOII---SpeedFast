package dao.impl;
import dao.EntregaDAO;
import modelo.*;
import util.ConexionDB;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/** Consultas parametrizadas; cada operación cierra conexión, sentencia y resultado. */
public class EntregaDAOImpl implements EntregaDAO {
    @Override
    public int create(Entrega entidad) throws SQLException {
        String sql = "INSERT INTO entregas (id_pedido, id_repartidor, fecha, hora) VALUES (?, ?, ?, ?)";
        try (Connection cn = ConexionDB.conectar();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entidad.getIdPedido());
            ps.setInt(2, entidad.getIdRepartidor());
            ps.setObject(3, entidad.getFecha());
            ps.setObject(4, entidad.getHora());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
                throw new SQLException("No se obtuvo el ID generado.");
            }
        }
    }

    @Override
    public List<Entrega> readAll() throws SQLException {
        String sql = "SELECT id, id_pedido, id_repartidor, fecha, hora FROM entregas ORDER BY id";
        List<Entrega> lista = new ArrayList<>();
        try (Connection cn = ConexionDB.conectar(); PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(new Entrega(rs.getInt("id"), rs.getInt("id_pedido"), rs.getInt("id_repartidor"), rs.getObject("fecha", LocalDate.class), rs.getObject("hora", LocalTime.class)));
        }
        return lista;
    }

    @Override
    public boolean update(Entrega entidad) throws SQLException {
        String sql = "UPDATE entregas SET id_pedido = ?, id_repartidor = ?, fecha = ?, hora = ? WHERE id = ?";
        try (Connection cn = ConexionDB.conectar(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, entidad.getIdPedido());
            ps.setInt(2, entidad.getIdRepartidor());
            ps.setObject(3, entidad.getFecha());
            ps.setObject(4, entidad.getHora());
            ps.setInt(5, entidad.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM entregas WHERE id = ?";
        try (Connection cn = ConexionDB.conectar(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
