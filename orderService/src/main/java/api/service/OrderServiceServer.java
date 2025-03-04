package api.service;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class OrderServiceServer {

    public static final int PORT = 8081;
    public static final String PATH = "/api/order/";

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext(PATH, new OrderServiceHandler());

        server.setExecutor(null);
        server.start();
    }

}