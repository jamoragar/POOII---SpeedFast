public class Main {

    public static void main(String[] args) {

        System.out.println("=== SISTEMA DE TRANSPORTE ===");

        System.out.println("\n--- BUS ---");

        Bus bus = new Bus("BUS-101");
        bus.procesarViaje();


        System.out.println("\n--- METRO ---");

        Metro metro = new Metro("METRO-202");
        metro.procesarViaje();
    }
}