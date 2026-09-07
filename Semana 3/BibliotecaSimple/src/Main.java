//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Libro libro = new Libro(1, "El principito", "Anotoine de Saint-Exupery");

        Revista revista = new Revista(2, "Ciencia hoy", 15);


        // Demostrar polimorfismo
        MaterialBiblioteca[] materiales = {libro, revista};

        for (MaterialBiblioteca material : materiales){
            material.mostrarResumen();
        }






    }
}