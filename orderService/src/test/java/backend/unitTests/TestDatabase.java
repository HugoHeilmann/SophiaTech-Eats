package backend.unitTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import backend.FoodType;
import backend.Restaurant;
import backend.database.CustomerDatabase;
import backend.database.RestaurantDatabase;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestDatabase {

    CustomerDatabase database = CustomerDatabase.getInstance();
    RestaurantDatabase restaurantDatabase = RestaurantDatabase.getInstance();

    @BeforeEach
    void setUp() {
        RestaurantDatabase.getInstance().initializeRestaurants();
        CustomerDatabase.getInstance().initializeCustomers();
    }

    @Test
    void testInitialization() {
        Assertions.assertEquals(5, RestaurantDatabase.getInstance().getRestaurants().size());
    }


    @Test
    void testGetFilteredRestaurants() {
        List<Restaurant> filteredRestaurantsByName = restaurantDatabase.getFilteredRestaurants("Subway", null, null);
        assertEquals(1, filteredRestaurantsByName.size());
        List<Restaurant> filteredRestaurantsByNameContained = restaurantDatabase.getFilteredRestaurants("Elysée", null, null);
        assertEquals(1, filteredRestaurantsByNameContained.size());

        List<Restaurant> filteredRestaurantsByFoodType = restaurantDatabase.getFilteredRestaurants(null, FoodType.VEGGIE, null);
        assertEquals(4, filteredRestaurantsByFoodType.size());

        List<Restaurant> filteredRestaurantsByAvailability = restaurantDatabase.getFilteredRestaurants(null, null, "310");
        assertEquals(0, filteredRestaurantsByAvailability.size());
    }
}