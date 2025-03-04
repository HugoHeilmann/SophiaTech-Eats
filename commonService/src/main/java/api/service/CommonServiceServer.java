package api.service;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class CommonServiceServer {

    public static final int PORT = 8082;
    public static final String PATH = "/api/common/";

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext(PATH, new CommonServiceHandler());
        server.setExecutor(null);
        server.start();
    }
}