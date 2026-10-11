package vista;
import controlador.ControladorReportes;
import modelo.*;
import util.Mensajes;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public final class VentanaReportes extends JFrame implements Actualizable {
    private JPanel panel;
    private JTable tabla;
    private JComboBox<String> cmbTipo;
    private JComboBox<Estudiante> cmbEstudiante;
    private JButton btnConsultar;
    private final ControladorReportes controlador;
    private boolean ocupado,pendiente;
    public VentanaReportes(ControladorReportes controlador) {
        super("Biblioteca — Reportes e historial");this.controlador=controlador;
        setContentPane(panel);setSize(1180,650);setLocationByPlatform(true);setDefaultCloseOperation(HIDE_ON_CLOSE);
        cmbTipo.setModel(new DefaultComboBoxModel<>(controlador.esBibliotecario()?new String[]{"Libros más prestados","Historial por estudiante","Préstamos activos"}:new String[]{"Historial por estudiante"}));
        tabla.setModel(new DefaultTableModel(){@Override public boolean isCellEditable(int r,int c){return false;}});tabla.setAutoCreateRowSorter(true);
        btnConsultar.addActionListener(e->refrescar());
    }
    @Override public boolean estaOcupado(){return ocupado;}
    private record Resultado(List<Estudiante> estudiantes,Reporte reporte,int elegido) { }
    @Override public void refrescar() {
        if(ocupado){pendiente=true;return;}
        String tipo=(String)cmbTipo.getSelectedItem();Estudiante elegido=(Estudiante)cmbEstudiante.getSelectedItem();
        int id=elegido==null?-1:elegido.getId();ocupado=true;btnConsultar.setEnabled(false);cmbTipo.setEnabled(false);cmbEstudiante.setEnabled(false);
        new SwingWorker<Resultado,Void>() {
            @Override protected Resultado doInBackground() throws Exception {
                List<Estudiante> estudiantes=controlador.estudiantes();
                int nuevo=estudiantes.stream().anyMatch(e->e.getId()==id)?id:estudiantes.isEmpty()?-1:estudiantes.get(0).getId();
                if("Historial por estudiante".equals(tipo) && nuevo<0)throw new IllegalArgumentException("No hay estudiantes disponibles para consultar.");
                return new Resultado(estudiantes,controlador.consultar(tipo,nuevo),nuevo);
            }
            @Override protected void done() {
                try {
                    Resultado datos=get();Combos.reponer(cmbEstudiante,datos.estudiantes(),true);Combos.seleccionar(cmbEstudiante,datos.elegido());
                    DefaultTableModel modelo=(DefaultTableModel)tabla.getModel();modelo.setDataVector(datos.reporte().filas().toArray(Object[][]::new),datos.reporte().columnas());
                } catch(Exception ex){Mensajes.error(VentanaReportes.this,ex);}
                finally {ocupado=false;btnConsultar.setEnabled(true);cmbTipo.setEnabled(true);cmbEstudiante.setEnabled(controlador.esBibliotecario());if(pendiente){pendiente=false;refrescar();}}
            }
        }.execute();
    }
}
