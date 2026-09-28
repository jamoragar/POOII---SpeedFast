package controlador;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import modelo.Entrega;
import modelo.Pedido;
import modelo.Repartidor;
import modelo.TipoPedido;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class ControladorPedidos {

    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final RepartidorDAO repartidorDAO = new RepartidorDAO();
    private final EntregaDAO entregaDAO = new EntregaDAO();
    private final List<Runnable> observadores = new ArrayList<>();
    private final List<Consumer<String>> observadoresError = new ArrayList<>();
    private final Set<Integer> simulaciones = new HashSet<>();
    private int operacionesActivas;
    private boolean cerrado;

    // Los métodos JDBC se invocan dentro de ejecutarEnSegundoPlano().
    public int registrarPedido(String direccion, TipoPedido tipo) throws SQLException {
        return pedidoDAO.guardar(new Pedido(direccion, tipo));
    }

    public int registrarRepartidor(String nombre) throws SQLException {
        return repartidorDAO.guardar(new Repartidor(nombre));
    }

    public List<Pedido> obtenerPedidos() throws SQLException {
        return pedidoDAO.listarTodos();
    }

    public List<Repartidor> obtenerRepartidores() throws SQLException {
        return repartidorDAO.listarTodos();
    }

    public List<Entrega> obtenerEntregas() throws SQLException {
        return entregaDAO.listarTodos();
    }

    public void iniciarEntrega(Pedido pedido, Repartidor repartidor, Runnable terminado) {
        if (pedido == null || repartidor == null) {
            throw new IllegalArgumentException("Selecciona un pedido y un repartidor disponible.");
        }
        if (!simulaciones.add(pedido.getId())) {
            throw new IllegalArgumentException("Este pedido ya tiene una simulación activa.");
        }
        ejecutarEnSegundoPlano(() -> {
            entregaDAO.guardar(new Entrega(pedido.getId(), repartidor.getId()));
            SwingUtilities.invokeLater(this::notificarCambios);
            Thread.sleep(3000);
            pedidoDAO.completarEntrega(pedido.getId());
            return null;
        }, resultado -> { }, () -> {
            simulaciones.remove(pedido.getId());
            terminado.run();
            notificarCambios();
        });
    }

    // Permite finalizar un EN_REPARTO persistido si se cerró el proceso o falló la conexión.
    public void finalizarEntrega(Pedido pedido, Runnable terminado) {
        if (pedido == null || estaSimulando(pedido.getId())) {
            throw new IllegalArgumentException("Espera a que termine la simulación del pedido.");
        }
        ejecutarEnSegundoPlano(() -> {
            pedidoDAO.completarEntrega(pedido.getId());
            return null;
        }, resultado -> notificarCambios(), terminado);
    }

    public boolean estaSimulando(int idPedido) { return simulaciones.contains(idPedido); }
    public boolean hayOperacionesActivas() { return operacionesActivas > 0; }

    /** Mantiene el patrón del ejemplo: vista -> controlador -> DAO, sin bloquear Swing. */
    public <T> void ejecutarEnSegundoPlano(Callable<T> operacion, Consumer<T> exito, Runnable terminado) {
        if (!SwingUtilities.isEventDispatchThread() || cerrado) {
            throw new IllegalStateException("La operación debe iniciarse desde una ventana activa.");
        }
        operacionesActivas++;
        new SwingWorker<T, Void>() {
            @Override
            protected T doInBackground() throws Exception {
                return operacion.call();
            }

            @Override
            protected void done() {
                operacionesActivas--;
                try {
                    exito.accept(get());
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    informarError("La operación fue interrumpida. Refresca los datos antes de reintentar.");
                } catch (ExecutionException ex) {
                    Throwable causa = ex.getCause();
                    informarError("No se pudo completar la operación: " + causa.getMessage()
                            + "\nRevisa MySQL y la configuración. Refresca antes de reintentar.");
                } finally {
                    terminado.run();
                }
            }
        }.execute();
    }

    public void notificarCambios() {
        if (!cerrado) {
            for (Runnable observador : List.copyOf(observadores)) { observador.run(); }
        }
    }

    public void agregarObservador(Runnable observador) { observadores.add(observador); }
    public void quitarObservador(Runnable observador) { observadores.remove(observador); }
    public void agregarObservadorError(Consumer<String> observador) { observadoresError.add(observador); }

    private void informarError(String mensaje) {
        for (Consumer<String> observador : List.copyOf(observadoresError)) { observador.accept(mensaje); }
    }

    public void cerrar() {
        cerrado = true;
        observadores.clear();
        observadoresError.clear();
    }
}
