package vista;

import util.Mensajes;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

/** Comportamiento compartido; los componentes se diseñan en los archivos .form. */
public abstract class VentanaGestion<T> extends JFrame {
    private JTable tabla;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> ordenador;
    private JButton[] botones;
    private List<T> registros = List.of();
    private boolean sincronizando;
    private boolean ocupado;
    private boolean refrescoPendiente;
    private final Runnable notificar;

    protected VentanaGestion(String titulo, Runnable notificar) {
        super("SpeedFast — " + titulo);
        this.notificar = notificar;
        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    public final boolean estaOcupado() { return ocupado; }

    protected void configurar(JPanel panel, JTable tabla, String[] columnas,
                              JButton crear, JButton actualizar, JButton eliminar,
                              JButton limpiar, JButton refrescar) {
        this.tabla = tabla;
        botones = new JButton[]{crear, actualizar, eliminar, limpiar, refrescar};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int fila, int columna) { return false; }
            @Override public Class<?> getColumnClass(int columna) { return columna == 0 ? Integer.class : Object.class; }
        };
        tabla.setModel(modelo);
        ordenador = new TableRowSorter<>(modelo);
        tabla.setRowSorter(ordenador);
        ajustarColumnas();
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !sincronizando && !ocupado) {
                T entidad = seleccionado();
                if (entidad != null) mostrar(entidad);
            }
        });
        crear.addActionListener(e -> guardar(false));
        actualizar.addActionListener(e -> guardar(true));
        eliminar.addActionListener(e -> eliminarSeleccionado());
        limpiar.addActionListener(e -> { tabla.clearSelection(); limpiar(); });
        refrescar.addActionListener(e -> refrescar());
        setContentPane(panel);
        setSize(980, 660);
        setLocationByPlatform(true);
    }

    private T seleccionado() {
        int fila = tabla.getSelectedRow();
        return fila < 0 ? null : registros.get(tabla.convertRowIndexToModel(fila));
    }

    protected final void filtrar(RowFilter<DefaultTableModel, Integer> filtro) {
        boolean anterior = sincronizando;
        sincronizando = true;
        try { ordenador.setRowFilter(filtro); }
        finally { sincronizando = anterior; }
    }

    public final void refrescar() {
        if (ocupado) { refrescoPendiente = true; return; }
        T seleccion = seleccionado();
        ejecutar(this::consultar, nuevos -> {
            sincronizando = true;
            try {
                registros = nuevos;
                Object[][] filas = nuevos.stream().map(this::fila).toArray(Object[][]::new);
                Object[] columnas = new Object[modelo.getColumnCount()];
                for (int i = 0; i < columnas.length; i++) columnas[i] = modelo.getColumnName(i);
                // Un solo evento conserva coherentes los índices del TableRowSorter.
                var orden = tabla.getRowSorter().getSortKeys();
                modelo.setDataVector(filas, columnas);
                ajustarColumnas();
                tabla.getRowSorter().setSortKeys(orden);
                actualizarRelaciones();
                aplicarFiltros();
                tabla.clearSelection();
                if (seleccion != null) {
                    for (int i = 0; i < nuevos.size(); i++) if (id(nuevos.get(i)) == id(seleccion)) {
                        int vista = tabla.convertRowIndexToView(i);
                        if (vista >= 0) tabla.setRowSelectionInterval(vista, vista);
                    }
                }
            } finally { sincronizando = false; }
        });
    }

    private void guardar(boolean actualizar) {
        T seleccion = seleccionado();
        if (actualizar && seleccion == null) { avisoSeleccion(); return; }
        try {
            T entidad = leer(actualizar ? id(seleccion) : 0);
            ejecutar(() -> {
                if (actualizar) {
                    if (!update(entidad)) throw new IllegalArgumentException("El registro ya no existe. Refresca el listado.");
                    return id(entidad);
                }
                return create(entidad);
            }, generado -> {
                tabla.clearSelection();
                limpiar();
                notificar.run();
                JOptionPane.showMessageDialog(this, "Registro " + generado + (actualizar ? " actualizado." : " creado."));
            });
        } catch (IllegalArgumentException ex) { Mensajes.error(this, ex); }
    }

    private void eliminarSeleccionado() {
        T seleccion = seleccionado();
        if (seleccion == null) { avisoSeleccion(); return; }
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar el registro " + id(seleccion) + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        ejecutar(() -> {
            if (!delete(id(seleccion))) throw new IllegalArgumentException("El registro ya no existe. Refresca el listado.");
            return true;
        }, resultado -> {
            tabla.clearSelection(); limpiar(); notificar.run();
            JOptionPane.showMessageDialog(this, "Registro eliminado.");
        });
    }

    private void avisoSeleccion() { JOptionPane.showMessageDialog(this, "Selecciona una fila del listado."); }

    /** Deja espacio para dirección y nombre, sin desperdiciar ancho en el ID. */
    private void ajustarColumnas() {
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            String nombre = tabla.getColumnName(i);
            int ancho = switch (nombre) {
                case "ID" -> 55;
                case "Dirección", "Pedido", "Nombre" -> 320;
                case "Repartidor" -> 300;
                case "Fecha", "Hora" -> 110;
                default -> 140;
            };
            tabla.getColumnModel().getColumn(i).setPreferredWidth(ancho);
        }
    }

    /** JDBC se ejecuta en segundo plano; done() actualiza componentes en el EDT. */
    private <R> void ejecutar(Callable<R> tarea, Consumer<R> exito) {
        ocupado = true;
        habilitarCampos(getContentPane(), false);
        for (JButton boton : botones) boton.setEnabled(false);
        tabla.setEnabled(false);
        new SwingWorker<R, Void>() {
            @Override protected R doInBackground() throws Exception { return tarea.call(); }
            @Override protected void done() {
                try { exito.accept(get()); }
                catch (Exception ex) { Mensajes.error(VentanaGestion.this, ex); }
                finally {
                    ocupado = false;
                    habilitarCampos(getContentPane(), true);
                    for (JButton boton : botones) boton.setEnabled(true);
                    tabla.setEnabled(true);
                    if (refrescoPendiente) { refrescoPendiente = false; refrescar(); }
                }
            }
        }.execute();
    }

    private void habilitarCampos(java.awt.Container panel, boolean habilitar) {
        for (java.awt.Component componente : panel.getComponents()) {
            if (componente instanceof JTextField || componente instanceof JComboBox<?>) componente.setEnabled(habilitar);
            else if (componente instanceof java.awt.Container contenedor) habilitarCampos(contenedor, habilitar);
        }
    }

    protected void actualizarRelaciones() { }
    protected void aplicarFiltros() { }
    protected abstract int id(T entidad);
    protected abstract Object[] fila(T entidad);
    protected abstract T leer(int id);
    protected abstract void mostrar(T entidad);
    protected abstract void limpiar();
    protected abstract List<T> consultar() throws Exception;
    protected abstract int create(T entidad) throws Exception;
    protected abstract boolean update(T entidad) throws Exception;
    protected abstract boolean delete(int id) throws Exception;
}
