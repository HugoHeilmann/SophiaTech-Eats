import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

public class GatewayHandler implements HttpHandler {
    Logger logger = Logger.getLogger(GatewayHandler.class.getName());
    public GatewayHandler() {
        ApiRegistry.registerRoute("GET", "/gateway/common/.*", this::forwardToCommonService);
        ApiRegistry.registerRoute("(GET|PUT|POST|DELETE)", "/gateway/order/.*", this::forwardToOrderService);
        ApiRegistry.registerRoute("GET|POST", "/gateway/auth/.*", this::forwardToAuthServer);
    }


    private void forwardToCommonService(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath().replace("/gateway", "/api");
        String backendUrl = "http://localhost:8082" + path + "?" + exchange.getRequestURI().getQuery();

        forwardRequest(exchange, backendUrl);
    }

    private void forwardToOrderService(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath().replace("/gateway/order", "/api/order");
        String backendUrl = "http://localhost:8081" + path + "?" + exchange.getRequestURI().getQuery();

        forwardRequest(exchange, backendUrl);
    }

    private void forwardToAuthServer(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath().replace("/gateway/auth", "/api/auth");
        String backendUrl = "http://localhost:8084" + path + "?" + exchange.getRequestURI().getQuery();
        forwardRequest(exchange, backendUrl);
    }


    private void forwardRequest(HttpExchange exchange, String backendUrl) throws IOException {
        logger.info("Forwarding request to " + backendUrl);
        HttpURLConnection connection = (HttpURLConnection) new URL(backendUrl).openConnection();
        connection.setRequestMethod(exchange.getRequestMethod());

        // Forward request headers
        exchange.getRequestHeaders().forEach((key, values) -> {
            for (String value : values) {
                connection.addRequestProperty(key, value);
            }
        });

        // Forward request body if applicable
        if (exchange.getRequestMethod().equalsIgnoreCase("POST") ||
                exchange.getRequestMethod().equalsIgnoreCase("PUT") ||
                exchange.getRequestMethod().equalsIgnoreCase("DELETE")) {
            connection.setDoOutput(true);
            try (InputStream inputStream = exchange.getRequestBody();
                 OutputStream outputStream = connection.getOutputStream()) {
                byte[] requestBody = inputStream.readAllBytes();
                outputStream.write(requestBody);
            }
        }

        connection.connect();

        // Forward response headers and cookies
        int responseCode = connection.getResponseCode();
        Headers responseHeaders = exchange.getResponseHeaders();

        connection.getHeaderFields().forEach((key, values) -> {
            if (key != null) {
                if (key.equalsIgnoreCase("Set-Cookie")) {
                    // Add multiple values for Set-Cookie headers
                    for (String value : values) {
                        responseHeaders.add(key, value);
                    }
                } else {
                    // Replace or set other headers (prevent duplication)
                    responseHeaders.set(key, String.join(", ", values));
                }
            }
        });

        // Forward response body
        InputStream inputStream = (responseCode >= 200 && responseCode < 300) ? connection.getInputStream() : connection.getErrorStream();
        byte[] response = inputStream.readAllBytes();

        sendResponse(exchange, responseCode, connection.getContentType(), response);
    }

    private void sendResponse(HttpExchange exchange, int code, String contentType, byte[] response) throws IOException {
        exchange.getResponseHeaders().add(HttpUtils.CONTENT_TYPE, contentType + "; charset=utf-8");
        exchange.sendResponseHeaders(code, response.length);
        exchange.getResponseBody().write(response);
        exchange.getResponseBody().close();
    }



    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "https://localhost:4200");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Accept, X-Requested-With, Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization");
        exchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");
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
