package api.service;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class PaymentServiceServer {

    public static final int PORT = 8083;
    public static final String PATH = "/api/payment/";

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext(PATH, new PaymentServiceHandler());

        server.setExecutor(null);
        server.start();
    }

}