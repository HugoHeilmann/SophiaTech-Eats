package api.service;

import api.utils.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import backend.FoodType;
import backend.Menu;
import backend.Restaurant;
import backend.exceptions.CustomerNotFoundException;
import backend.exceptions.RestaurantNotFoundException;
import backend.CommonFacade;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

public class CommonServiceHandler implements HttpHandler {
    Logger logger = Logger.getLogger(CommonServiceHandler.class.getName());

    public CommonServiceHandler() {
        ApiRegistry.registerRoute("GET", "/api/common/restaurants", this::answerWithRestaurants);
        ApiRegistry.registerRoute("GET", "/api/common/menus", this::answerWithMenus);
    }


    private void answerWithRestaurants(HttpExchange exchange) throws IOException {
        logger.info("Answering with restaurants");
        try {
            URI requestURI = exchange.getRequestURI();
            String query = requestURI.getQuery();

            Restaurant.Data[] restaurants;
            Map<String, String> queryParams = QueryUtils.parseQuery(query);

            FoodType foodType = (queryParams.get("foodType") != null) ? FoodType.valueOf(queryParams.get("foodType")) : null;

            restaurants = CommonFacade.getInstance().browseRestaurants(queryParams.get("name"), foodType, queryParams.get("availability"));

            sendResponse(exchange, HttpUtils.OK_CODE, HttpUtils.APPLICATION_JSON, JaxsonUtils.toJson(restaurants));
        } catch (Exception exception) {
            sendResponse(exchange, HttpUtils.INTERNAL_SERVER_ERROR_CODE, HttpUtils.TEXT_PLAIN, exception.getMessage());
        }
    }


    private void answerWithMenus(HttpExchange exchange) throws IOException {
        try {
            URI requestURI = exchange.getRequestURI();
            String query = requestURI.getQuery();

            Menu.Data[] menus;
            Map<String, String> queryParams = QueryUtils.parseQuery(query);

            int restaurantId = Integer.parseInt(queryParams.get("restaurantId"));
            Optional<Integer> customerId = Optional.ofNullable((queryParams.get("customerId") != null) ? Integer.parseInt(queryParams.get("customerId")) : null);
            
            menus = CommonFacade.getInstance().browseMenus(restaurantId, customerId);

            sendResponse(exchange, HttpUtils.OK_CODE, HttpUtils.APPLICATION_JSON, JaxsonUtils.toJson(menus));
        } catch (RestaurantNotFoundException | CustomerNotFoundException exception) {
            sendResponse(exchange, HttpUtils.NOT_FOUND_CODE, HttpUtils.TEXT_PLAIN, exception.getMessage());
        } catch (Exception exception) {
            sendResponse(exchange, HttpUtils.INTERNAL_SERVER_ERROR_CODE, HttpUtils.TEXT_PLAIN, exception.getMessage());
        }
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
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "https://localhost:4200");
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