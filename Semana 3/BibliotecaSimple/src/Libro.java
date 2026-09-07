public class Libro extends MaterialBiblioteca implements Prestable, Renovable{

    private String autor;

    public Libro(int codigo, String titulo, String autor) {
        super(codigo, titulo);
        this.autor = autor;
    }

    @Override
    public int calcularDiasPrestamo() {
        return 7;
    }

    @Override
    public void prestar() {
        System.out.println("El libro fue prestado");
    }

    @Override
    public void renovar() {
        System.out.println("El prestamo del libro fue renovado");
    }

    //Sobrecarga del metodo
    public void prestar(String nombreUsuario) {
        System.out.println("El libro fue prestado a: " + nombreUsuario);
    }

    public void mostrarAutor(){
        System.out.println("Autor: " + autor);
    }
}
