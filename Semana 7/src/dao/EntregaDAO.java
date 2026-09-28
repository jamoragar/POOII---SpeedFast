package dao;

import modelo.Entrega;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class EntregaDAO {

    // La reserva del pedido y el registro de la entrega deben guardarse juntos.
    public int guardar(Entrega entrega) throws SQLException {
        try (Connection conexion = ConexionBD.conectar()) {
            conexion.setAutoCommit(false);
            try {
                // Serializa asignaciones del mismo repartidor entre aplicaciones.
                try (PreparedStatement ps = conexion.prepareStatement(
                        "SELECT id FROM repartidor WHERE id = ? FOR UPDATE")) {
                    ps.setInt(1, entrega.getIdRepartidor());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) { throw new SQLException("El repartidor no existe."); }
                    }
                }
                try (PreparedStatement ps = conexion.prepareStatement(
                        "SELECT e.id FROM entrega e JOIN pedido p ON p.id = e.id_pedido "
                                + "WHERE e.id_repartidor = ? AND p.estado = 'EN_REPARTO'")) {
                    ps.setInt(1, entrega.getIdRepartidor());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) { throw new SQLException("El repartidor ya tiene una entrega activa."); }
                    }
                }
                try (PreparedStatement ps = conexion.prepareStatement(
                        "UPDATE pedido SET estado = 'EN_REPARTO' WHERE id = ? AND estado = 'PENDIENTE'")) {
                    ps.setInt(1, entrega.getIdPedido());
                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("El pedido no existe o ya no está pendiente.");
                    }
                }
                int id;
                String sql = "INSERT INTO entrega (id_pedido, id_repartidor, fecha, hora) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, entrega.getIdPedido());
                    ps.setInt(2, entrega.getIdRepartidor());
                    ps.setDate(3, Date.valueOf(entrega.getFecha()));
                    ps.setTime(4, Time.valueOf(entrega.getHora()));
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) { throw new SQLException("MySQL no devolvió el ID de la entrega."); }
                        id = rs.getInt(1);
                    }
                }
                conexion.commit();
                return id;
            } catch (SQLException ex) {
                try { conexion.rollback(); } catch (SQLException rollback) { ex.addSuppressed(rollback); }
                throw ex;
            }
        }
    }

    public List<Entrega> listarTodos() throws SQLException {
        List<Entrega> entregas = new ArrayList<>();
        String sql = "SELECT id, id_pedido, id_repartidor, fecha, hora FROM entrega ORDER BY id";
        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                entregas.add(new Entrega(rs.getInt("id"), rs.getInt("id_pedido"),
                        rs.getInt("id_repartidor"), rs.getDate("fecha").toLocalDate(),
                        rs.getTime("hora").toLocalTime()));
            }
        }
        return entregas;
    }
}
