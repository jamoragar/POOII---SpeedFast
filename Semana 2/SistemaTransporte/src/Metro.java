public class Metro extends VehiculoTransporte{

    public Metro(String codigoVehiculo) {
        super(codigoVehiculo);
    }

    @Override
    public void validarPasaje() {
        System.out.println("Pasaje validado por ticket");
    }
}
