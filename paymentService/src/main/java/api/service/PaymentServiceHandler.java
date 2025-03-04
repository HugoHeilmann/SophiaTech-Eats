package api.service;

import api.utils.ApiRegistry;
import api.utils.HttpUtils;
import api.utils.RouteInfo;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PaymentServiceHandler implements HttpHandler {

    public PaymentServiceHandler() {
        ApiRegistry.registerRoute("GET", "/api/payment/", this::pay);
    }


    private void pay(HttpExchange exchange) throws IOException {
        sendResponse(exchange, HttpUtils.TEAPOT_CODE, HttpUtils.TEXT_PLAIN, "Payment failed");
        System.out.println("PaymentServiceHandler");
    }


    private void sendResponse(HttpExchange exchange, int code, String contentType, String response) throws IOException {
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add(HttpUtils.CONTENT_TYPE, contentType + "; charset=utf-8");
        exchange.sendResponseHeaders(code, responseBytes.length);
        exchange.getResponseBody().write(responseBytes);
        exchange.getResponseBody().close();
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Accept, X-Requested-With, Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization");

        String requestMethod = exchange.getRequestMethod();
        String requestPath = exchange.getRequestURI().getPath();

        for (RouteInfo route : ApiRegistry.getRoutes()) {
            if (route.matches(requestMethod, requestPath)) {
                route.getHandler().handle(exchange);
                return;
            }
        }

        exchange.sendResponseHeaders(HttpUtils.NOT_FOUND_CODE, 0);
        exchange.getResponseBody().close();
    }

}