public class Bus extends VehiculoTransporte{

    public Bus(String codigoVehiculo) {
        super(codigoVehiculo);
    }

    @Override
    public void validarPasaje() {
        System.out.println("Pasaje validado por tarjeta BIP");
    }
}
