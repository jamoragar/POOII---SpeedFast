package modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Entrega {

    private final int id;
    private final int idPedido;
    private final int idRepartidor;
    private final LocalDate fecha;
    private final LocalTime hora;

    public Entrega(int idPedido, int idRepartidor) {
        this(0, idPedido, idRepartidor, LocalDate.now(), LocalTime.now().withNano(0));
    }

    public Entrega(int id, int idPedido, int idRepartidor, LocalDate fecha, LocalTime hora) {
        if (idPedido <= 0 || idRepartidor <= 0 || fecha == null || hora == null) {
            throw new IllegalArgumentException("La entrega requiere pedido, repartidor, fecha y hora válidos.");
        }
        this.id = id;
        this.idPedido = idPedido;
        this.idRepartidor = idRepartidor;
        this.fecha = fecha;
        this.hora = hora;
    }

    public int getId() { return id; }
    public int getIdPedido() { return idPedido; }
    public int getIdRepartidor() { return idRepartidor; }
    public LocalDate getFecha() { return fecha; }
    public LocalTime getHora() { return hora; }
}
