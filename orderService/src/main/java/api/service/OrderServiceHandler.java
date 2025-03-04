package api.service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import backend.customer.Customer;
import backend.database.CustomerDatabase;
import api.utils.*;
import backend.Group;
import backend.Order;
import backend.database.GroupDatabase;
import backend.exceptions.CustomerNotFoundException;
import backend.exceptions.GroupNotFoundException;
import backend.exceptions.RestaurantNotFoundException;
import backend.facade.OrderFacade;
import backend.location.Location;
import backend.location.LocationFactory;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;


public class OrderServiceHandler implements HttpHandler {
    private static final String PATH = "/api/order/";

    public OrderServiceHandler() {

        ApiRegistry.registerRoute("GET", PATH + "groups", this::getGroups); // good

        ApiRegistry.registerRoute("POST", PATH + "groups", this::askToCreateGroup); // nouveau attribut créé (wtf wtf)

        ApiRegistry.registerRoute("PUT", PATH + "groups", this::addCustomerToGroup); // double les membres

        ApiRegistry.registerRoute("DELETE", PATH + "groups", this::askToDeleteGroup); // good

        ApiRegistry.registerRoute("PUT", PATH + "customer/location", this::changeCustomerLocation); // good

        ApiRegistry.registerRoute("DELETE", PATH + "customer/location", this::deleteCustomerLocation); // good

        ApiRegistry.registerRoute("POST", PATH + "order", this::createOrderBuilder); // nouveau attribut créé (wtf wtf)

        ApiRegistry.registerRoute("PUT", PATH + "order/customer/location", this::changeOrderBuilderLocation); // good

        ApiRegistry.registerRoute("PUT", PATH + "order/customer/delivery", this::changeOrderBuilderDelivery);

        ApiRegistry.registerRoute("PUT", PATH + "order/customer/menu", this::addMenuToOrderBuilder);

        ApiRegistry.registerRoute("PUT", PATH + "order/customer/history", this::closeOrderBuilder);

        ApiRegistry.registerRoute("GET", PATH + "customer", this::getCustomer); // good
    }

    private static final Logger logger = Logger.getLogger(OrderServiceHandler.class.getName());


