package util;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.*;
import java.util.Properties;

/** Una instancia y una conexión por proceso. El monitor cubre operaciones completas. */
public final class DatabaseConnection {
    private Connection conexion;
    private DatabaseConnection() { }
    private static class Holder { private static final DatabaseConnection INSTANCE=new DatabaseConnection(); }
    public static DatabaseConnection getInstance() { return Holder.INSTANCE; }
    @FunctionalInterface public interface Operacion<T> { T ejecutar(Connection cn) throws SQLException; }

    private Connection conectar() throws SQLException {
        if(conexion!=null && !conexion.isClosed() && conexion.isValid(2)) return conexion;
        cerrar();
        Properties props=new Properties();
        Path base=Path.of(System.getProperty("user.dir"));
        try {
            Path codigo=Path.of(DatabaseConnection.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            if(codigo.toString().endsWith(".jar")) base=codigo.getParent();
            Path archivo=Path.of(System.getProperty("biblioteca.config",base.resolve("config/db.properties").toString()));
            if(Files.exists(archivo)) try(Reader reader=Files.newBufferedReader(archivo,StandardCharsets.UTF_8)) { props.load(reader); }
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch(Exception ex) { throw new SQLException("Revisa config/db.properties y el conector JDBC.",ex); }
        String clave=valor(props,"db.password","BIBLIOTECA_DB_PASSWORD",null);
        if(clave==null) throw new SQLException("Completa config/db.properties con tus credenciales de MySQL.");
        Properties acceso=new Properties();
        acceso.setProperty("user",valor(props,"db.user","BIBLIOTECA_DB_USER","root"));acceso.setProperty("password",clave);
        acceso.setProperty("connectTimeout","5000");acceso.setProperty("socketTimeout","10000");
        acceso.setProperty("useAffectedRows","false");
        conexion=DriverManager.getConnection(valor(props,"db.url","BIBLIOTECA_DB_URL","jdbc:mysql://localhost:3306/biblioteca"),acceso);
        return conexion;
    }
    private static String valor(Properties p,String clave,String env,String defecto) {
        return System.getenv(env)!=null?System.getenv(env):p.getProperty(clave,defecto);
    }
    public synchronized <T> T consultar(Operacion<T> operacion) throws SQLException {
        return operacion.ejecutar(conectar());
    }
    /** Ante cualquier fallo, revierte la unidad de negocio entera; no reintenta escrituras. */
    public synchronized <T> T transaccion(Operacion<T> operacion) throws SQLException {
        Connection cn=conectar();
        cn.setAutoCommit(false);
        try {
            T resultado=operacion.ejecutar(cn); cn.commit(); return resultado;
        } catch(SQLException | RuntimeException ex) {
            try { cn.rollback(); } catch(SQLException rollback) { ex.addSuppressed(rollback); cerrar(); }
            throw ex;
        } finally {
            try { cn.setAutoCommit(true); } catch(SQLException ex) { cerrar(); }
        }
    }
    public synchronized void cerrar() {
        if(conexion!=null) { try { conexion.close(); } catch(SQLException ignored) { } conexion=null; }
    }
}
