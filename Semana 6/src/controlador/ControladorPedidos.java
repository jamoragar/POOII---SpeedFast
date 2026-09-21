package controlador;

import modelo.Pedido;
import modelo.Repartidor;
import modelo.TipoPedido;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

/**
 * Datos compartidos por las vistas. Las operaciones se realizan en el hilo de
 * eventos (EDT); únicamente la espera simulada ocurre en segundo plano.
 */
public class ControladorPedidos {
    private final List<Pedido> pedidos = new ArrayList<>();
    private final List<Repartidor> repartidores = List.of(
            new Repartidor("Juan"), new Repartidor("Camila"), new Repartidor("Pedro"));
    private final Map<Pedido, SwingWorker<Void, Void>> entregas = new LinkedHashMap<>();
    private final List<Runnable> observadores = new ArrayList<>();
    private final List<Consumer<String>> observadoresError = new ArrayList<>();
    private boolean cerrado;

    public Pedido registrarPedido(String textoId, String direccion, TipoPedido tipo) {
        comprobarAbierto();
        if (textoId == null || textoId.isBlank()) {
            throw new IllegalArgumentException("El ID es obligatorio.");
        }
        int id = Integer.parseInt(textoId.trim());
        Pedido pedido = new Pedido(id, direccion, tipo);
        if (pedidos.stream().anyMatch(actual -> actual.getId() == id)) {
            throw new IllegalArgumentException("Ya existe un pedido con ese ID.");
        }
        pedidos.add(pedido);
        notificarCambios();
        return pedido;
    }

    public List<Pedido> obtenerPedidos() {
        comprobarHilo();
        return List.copyOf(pedidos);
    }

    public List<Repartidor> obtenerRepartidoresDisponibles() {
        comprobarHilo();
        return repartidores.stream().filter(Repartidor::isDisponible).toList();
    }

    public void iniciarEntrega(Pedido pedido, Repartidor repartidor) {
        comprobarAbierto();
        if (pedido == null || !pedidos.contains(pedido)) {
            throw new IllegalArgumentException("Selecciona un pedido de la tabla.");
        }
        if (repartidor == null || !repartidores.contains(repartidor)) {
            throw new IllegalArgumentException("Selecciona un repartidor disponible.");
        }
        // Validación y reserva antes de iniciar el worker: un segundo clic no duplica la entrega.
        pedido.iniciarEntrega(repartidor);
        SwingWorker<Void, Void> tarea = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws InterruptedException {
                Thread.sleep(3000);
                return null;
            }

            @Override
            protected void done() {
                // cerrar() ya revierte y elimina las tareas canceladas al salir.
                if (entregas.remove(pedido) != this) {
                    return;
                }
                String error = null;
                try {
                    get();
                    pedido.completarEntrega();
                } catch (CancellationException | ExecutionException ex) {
                    pedido.revertirEntrega();
                    error = "No se pudo completar el pedido #" + pedido.getId()
                            + ". Volvió a pendiente; puedes reintentar la entrega.";
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    pedido.revertirEntrega();
                    error = "La entrega fue interrumpida. El pedido volvió a pendiente.";
                }
                notificarCambios();
                if (error != null) {
                    for (Consumer<String> observador : List.copyOf(observadoresError)) {
                        observador.accept(error);
                    }
                }
            }
        };
        entregas.put(pedido, tarea);
        tarea.execute();
        notificarCambios();
    }

    public boolean hayEntregasActivas() {
        comprobarHilo();
        return !entregas.isEmpty();
    }

    public void agregarObservador(Runnable observador) {
        comprobarHilo();
        observadores.add(observador);
    }

    public void quitarObservador(Runnable observador) {
        comprobarHilo();
        observadores.remove(observador);
    }

    public void agregarObservadorError(Consumer<String> observador) {
        comprobarHilo();
        observadoresError.add(observador);
    }

    public void cerrar() {
        comprobarHilo();
        cerrado = true;
        // Primero retirar las tareas: done() puede ejecutarse durante cancel().
        Map<Pedido, SwingWorker<Void, Void>> pendientes = new LinkedHashMap<>(entregas);
        entregas.clear();
        pendientes.forEach((pedido, tarea) -> {
            tarea.cancel(true);
            pedido.revertirEntrega();
        });
        observadores.clear();
        observadoresError.clear();
    }

    private void notificarCambios() {
        for (Runnable observador : List.copyOf(observadores)) {
            observador.run();
        }
    }

    private void comprobarAbierto() {
        comprobarHilo();
        if (cerrado) {
            throw new IllegalStateException("El controlador está cerrado.");
        }
    }

    private void comprobarHilo() {
        if (!SwingUtilities.isEventDispatchThread()) {
            throw new IllegalStateException("Usa el controlador desde el hilo de eventos de Swing.");
        }
    }
}
