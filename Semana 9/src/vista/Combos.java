package vista;
import modelo.Entidad;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.util.List;
/** Conserva identidad, no posición, al actualizar entidades relacionadas. */
final class Combos {
    private Combos() { }
    static void seleccionar(JComboBox<? extends Entidad> combo,int id) {
        combo.setSelectedIndex(-1);
        for(int i=0;i<combo.getItemCount();i++)if(combo.getItemAt(i).getId()==id){combo.setSelectedIndex(i);return;}
    }
    static <T extends Entidad> void reponer(JComboBox<T> combo,List<T> datos,boolean seleccionarPrimero) {
        Object anterior=combo.getSelectedItem();int id=anterior instanceof Entidad e?e.getId():-1;
        DefaultComboBoxModel<T> modelo=new DefaultComboBoxModel<>();datos.forEach(modelo::addElement);combo.setModel(modelo);
        seleccionar(combo,id);if(seleccionarPrimero && combo.getSelectedIndex()<0 && !datos.isEmpty())combo.setSelectedIndex(0);
    }
    static String nombre(List<? extends Entidad> datos,int id) {
        return datos.stream().filter(e->e.getId()==id).findFirst().map(Object::toString).orElse("ID "+id);
    }
}
