public class Revista extends MaterialBiblioteca implements Prestable{

    private int numeroEdicion;

    public Revista(int codigo, String titulo, int numeroEdicion) {
        super(codigo, titulo);
        this.numeroEdicion = numeroEdicion;
    }

    @Override
    public int calcularDiasPrestamo() {
        return 3;
    }

    @Override
    public void prestar() {
        System.out.printf("La revista fue prestada");
    }

    public void mostrarEdicion(){
        System.out.println("Numero de edicion: " + numeroEdicion);
    }
}
