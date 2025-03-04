package authService;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class AuthServiceServer {

    public static final int PORT = 8084;
    public static final String PATH = "/api/auth/";

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext(PATH, new AuthServiceHandler());
        server.setExecutor(null);
        server.start();
    }
}