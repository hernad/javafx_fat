import py4j.GatewayServer;

public class EntryPoint {

    public static void run_gui(String[] args) {
        HelloFX.main(args);
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("java.runtime.version"));
        GatewayServer server = new GatewayServer(new EntryPoint());
        server.start();
    }
}
