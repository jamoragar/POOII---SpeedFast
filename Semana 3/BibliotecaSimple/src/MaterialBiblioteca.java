public abstract class MaterialBiblioteca {

    //Definimos atributos en comun
    protected int codigo;
    protected String titulo;

    public MaterialBiblioteca(int codigo, String titulo) {
        this.codigo = codigo;
        this.titulo = titulo;
    }

    public void mostrarResumen(){
        System.out.println("Codigo: " + codigo);
        System.out.println("Titulo: " + titulo);
    }

    public abstract int calcularDiasPrestamo();
}
