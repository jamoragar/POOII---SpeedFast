package vista;
import controlador.ControladorPrestamos;
import modelo.*;
import util.Validaciones;
import javax.swing.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.time.LocalDate;

public final class VentanaPrestamos extends VentanaGestion<Prestamo> {
    private JPanel panel;
    private JTable tabla;
    private JButton btnCrear, btnActualizar, btnEliminar, btnLimpiar, btnRefrescar, btnDevolver;
    private JTextField txtId;
    private JComboBox<Estudiante> cmbEstudiante;
    private JComboBox<Libro> cmbLibro;
    private JTextField txtFecha;
    private JTextField txtVencimiento;
    private JTextField txtEstado;
    private JTextField txtReal;
    private final ControladorPrestamos gestion;
    public VentanaPrestamos(ControladorPrestamos gestion,Runnable notificar) {
        super("Prestamos",gestion,notificar);this.gestion=gestion;
        configurar(panel,tabla,new String[]{"ID", "Estudiante", "Libro", "Préstamo", "Vencimiento", "Estado", "Devolución real", "Atraso"},btnCrear,btnActualizar,btnEliminar,btnLimpiar,btnRefrescar);
        permisos(true,gestion.esBibliotecario(),gestion.esBibliotecario());
        btnDevolver.addActionListener(ev->{
            Prestamo prestamo=seleccionado();if(prestamo==null){avisoSeleccion();return;}
            ejecutar(()->{gestion.devolver(prestamo.getId());return true;},ok->{limpiarSeleccion();notificarCambio();JOptionPane.showMessageDialog(this,"Devolución registrada. Stock actualizado.");});
        });ajustarPermisos();
        limpiar();
    }
    @Override protected Prestamo leer(int id) { Estudiante estudiante=(Estudiante)cmbEstudiante.getSelectedItem();Libro libro=(Libro)cmbLibro.getSelectedItem();
        if(estudiante==null || libro==null)throw new IllegalArgumentException("Selecciona estudiante y libro.");
        LocalDate fecha=Validaciones.fecha(txtFecha.getText()),vencimiento=Validaciones.fecha(txtVencimiento.getText());
        if(vencimiento.isBefore(fecha))throw new IllegalArgumentException("El vencimiento no puede ser anterior al préstamo.");
        return new Prestamo(id,estudiante.getId(),libro.getId(),fecha,vencimiento,false,null); }
    @Override protected Object[] fila(Prestamo e) { return new Object[]{e.getId(),Combos.nombre(estudiantes,e.getIdEstudiante()),Combos.nombre(libros,e.getIdLibro()),e.getFechaPrestamo(),e.getFechaDevolucion(),e.getDevuelto()?"Devuelto":"Activo",e.getFechaDevolucionReal()==null?(e.getDevuelto()?"Sin fecha registrada":"—"):e.getFechaDevolucionReal(),e.atraso(LocalDate.now())}; }
    @Override protected void mostrar(Prestamo e) {txtId.setText(""+e.getId());Combos.seleccionar(cmbEstudiante,e.getIdEstudiante());Combos.seleccionar(cmbLibro,e.getIdLibro());txtFecha.setText(e.getFechaPrestamo().toString());txtVencimiento.setText(e.getFechaDevolucion().toString());txtEstado.setText(e.getDevuelto()?"Devuelto":"Activo");txtReal.setText(e.getFechaDevolucionReal()==null?(e.getDevuelto()?"Sin fecha registrada":""):e.getFechaDevolucionReal().toString());}
    @Override protected void limpiar() {txtId.setText("");cmbEstudiante.setSelectedIndex(-1);cmbLibro.setSelectedIndex(-1);if(!gestion.esBibliotecario() && cmbEstudiante.getItemCount()>0)cmbEstudiante.setSelectedIndex(0);txtFecha.setText(LocalDate.now().toString());txtVencimiento.setText(LocalDate.now().plusDays(7).toString());txtEstado.setText("Nuevo préstamo");txtReal.setText("");}
    @Override protected List<Prestamo> consultar() throws Exception { var nuevosEstudiantes=gestion.estudiantes();var nuevosLibros=gestion.libros();var lista=gestion.listar();estudiantes=nuevosEstudiantes;libros=nuevosLibros;return lista; }
    private List<Estudiante> estudiantes=List.of();
    private List<Libro> libros=List.of();
    @Override protected void actualizarRelaciones() {
        Combos.reponer(cmbEstudiante,estudiantes,!gestion.esBibliotecario());Combos.reponer(cmbLibro,libros,false);
    }
    @Override protected String confirmacionEliminar(Prestamo dato) {
        return dato.getDevuelto()?"¿Eliminar este préstamo del historial? No cambiará el stock.":"¿Cancelar y eliminar este préstamo? El ejemplar volverá al stock.";
    }
    @Override protected void ajustarPermisos() {
        cmbEstudiante.setEnabled(gestion.esBibliotecario());txtFecha.setEditable(gestion.esBibliotecario());txtVencimiento.setEditable(gestion.esBibliotecario());
    }
}
