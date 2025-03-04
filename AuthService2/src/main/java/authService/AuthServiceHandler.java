package authService;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dependencies.AuthFacade;
import dependencies.databse.CustomerDatabase;
import exceptions.CustomerNotFoundException;
import exceptions.GroupNotFoundException;
import exceptions.RestaurantNotFoundException;
import utils.*;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;


public class AuthServiceHandler implements HttpHandler {
    private static final String PATH = "/api/auth/";

    public AuthServiceHandler() {
        ApiRegistry.registerRoute("POST", PATH + "login", this::loginWithCredentials); // login with credentials
        ApiRegistry.registerRoute("POST", PATH + "register", this::register); // register
        ApiRegistry.registerRoute("POST", PATH + "google-login", this::loginWithGoogle); // google login
        ApiRegistry.registerRoute("POST", PATH + "facebook-login", this::loginWithFacebook); // facebook login
        ApiRegistry.registerRoute("GET", PATH + "validate-token", this::validateToken); // validate token
        ApiRegistry.registerRoute("POST", PATH + "logout", this::logout); // logout
    }

    private static final Logger logger = Logger.getLogger(AuthServiceHandler.class.getName());

    private void loginWithCredentials(HttpExchange httpExchange, String param) throws IOException {
        Map<String, String> queryBody = QueryUtils.parseJsonBody(httpExchange.getRequestBody());
        String email = queryBody.get("email");
        String password = queryBody.get("password");

        try {
            logger.info("Attempting login for email: " + email);
            String token = AuthFacade.getInstance().loginWithCredentials(email, password);
            if (token != null) {
                // Set the JWT cookie with HttpOnly, Secure, and SameSite
                String cookie = "JWT=" + token + "; HttpOnly; Secure; Max-Age=3600; Path=/; Domain=localhost; SameSite=None";
                int id = CustomerDatabase.getInstance().getCustomerByEmail(email).getId();

                httpExchange.getResponseHeaders().add("Set-Cookie", cookie);

                String response = "{\"status\": \"success\", \"id\": \"" + id + "\"}";
                byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
                httpExchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
                httpExchange.sendResponseHeaders(HttpUtils.OK_CODE, responseBytes.length);
                httpExchange.getResponseBody().write(responseBytes);
            } else {
                String response = "{\"status\": \"failure\", \"message\": \"Invalid credentials\"}";
                byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
                httpExchange.sendResponseHeaders(HttpUtils.UNAUTHORIZED_CODE, responseBytes.length);
                httpExchange.getResponseBody().write(responseBytes);
            }
        } catch (Exception e) {
            logger.severe("Error during login: " + e.getMessage());
            String response = "{\"status\": \"failure\", \"message\": \"Internal server error\"}";
            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
            httpExchange.sendResponseHeaders(HttpUtils.INTERNAL_SERVER_ERROR_CODE, responseBytes.length);
            httpExchange.getResponseBody().write(responseBytes);
        } finally {
            httpExchange.getResponseBody().close();
        }
    }



