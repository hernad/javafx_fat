import py4j.GatewayServer;
import java.util.*;

public class EntryPoint {

    private Stack stack;

    public EntryPoint() {
      stack = new Stack();
      stack.push("Initial Item");
    }

    public Stack getStack() {
        String[] args = {};

        HelloFX.main_startup(args);
        return stack;
    }

    public Stack getStack2() {
        String[] args = {};
        stack.push("Later");
        HelloFX.main_runlater(args);
        return stack;
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("java.runtime.version"));
        GatewayServer server = new GatewayServer(new EntryPoint());
        server.start();
    }
}
