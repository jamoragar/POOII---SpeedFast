package vista;
import controlador.ControladorLibros;
import modelo.*;
import util.Validaciones;
import javax.swing.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.time.LocalDate;

public final class VentanaLibros extends VentanaGestion<Libro> {
    private JPanel panel;
    private JTable tabla;
    private JButton btnCrear, btnActualizar, btnEliminar, btnLimpiar, btnRefrescar;
    private JTextField txtId;
    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtIsbn;
    private JTextField txtEditorial;
    private JTextField txtStock;
    private JComboBox<Categoria> cmbCategoria;
    private final ControladorLibros gestion;
    public VentanaLibros(ControladorLibros gestion,Runnable notificar) {
        super("Libros",gestion,notificar);this.gestion=gestion;
        configurar(panel,tabla,new String[]{"ID", "Título", "Autor", "ISBN", "Editorial", "Stock", "Categoría"},btnCrear,btnActualizar,btnEliminar,btnLimpiar,btnRefrescar);
        permisos(gestion.esBibliotecario(),gestion.esBibliotecario(),gestion.esBibliotecario());ajustarPermisos();
        limpiar();
    }
    @Override protected Libro leer(int id) { Categoria categoria=(Categoria)cmbCategoria.getSelectedItem();
        if(categoria==null)throw new IllegalArgumentException("Selecciona una categoría.");
        return new Libro(id,Validaciones.texto(txtTitulo.getText(),"Título",200),Validaciones.texto(txtAutor.getText(),"Autor",100),
                Validaciones.texto(txtIsbn.getText(),"ISBN",20),Validaciones.texto(txtEditorial.getText(),"Editorial",100),Validaciones.stock(txtStock.getText()),categoria.getId()); }
    @Override protected Object[] fila(Libro e) { return new Object[]{e.getId(),e.getTitulo(),e.getAutor(),e.getIsbn(),e.getEditorial(),e.getStock(),Combos.nombre(categorias,e.getIdCategoria())}; }
    @Override protected void mostrar(Libro e) {txtId.setText(""+e.getId());txtTitulo.setText(e.getTitulo());txtAutor.setText(e.getAutor());txtIsbn.setText(e.getIsbn());txtEditorial.setText(e.getEditorial());txtStock.setText(""+e.getStock());Combos.seleccionar(cmbCategoria,e.getIdCategoria());}
    @Override protected void limpiar() {txtId.setText("");txtTitulo.setText("");txtAutor.setText("");txtIsbn.setText("");txtEditorial.setText("");txtStock.setText("0");cmbCategoria.setSelectedIndex(-1);}
    @Override protected List<Libro> consultar() throws Exception { var nuevas=gestion.categorias();var lista=gestion.listar();categorias=nuevas;return lista; }
    private List<Categoria> categorias=List.of();
    @Override protected void actualizarRelaciones() { Combos.reponer(cmbCategoria,categorias,false); }
    @Override protected void ajustarPermisos() {
        boolean editar=gestion.esBibliotecario();
        txtTitulo.setEditable(editar);txtAutor.setEditable(editar);txtIsbn.setEditable(editar);txtEditorial.setEditable(editar);txtStock.setEditable(editar);cmbCategoria.setEnabled(editar);
    }
}