    private void getCustomer(HttpExchange httpExchange) throws IOException {
        URI requestURI = httpExchange.getRequestURI();
        String query = requestURI.getQuery();
        Customer customer;
        Map<String, String> queryParams = QueryUtils.parseQuery(query);
        int customerId = Integer.parseInt(queryParams.get("customerId"));

        try {
            logger.info("Fetching customer with ID: " + customerId);
            customer = CustomerDatabase.getInstance().getCustomerById(customerId);
            String response = JaxsonUtils.toJson(customer);
            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);

            httpExchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            httpExchange.sendResponseHeaders(HttpUtils.OK_CODE, responseBytes.length);
            httpExchange.getResponseBody().write(responseBytes);
        } catch (CustomerNotFoundException e) {
            logger.warning("Customer not found: " + e.getMessage());
            String response = e.getMessage();
            httpExchange.sendResponseHeaders(HttpUtils.NOT_FOUND_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } catch (Exception e) {
            logger.severe("Internal server error: " + e.getMessage());
            String response = "Internal server error";
            httpExchange.sendResponseHeaders(HttpUtils.INTERNAL_SERVER_ERROR_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } finally {
            httpExchange.getResponseBody().close();
        }
    }

    private void getGroups(HttpExchange httpExchange) throws IOException {
        URI requestURI = httpExchange.getRequestURI();
        String query = requestURI.getQuery();
        Group group;
        Map<String, String> queryParams = QueryUtils.parseQuery(query);
        int groupId = Integer.parseInt(queryParams.get("groupId"));

        try {
            logger.info("Fetching group with ID: " + groupId);
            group = GroupDatabase.getInstance().getGroupById(groupId);
            String response = JaxsonUtils.toJson(group);
            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);

            httpExchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            httpExchange.sendResponseHeaders(HttpUtils.OK_CODE, responseBytes.length);
            httpExchange.getResponseBody().write(responseBytes);
        } catch (GroupNotFoundException e) {
            logger.warning("Group not found: " + e.getMessage());
            String response = e.getMessage();
            httpExchange.sendResponseHeaders(HttpUtils.NOT_FOUND_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } catch (CustomerNotFoundException e) {
            logger.warning("Customer not found: " + e.getMessage());
            String response = e.getMessage();
            httpExchange.sendResponseHeaders(HttpUtils.INTERNAL_SERVER_ERROR_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } catch (Exception e) {
            logger.severe("Internal server error: " + e.getMessage());
            String response = "Internal server error";
            httpExchange.sendResponseHeaders(HttpUtils.INTERNAL_SERVER_ERROR_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } finally {
            httpExchange.getResponseBody().close();
        }
    }

    private void askToCreateGroup(HttpExchange httpExchange) throws IOException {
        URI requestURI = httpExchange.getRequestURI();
        String query = requestURI.getQuery();
        boolean responseSent = false; // Flag pour suivre l'état des en-têtes

        try {
            // Extraction des paramètres de la requête
            Map<String, String> queryParams = QueryUtils.parseQuery(query);
            LocalTime delivery = LocalTime.parse(queryParams.get("delivery"));
            Location location = LocationFactory.createLocation(queryParams.get("location"));
            int customerId = Integer.parseInt(queryParams.get("customerId"));

            logger.info("Creating group with delivery: " + delivery + ", location: " + location + ", customerId: " + customerId);

            // Création du groupe

            Group group = OrderFacade.getInstance().createGroup(delivery, location, customerId).toGroup();
            // Envoi de la réponse de succès
            String response = JaxsonUtils.toJson(group.toData());
            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);

            httpExchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            httpExchange.sendResponseHeaders(HttpUtils.OK_CODE, responseBytes.length);
            httpExchange.getResponseBody().write(responseBytes);
            responseSent = true; // La réponse a été envoyée
            logger.info("Response sent successfully.");
        } catch (CustomerNotFoundException e) {
            logger.warning("Customer not found: " + e.getMessage());
            sendErrorResponse(httpExchange, HttpUtils.NOT_FOUND_CODE, e.getMessage(), responseSent);
        } catch (GroupNotFoundException e) {
            logger.warning("Group not found: " + e.getMessage());
            sendErrorResponse(httpExchange, HttpUtils.NOT_FOUND_CODE, e.getMessage(), responseSent);
        } catch (Exception e) {
            logger.severe("Internal server error: " + e.getMessage());
            sendErrorResponse(httpExchange, HttpUtils.INTERNAL_SERVER_ERROR_CODE, "Internal server error", responseSent);
        } finally {
            if (!responseSent) {
                try {
                    httpExchange.getResponseBody().close();
                } catch (IOException ex) {
                    logger.severe("Failed to close response body: " + ex.getMessage());
                }
            }
        }
    }

    private void sendErrorResponse(HttpExchange httpExchange, int statusCode, String message, boolean responseSent) {
        if (responseSent) {
            logger.severe("Cannot send error response: headers already sent.");
            return; // Empêche un double envoi
        }

        try {
            byte[] responseBytes = message.getBytes(StandardCharsets.UTF_8);
            httpExchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
            httpExchange.sendResponseHeaders(statusCode, responseBytes.length);
            httpExchange.getResponseBody().write(responseBytes);

            logger.info("Error response sent: " + message);
        } catch (IOException e) {
            logger.severe("Failed to send error response: " + e.getMessage());
        }
    }


    private void addCustomerToGroup(HttpExchange httpExchange) throws IOException, CustomerNotFoundException, GroupNotFoundException {
        URI requestURI = httpExchange.getRequestURI();
        String query = requestURI.getQuery();
        boolean success;
        Map<String, String> queryParams = QueryUtils.parseQuery(query);
        int groupId = Integer.parseInt(queryParams.get("groupId"));
        int customerId = Integer.parseInt(queryParams.get("customerId"));

        try {
            logger.info("Adding customer with ID: " + customerId + " to group with ID: " + groupId);
            success = OrderFacade.getInstance().joinGroup(groupId, customerId);
            String response = JaxsonUtils.toJson(success);
            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);

            httpExchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            httpExchange.sendResponseHeaders(HttpUtils.OK_CODE, responseBytes.length);
            httpExchange.getResponseBody().write(responseBytes);
        } catch (CustomerNotFoundException e) {
            logger.warning("Customer not found: " + e.getMessage());
            String response = e.getMessage();
            httpExchange.sendResponseHeaders(HttpUtils.NOT_FOUND_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } catch (GroupNotFoundException e) {
            logger.warning("Group not found: " + e.getMessage());
            String response = e.getMessage();
            httpExchange.sendResponseHeaders(HttpUtils.NOT_FOUND_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } catch (Exception e) {
            logger.severe("Internal server error: " + e.getMessage());
            String response = "Internal server error";
            httpExchange.sendResponseHeaders(HttpUtils.INTERNAL_SERVER_ERROR_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } finally {
            httpExchange.getResponseBody().close();
        }
    }

    private void askToDeleteGroup(HttpExchange httpExchange) throws IOException {
        URI requestURI = httpExchange.getRequestURI();
        String query = requestURI.getQuery();
        boolean success;
        Map<String, String> queryParams = QueryUtils.parseQuery(query);
        int groupId = Integer.parseInt(queryParams.get("groupId"));
        int customerId = Integer.parseInt(queryParams.get("customerId"));
        LocalTime delivery = LocalTime.parse(queryParams.get("delivery"));

        try {
            logger.info("Deleting group with ID: " + groupId + ", customerId: " + customerId + ", delivery: " + delivery);
            success = OrderFacade.getInstance().closeGroup(groupId, customerId, delivery);
            String response = JaxsonUtils.toJson(success);
            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);

            httpExchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            httpExchange.sendResponseHeaders(HttpUtils.OK_CODE, responseBytes.length);
            httpExchange.getResponseBody().write(responseBytes);
        } catch (GroupNotFoundException e) {
            logger.warning("Group not found: " + e.getMessage());
            String response = e.getMessage();
            httpExchange.sendResponseHeaders(HttpUtils.NOT_FOUND_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } catch (Exception e) {
            logger.severe("Internal server error: " + e.getMessage());
            String response = "Internal server error";
            httpExchange.sendResponseHeaders(HttpUtils.INTERNAL_SERVER_ERROR_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } finally {
            httpExchange.getResponseBody().close();
        }
    }

    private void changeCustomerLocation(HttpExchange httpExchange) throws IOException {
        URI requestURI = httpExchange.getRequestURI();
        String query = requestURI.getQuery();
        boolean success;
        Map<String, String> queryParams = QueryUtils.parseQuery(query);
        String location = queryParams.get("location");
        int customerId = Integer.parseInt(queryParams.get("customerId"));

        try {
            logger.info("Changing location for customer with ID: " + customerId + " to location: " + location);
            success = OrderFacade.getInstance().registerLocation(location, customerId);
            String response = JaxsonUtils.toJson(CustomerDatabase.getInstance().getCustomerById(customerId).getLocations());
            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
            httpExchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            httpExchange.sendResponseHeaders(HttpUtils.OK_CODE, responseBytes.length);
            httpExchange.getResponseBody().write(responseBytes);
        } catch (CustomerNotFoundException e) {
            logger.warning("Customer not found: " + e.getMessage());
            String response = e.getMessage();
            httpExchange.sendResponseHeaders(HttpUtils.NOT_FOUND_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } catch (Exception e) {
            logger.severe("Internal server error: " + e.getMessage());
            String response = "Internal server error";
            httpExchange.sendResponseHeaders(HttpUtils.INTERNAL_SERVER_ERROR_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } finally {
            httpExchange.getResponseBody().close();
        }
    }

    private void deleteCustomerLocation(HttpExchange httpExchange) throws IOException {
        URI requestURI = httpExchange.getRequestURI();
        String query = requestURI.getQuery();
        boolean success;
        Map<String, String> queryParams = QueryUtils.parseQuery(query);
        int locationIndex = Integer.parseInt(queryParams.get("locationIndex"));
        int customerId = Integer.parseInt(queryParams.get("customerId"));

        try {
            logger.info("Deleting location with index: " + locationIndex + " for customer with ID: " + customerId);
            success = OrderFacade.getInstance().deleteLocation(locationIndex, customerId);
            String response = JaxsonUtils.toJson(success);
            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);

            httpExchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            httpExchange.sendResponseHeaders(HttpUtils.OK_CODE, responseBytes.length);
            httpExchange.getResponseBody().write(responseBytes);
        } catch (CustomerNotFoundException e) {
            logger.warning("Customer not found: " + e.getMessage());
            String response = e.getMessage();
            httpExchange.sendResponseHeaders(HttpUtils.NOT_FOUND_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } catch (Exception e) {
            logger.severe("Internal server error: " + e.getMessage());
            String response = "Internal server error";
            httpExchange.sendResponseHeaders(HttpUtils.INTERNAL_SERVER_ERROR_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } finally {
            httpExchange.getResponseBody().close();
        }
    }

    private void createOrderBuilder(HttpExchange httpExchange) throws IOException {
        URI requestURI = httpExchange.getRequestURI();
        String query = requestURI.getQuery();
        Order.Builder.Data orderBuilder;
        Map<String, String> queryParams = QueryUtils.parseQuery(query);
        int restaurantId = Integer.parseInt(queryParams.get("restaurantId"));
        int customerId = Integer.parseInt(queryParams.get("customerId"));

        try {
            logger.info("Creating order builder for restaurant ID: " + restaurantId + ", customer ID: " + customerId);
            orderBuilder = OrderFacade.getInstance().initOrder(restaurantId, customerId);
            logger.info("Order builder created: " + orderBuilder);
            String response = JaxsonUtils.toJson(orderBuilder);
            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);

            httpExchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            httpExchange.sendResponseHeaders(HttpUtils.OK_CODE, responseBytes.length);
            httpExchange.getResponseBody().write(responseBytes);
        } catch (CustomerNotFoundException e) {
            logger.warning("Customer not found: " + e.getMessage());
            String response = e.getMessage();
            httpExchange.sendResponseHeaders(HttpUtils.NOT_FOUND_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } catch (RestaurantNotFoundException e) {
            logger.warning("Restaurant not found: " + e.getMessage());
            String response = e.getMessage();
            httpExchange.sendResponseHeaders(HttpUtils.NOT_FOUND_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } catch (Exception e) {
            logger.severe("Internal server error: " + e.getMessage());
            String response = "Internal server error";
            httpExchange.sendResponseHeaders(HttpUtils.INTERNAL_SERVER_ERROR_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } finally {
            httpExchange.getResponseBody().close();
        }
    }

    private void changeOrderBuilderLocation(HttpExchange httpExchange) throws IOException {
        URI requestURI = httpExchange.getRequestURI();
        String query = requestURI.getQuery();
        Order.Builder.Data orderBuilder;
        Map<String, String> queryParams = QueryUtils.parseQuery(query);
        int locationIndex = Integer.parseInt(queryParams.get("locationIndex"));
        int customerId = Integer.parseInt(queryParams.get("customerId"));

        try {
            logger.info("Changing order location for customer with ID: " + customerId + " to location index: " + locationIndex);
            orderBuilder = OrderFacade.getInstance().setOrderLocation(locationIndex, customerId);
            logger.info("Order location changed: " + orderBuilder);
            String response = JaxsonUtils.toJson(orderBuilder);
            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);

            httpExchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            httpExchange.sendResponseHeaders(HttpUtils.OK_CODE, responseBytes.length);
            httpExchange.getResponseBody().write(responseBytes);
        } catch (CustomerNotFoundException e) {
            logger.warning("Customer not found: " + e.getMessage());
            String response = e.getMessage();
            httpExchange.sendResponseHeaders(HttpUtils.NOT_FOUND_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } catch (Exception e) {
            logger.severe("Internal server error: " + e.getMessage());
            String response = "Internal server error";
            httpExchange.sendResponseHeaders(HttpUtils.INTERNAL_SERVER_ERROR_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } finally {
            httpExchange.getResponseBody().close();
        }
    }

    private void changeOrderBuilderDelivery(HttpExchange httpExchange) throws IOException {
        URI requestURI = httpExchange.getRequestURI();
        String query = requestURI.getQuery();
        Order.Builder.Data orderBuilder;
        Map<String, String> queryParams = QueryUtils.parseQuery(query);
        LocalTime delivery = LocalTime.parse(queryParams.get("delivery"));
        int customerId = Integer.parseInt(queryParams.get("customerId"));

        try {
            logger.info("Changing delivery date for customer with ID: " + customerId + " to delivery: " + delivery);
            orderBuilder = OrderFacade.getInstance().setDeliveryDate(delivery, customerId);
            logger.info("Delivery date changed: " + orderBuilder);
            String response = JaxsonUtils.toJson(orderBuilder);
            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);

            httpExchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            httpExchange.sendResponseHeaders(HttpUtils.OK_CODE, responseBytes.length);
            httpExchange.getResponseBody().write(responseBytes);
        } catch (CustomerNotFoundException e) {
            logger.warning("Customer not found: " + e.getMessage());
            String response = e.getMessage();
            httpExchange.sendResponseHeaders(HttpUtils.NOT_FOUND_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } catch (Exception e) {
            logger.severe("Internal server error: " + e.getMessage());
            String response = "Internal server error";
            httpExchange.sendResponseHeaders(HttpUtils.INTERNAL_SERVER_ERROR_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } finally {
            httpExchange.getResponseBody().close();
        }
    }

    private void addMenuToOrderBuilder(HttpExchange httpExchange) throws IOException {
        URI requestURI = httpExchange.getRequestURI();
        String query = requestURI.getQuery();
        Order.Builder.Data orderBuilder;
        Map<String, String> queryParams = QueryUtils.parseQuery(query);
        String menuName = queryParams.get("menuName").replace("_", " ");
        int customerId = Integer.parseInt(queryParams.get("customerId"));

        try {
            logger.info("Adding menu: " + menuName + " to order for customer with ID: " + customerId);
            orderBuilder = OrderFacade.getInstance().addMenuToOrder(menuName, customerId);
            String response = JaxsonUtils.toJson(orderBuilder);
            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);

            httpExchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            httpExchange.sendResponseHeaders(HttpUtils.OK_CODE, responseBytes.length);
            httpExchange.getResponseBody().write(responseBytes);
        } catch (CustomerNotFoundException e) {
            logger.warning("Customer not found: " + e.getMessage());
            String response = e.getMessage();
            httpExchange.sendResponseHeaders(HttpUtils.NOT_FOUND_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } catch (RestaurantNotFoundException e) {
            logger.warning("Restaurant not found: " + e.getMessage());
            String response = e.getMessage();
            httpExchange.sendResponseHeaders(HttpUtils.NOT_FOUND_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } catch (Exception e) {
            logger.severe("Internal server error: " + e.getMessage());
            String response = "Internal server error";
            httpExchange.sendResponseHeaders(HttpUtils.INTERNAL_SERVER_ERROR_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } finally {
            httpExchange.getResponseBody().close();
        }
    }

    private void closeOrderBuilder(HttpExchange httpExchange) throws CustomerNotFoundException, RestaurantNotFoundException, IOException {
        URI requestURI = httpExchange.getRequestURI();
        String query = requestURI.getQuery();
        boolean success;
        Map<String, String> queryParams = QueryUtils.parseQuery(query);
        int customerId = Integer.parseInt(queryParams.get("customerId"));

        try {

        logger.info("Closing order builder for customer with ID: " + customerId);
        success = OrderFacade.getInstance().validateAndPayOrder(customerId);
        String response = JaxsonUtils.toJson(success);
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);

        httpExchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        httpExchange.sendResponseHeaders(HttpUtils.OK_CODE, responseBytes.length);
        httpExchange.getResponseBody().write(responseBytes);
        } catch (CustomerNotFoundException e) {
            logger.warning("Customer not found: " + e.getMessage());
            String response = e.getMessage();
            httpExchange.sendResponseHeaders(HttpUtils.NOT_FOUND_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } catch (RestaurantNotFoundException e) {
            logger.warning("Restaurant not found: " + e.getMessage());
            String response = e.getMessage();
            httpExchange.sendResponseHeaders(HttpUtils.NOT_FOUND_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } catch (Exception e) {
            logger.severe("Internal server error: " + e.getMessage());
            String response = "Internal server error";
            httpExchange.sendResponseHeaders(HttpUtils.INTERNAL_SERVER_ERROR_CODE, response.length());
            httpExchange.getResponseBody().write(response.getBytes());
        } finally {
            httpExchange.getResponseBody().close();
        }

    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Accept, X-Requested-With, Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization");

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        String requestMethod = exchange.getRequestMethod();
        String requestPath = exchange.getRequestURI().getPath();

        for (RouteInfo route : ApiRegistry.getRoutes()) {
            if (route.matches(requestMethod, requestPath)) {
                try {
                    route.getHandler().handle(exchange);
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
}
