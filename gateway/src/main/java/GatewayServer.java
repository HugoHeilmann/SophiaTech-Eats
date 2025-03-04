import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class GatewayServer {
    public static final int PORT = 8080;
    public static final String PATH = "/gateway/";

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext(PATH, new GatewayHandler());
        server.setExecutor(null);
        server.start();
    }
}