    private void register(HttpExchange httpExchange, String param) throws IOException {
        Map<String, String> queryBody = QueryUtils.parseJsonBody(httpExchange.getRequestBody());
        String email = queryBody.get("email");
        String password = queryBody.get("password");
        String name = queryBody.get("name");

        try {
            logger.info("Attempting to register user with email: " + email);
            boolean success = AuthFacade.getInstance().register(name,email, password);

            String response;
            int responseCode;

            if (success) {
                response = "{\"status\": \"success\"}";
                responseCode = HttpUtils.OK_CODE;
            } else {
                response = "{\"status\": \"failure\", \"message\": \"Registration failed\"}";
                responseCode = HttpUtils.BAD_REQUEST_CODE;
            }

            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
            httpExchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            httpExchange.sendResponseHeaders(responseCode, responseBytes.length);
            httpExchange.getResponseBody().write(responseBytes);

        } catch (Exception e) {
            logger.severe("Error during registration: " + e.getMessage());
            String response = "{\"status\": \"failure\", \"message\": \"Internal server error\"}";
            httpExchange.sendResponseHeaders(HttpUtils.INTERNAL_SERVER_ERROR_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } finally {
            httpExchange.getResponseBody().close();
        }
    }

    private void loginWithGoogle(HttpExchange httpExchange, String param) throws IOException {
        URI requestURI = httpExchange.getRequestURI();
        String query = requestURI.getQuery();
        Map<String, String> queryParams = QueryUtils.parseQuery(query);
        String googleCredential = queryParams.get("googleCredential");

        try {
            logger.info("Attempting Google login with credential: " + googleCredential);
            boolean success = AuthFacade.getInstance().loginWithGoogle(googleCredential);
            if (success) {
                String response = "{\"status\": \"success\"}";
                byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
                httpExchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
                httpExchange.sendResponseHeaders(HttpUtils.OK_CODE, responseBytes.length);
                httpExchange.getResponseBody().write(responseBytes);
            } else {
                String response = "{\"status\": \"failure\", \"message\": \"Google login failed\"}";
                byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
                httpExchange.sendResponseHeaders(HttpUtils.UNAUTHORIZED_CODE, responseBytes.length);
                httpExchange.getResponseBody().write(responseBytes);
            }
        } catch (Exception e) {
            logger.severe("Error during Google login: " + e.getMessage());
            String response = "Internal server error";
            httpExchange.sendResponseHeaders(HttpUtils.INTERNAL_SERVER_ERROR_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } finally {
            httpExchange.getResponseBody().close();
        }
    }

    private void loginWithFacebook(HttpExchange httpExchange, String param) throws IOException {
        URI requestURI = httpExchange.getRequestURI();
        String query = requestURI.getQuery();
        Map<String, String> queryParams = QueryUtils.parseQuery(query);
        String facebookToken = queryParams.get("facebookToken");

        try {
            logger.info("Attempting Facebook login with token: " + facebookToken);
            boolean success = AuthFacade.getInstance().loginWithFacebook(facebookToken);
            if (success) {
                String response = "{\"status\": \"success\"}";
                byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
                httpExchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
                httpExchange.sendResponseHeaders(HttpUtils.OK_CODE, responseBytes.length);
                httpExchange.getResponseBody().write(responseBytes);
            } else {
                String response = "{\"status\": \"failure\", \"message\": \"Facebook login failed\"}";
                byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
                httpExchange.sendResponseHeaders(HttpUtils.UNAUTHORIZED_CODE, responseBytes.length);
                httpExchange.getResponseBody().write(responseBytes);
            }
        } catch (Exception e) {
            logger.severe("Error during Facebook login: " + e.getMessage());
            String response = "Internal server error";
            httpExchange.sendResponseHeaders(HttpUtils.INTERNAL_SERVER_ERROR_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } finally {
            httpExchange.getResponseBody().close();
        }
    }

    private void validateToken(HttpExchange httpExchange, String param) throws IOException {
        try {
            logger.info("Validating token");
            String token = getTokenFromRequest(httpExchange);

            boolean valid = AuthFacade.getInstance().validateToken(token);
            String email = JwtUtil.getEmailFromToken(token);
            int id = CustomerDatabase.getInstance().getCustomerByEmail(email).getId();
            String response;
            byte[] responseBytes;


            if (valid) {
                response = "{\"status\": \"valid\", \"id\": \"" + id + "\"}";
                responseBytes = response.getBytes(StandardCharsets.UTF_8);
                httpExchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
                httpExchange.sendResponseHeaders(HttpUtils.OK_CODE, responseBytes.length);
            } else {
                response = "{\"status\": \"invalid\"}";
                responseBytes = response.getBytes(StandardCharsets.UTF_8);
                httpExchange.sendResponseHeaders(HttpUtils.UNAUTHORIZED_CODE, responseBytes.length);
            }

            httpExchange.getResponseBody().write(responseBytes);
        } catch (Exception e) {
            logger.severe("Error during token validation: " + e.getMessage());
            String response = "Internal server error";
            httpExchange.sendResponseHeaders(HttpUtils.INTERNAL_SERVER_ERROR_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } finally {
            httpExchange.getResponseBody().close();
        }
    }

    private void logout(HttpExchange httpExchange, String param) throws IOException {
        try {
            logger.info("Logging out");
            String token = getTokenFromRequest(httpExchange);
            boolean success = AuthFacade.getInstance().logout(token);
            if (success) {
                String response = "{\"status\": \"success\"}";
                byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
                httpExchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
                httpExchange.sendResponseHeaders(HttpUtils.OK_CODE, responseBytes.length);
                httpExchange.getResponseBody().write(responseBytes);
            } else {
                String response = "{\"status\": \"failure\", \"message\": \"Logout failed\"}";
                byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
                httpExchange.sendResponseHeaders(HttpUtils.BAD_REQUEST_CODE, responseBytes.length);
                httpExchange.getResponseBody().write(responseBytes);
            }
        } catch (Exception e) {
            logger.severe("Error during logout: " + e.getMessage());
            String response = "Internal server error";
            httpExchange.sendResponseHeaders(HttpUtils.INTERNAL_SERVER_ERROR_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } finally {
            httpExchange.getResponseBody().close();
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "https://localhost:4200");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Accept, X-Requested-With, Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization");
        exchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        String requestMethod = exchange.getRequestMethod();
        String requestPath = exchange.getRequestURI().getPath();
        for (RouteInfo route : ApiRegistry.getRoutes()) {
            if (route.matches(requestMethod, requestPath)) {
                Matcher matcher = route.getPathMatcher(requestPath);
                String param = "";
                if (matcher.find() && matcher.groupCount() > 0) {
                    param = matcher.group(1);
                }

                try {
                    route.getHandler().handle(exchange, param);
                } catch (GroupNotFoundException | RestaurantNotFoundException | CustomerNotFoundException e) {
                    throw new RuntimeException(e);
                }
                return; // Ensure to return after handling the request
            }
        }
        logger.info("Route not found: " + requestPath);
        exchange.sendResponseHeaders(404, 0);
        exchange.getResponseBody().close();
    }


    private String getTokenFromRequest(HttpExchange httpExchange) {
        // Check for token in Authorization header (Bearer token)
        String authorizationHeader = httpExchange.getRequestHeaders().getFirst("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);  // Extract token after "Bearer "
        }

        // Check for token in cookies
        Map<String, String> cookies = getCookiesFromRequest(httpExchange);
        return cookies.get("JWT");  // Return JWT token from cookies
    }

    private Map<String, String> getCookiesFromRequest(HttpExchange httpExchange) {
        String cookiesHeader = httpExchange.getRequestHeaders().getFirst("Cookie");
        return cookiesHeader != null ? parseCookies(cookiesHeader) : Map.of();  // Parse cookies or return empty map
    }

    private Map<String, String> parseCookies(String cookiesHeader) {
        // Logic to parse cookies from header into a map of cookie names and their values
        Map<String, String> cookiesMap = new HashMap<>();
        String[] cookies = cookiesHeader.split("; ");
        for (String cookie : cookies) {
            String[] cookieParts = cookie.split("=");
            if (cookieParts.length == 2) {
                cookiesMap.put(cookieParts[0], cookieParts[1]);
            }
        }
        return cookiesMap;
    }

}
