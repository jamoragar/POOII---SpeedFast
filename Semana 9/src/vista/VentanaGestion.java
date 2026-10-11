package vista;
import controlador.ControladorCRUD;
import modelo.Entidad;
import util.Mensajes;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.Component;
import java.awt.Container;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

/** Reutiliza eventos y tareas; cada formulario conserva sus campos y su archivo .form. */
public abstract class VentanaGestion<T extends Entidad> extends JFrame implements Actualizable {
    protected final ControladorCRUD<T> controlador;
    private final Runnable notificar;
    private JTable tabla;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> ordenador;
    private JButton[] botones;
    private final boolean[] permisos={true,true,true,true,true};
    private List<T> registros=List.of();
    private T originalEdicion;
    private boolean ocupado, sincronizando, refrescoPendiente;

    protected VentanaGestion(String titulo,ControladorCRUD<T> controlador,Runnable notificar) {
        super("Biblioteca — "+titulo);this.controlador=controlador;this.notificar=notificar;
        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }
    protected void configurar(JPanel panel,JTable tabla,String[] columnas,JButton crear,JButton actualizar,JButton eliminar,JButton limpiar,JButton refrescar) {
        this.tabla=tabla;botones=new JButton[]{crear,actualizar,eliminar,limpiar,refrescar};
        modelo=new DefaultTableModel(columnas,0) {
            @Override public boolean isCellEditable(int row,int col) { return false; }
            @Override public Class<?> getColumnClass(int col) { return col==0?Integer.class:Object.class; }
        };
        tabla.setModel(modelo);ordenador=new TableRowSorter<>(modelo);tabla.setRowSorter(ordenador);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(event -> {
            if(!event.getValueIsAdjusting() && !sincronizando && !ocupado) {
                T dato=seleccionado();if(dato!=null && (originalEdicion==null || originalEdicion.getId()!=dato.getId())) {originalEdicion=dato;mostrar(dato);}
            }
        });
        crear.addActionListener(ev->guardar(false));actualizar.addActionListener(ev->guardar(true));
        eliminar.addActionListener(ev->eliminarSeleccionado());
        limpiar.addActionListener(ev->limpiarSeleccion());refrescar.addActionListener(ev->refrescar());
        setContentPane(panel);setSize(1100,730);setLocationByPlatform(true);ajustarColumnas();
    }
    protected final void permisos(boolean crear,boolean actualizar,boolean eliminar) {
        permisos[0]=crear;permisos[1]=actualizar;permisos[2]=eliminar;
        for(int i=0;i<3;i++){botones[i].setVisible(permisos[i]);botones[i].setEnabled(permisos[i]&&!ocupado);}
    }
    @Override public final boolean estaOcupado() { return ocupado; }
    protected final T seleccionado() {
        int fila=tabla.getSelectedRow();return fila<0?null:registros.get(tabla.convertRowIndexToModel(fila));
    }
    protected final void limpiarSeleccion() {tabla.clearSelection();originalEdicion=null;limpiar();}
    @Override public final void refrescar() {
        if(ocupado){refrescoPendiente=true;return;}
        T seleccionado=seleccionado();
        ejecutar(this::consultar,nuevos->{
            sincronizando=true;
            try {
                registros=nuevos;Object[] cols=new Object[modelo.getColumnCount()];
                for(int i=0;i<cols.length;i++)cols[i]=modelo.getColumnName(i);
                var orden=ordenador.getSortKeys();
                modelo.setDataVector(nuevos.stream().map(this::fila).toArray(Object[][]::new),cols);
                ajustarColumnas();ordenador.setSortKeys(orden);actualizarRelaciones();
                tabla.clearSelection();
                if(seleccionado!=null)for(int i=0;i<nuevos.size();i++)if(nuevos.get(i).getId()==seleccionado.getId()){
                    int v=tabla.convertRowIndexToView(i);if(v>=0)tabla.setRowSelectionInterval(v,v);
                }
                // No reemplazar originalEdicion: conserva el stock que el usuario realmente vio.
            } finally {sincronizando=false;}
        });
    }
    private void guardar(boolean editar) {
        if(editar && (seleccionado()==null || originalEdicion==null)){avisoSeleccion();return;}
        try {
            T dato=leer(editar?originalEdicion.getId():0);
            Callable<Integer> accion=editar?prepararActualizacion(originalEdicion,dato):prepararCreacion(dato);
            ejecutar(accion,id->{limpiarSeleccion();notificar.run();JOptionPane.showMessageDialog(this,"Registro "+id+(editar?" actualizado.":" creado."),"Biblioteca",JOptionPane.INFORMATION_MESSAGE);});
        } catch(IllegalArgumentException ex) {Mensajes.error(this,ex);}
    }
    private void eliminarSeleccionado() {
        T dato=seleccionado();if(dato==null){avisoSeleccion();return;}
        if(JOptionPane.showConfirmDialog(this,confirmacionEliminar(dato),"Confirmar eliminación",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return;
        ejecutar(()->{controlador.eliminar(dato.getId());return true;},ok->{limpiarSeleccion();notificar.run();JOptionPane.showMessageDialog(this,"Registro eliminado.");});
    }
    protected String confirmacionEliminar(T e) {return "¿Eliminar el registro "+e.getId()+"?";}
    protected final void avisoSeleccion() {JOptionPane.showMessageDialog(this,"Selecciona una fila del listado.");}
    protected Callable<Integer> prepararCreacion(T dato) {return ()->controlador.crear(dato);}
    protected Callable<Integer> prepararActualizacion(T original,T dato) {return ()->{controlador.actualizar(original,dato);return dato.getId();};}
    protected final void notificarCambio() {notificar.run();}
    protected List<T> consultar() throws Exception {return controlador.listar();}
    protected void actualizarRelaciones() { }
    protected void ajustarPermisos() { }
    /** Todas las lecturas de campos se hacen antes de crear la tarea de JDBC. */
    protected final <R> void ejecutar(Callable<R> trabajo,Consumer<R> exito) {
        if(ocupado)return;ocupado=true;habilitar(getContentPane(),false);
        new SwingWorker<R,Void>() {
            @Override protected R doInBackground() throws Exception {return trabajo.call();}
            @Override protected void done() {
                try {exito.accept(get());}catch(Exception ex){Mensajes.error(VentanaGestion.this,ex);}
                finally {
                    ocupado=false;habilitar(getContentPane(),true);
                    for(int i=0;i<botones.length;i++)botones[i].setEnabled(permisos[i]);
                    ajustarPermisos();if(refrescoPendiente){refrescoPendiente=false;refrescar();}
                }
            }
        }.execute();
    }
    private void habilitar(Container padre,boolean valor) {
        for(Component c:padre.getComponents()){
            if(c instanceof JTextField || c instanceof JComboBox<?> || c instanceof JButton || c instanceof JTable)c.setEnabled(valor);
            else if(c instanceof Container hijo)habilitar(hijo,valor);
        }
    }
    private void ajustarColumnas() {
        for(int i=0;i<tabla.getColumnCount();i++) {
            var columna=tabla.getColumnModel().getColumn(i);
            if(i==0){columna.setPreferredWidth(50);columna.setMaxWidth(65);}
            else columna.setPreferredWidth(switch(tabla.getColumnName(i)) {case "Libro","Título","Nombre","Estudiante"->230;default->125;});
        }
    }
    protected abstract T leer(int id);
    protected abstract Object[] fila(T dato);
    protected abstract void mostrar(T dato);
    protected abstract void limpiar();
}
