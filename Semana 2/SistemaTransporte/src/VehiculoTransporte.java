public abstract class VehiculoTransporte {
    private String codigoVehiculo;

    public VehiculoTransporte(String codigoVehiculo) {
        this.codigoVehiculo = codigoVehiculo;
    }

    // Metodo concreto
    public void mostrarIdentificacion(){
        System.out.println("Codigo del vehiculo: " + codigoVehiculo);
    }

    // Metodo abstracto
    public abstract void validarPasaje();

    // Metodo concreto
    public void iniciarRecorrido(){
        System.out.println("El vehiculo inicia su recorrido.");
    }

    // Template Method
    public void procesarViaje(){
        mostrarIdentificacion();
        validarPasaje();
        iniciarRecorrido();
    }

}
