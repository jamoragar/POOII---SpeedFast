package dao.impl;
import dao.CrudDAO;
import modelo.Entidad;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Reutiliza el CRUD sin ocultar la transacción del servicio ni cerrar su conexión. */
public abstract class JdbcDAO<T extends Entidad> implements CrudDAO<T> {
    private final String tabla, columnas;
    private final String insertar, actualizar;
    protected JdbcDAO(String tabla,String... campos) {
        this.tabla=tabla;this.columnas=String.join(", ",campos);
        insertar="INSERT INTO "+tabla+" ("+columnas+") VALUES ("+String.join(", ",java.util.Collections.nCopies(campos.length,"?"))+")";
        actualizar="UPDATE "+tabla+" SET "+String.join(", ",java.util.Arrays.stream(campos).map(c->c+" = ?").toList())+" WHERE id = ?";
    }
    protected abstract T mapear(ResultSet rs) throws SQLException;
    protected abstract void parametros(PreparedStatement ps,T entidad) throws SQLException;
    @Override public int create(Connection cn,T entidad) throws SQLException {
        try(PreparedStatement ps=cn.prepareStatement(insertar,Statement.RETURN_GENERATED_KEYS)) {
            parametros(ps,entidad);ps.executeUpdate();
            try(ResultSet rs=ps.getGeneratedKeys()) {
                if(rs.next())return rs.getInt(1);throw new SQLException("No se obtuvo el ID generado.");
            }
        }
    }
    @Override public List<T> readAll(Connection cn) throws SQLException { return consultar(cn,"",ps->{}); }
    @Override public T findById(Connection cn,int id,boolean bloquear) throws SQLException {
        List<T> filas=consultar(cn," WHERE id = ?"+(bloquear?" FOR UPDATE":""),ps->ps.setInt(1,id));
        return filas.isEmpty()?null:filas.get(0);
    }
    @Override public boolean update(Connection cn,T entidad) throws SQLException {
        try(PreparedStatement ps=cn.prepareStatement(actualizar)) {
            parametros(ps,entidad);ps.setInt(columnas.split(",").length+1,entidad.getId());return ps.executeUpdate()>0;
        }
    }
    @Override public boolean delete(Connection cn,int id) throws SQLException {
        try(PreparedStatement ps=cn.prepareStatement("DELETE FROM "+tabla+" WHERE id = ?")) {
            ps.setInt(1,id);return ps.executeUpdate()>0;
        }
    }
    @FunctionalInterface protected interface Parametros { void aplicar(PreparedStatement ps) throws SQLException; }
    protected List<T> consultar(Connection cn,String condicion,Parametros bind) throws SQLException {
        List<T> filas=new ArrayList<>();
        try(PreparedStatement ps=cn.prepareStatement("SELECT id, "+columnas+" FROM "+tabla+condicion)) {
            bind.aplicar(ps);try(ResultSet rs=ps.executeQuery()) { while(rs.next())filas.add(mapear(rs)); }
        }return filas;
    }
}
