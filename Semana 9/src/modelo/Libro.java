package modelo;
public final class Libro implements Entidad {
    private final int id;
    private final String titulo;
    private final String autor;
    private final String isbn;
    private final String editorial;
    private final int stock;
    private final int idCategoria;
    public Libro(int id, String titulo, String autor, String isbn, String editorial, int stock, int idCategoria) {
        this.id=id;
        this.titulo=titulo;
        this.autor=autor;
        this.isbn=isbn;
        this.editorial=editorial;
        this.stock=stock;
        this.idCategoria=idCategoria;
    }
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getIsbn() { return isbn; }
    public String getEditorial() { return editorial; }
    public int getStock() { return stock; }
    public int getIdCategoria() { return idCategoria; }
    @Override public String toString() { return id+" - "+titulo; }
}
