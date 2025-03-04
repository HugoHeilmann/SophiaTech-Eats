package backend.unitTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import backend.*;
import backend.database.RestaurantDatabase;
import backend.exceptions.CustomerNotFoundException;
import backend.exceptions.RestaurantNotFoundException;
import backend.facade.OrderFacade;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import static org.junit.Assert.*;

class TestRestaurant {
    RestaurantDatabase restaurantDatabase = RestaurantDatabase.getInstance();

    @BeforeEach
    void setUp() {
        RestaurantDatabase.getInstance().initializeRestaurants();
    }

    @Test
    void testGetter() {
        Restaurant subway = restaurantDatabase.getFilteredRestaurants("Subway", null, null).get(0);

        assertEquals(2, subway.getId());
        assertEquals("Subway", subway.getName());
        assertEquals("200 Avenue Roumanille, 06410 Biot", subway.getAddress());
        assertEquals("04 93 65 39 43", subway.getPhone());
        assertEquals("subway.biot@gmail.com", subway.getEmail());
        assertEquals(subway.getScheduleAtDay(0)[0], LocalTime.of(9, 0));
        assertEquals(subway.getScheduleAtDay(0)[1], LocalTime.of(22, 0));
        assertEquals(0, subway.getCapacity().getCapacityForSlot(0, 0));

        List<FoodType> types = subway.getFoodTypes();

        assertTrue(types.contains(FoodType.FAST_FOOD));
        assertTrue(types.contains(FoodType.VEGGIE));

        assertEquals("10% off for Students", subway.getStrategy().getOfferDescription());
    }


    @Test
    void testSetScheduleForAllDays() throws RestaurantNotFoundException {
        Restaurant restaurant = restaurantDatabase.getInstance().getRestaurantById(3);

        restaurant.setScheduleForAllDays("01", "18");
        for (int i = 0; i < 7; i++) {
            assertEquals(restaurant.getScheduleAtDay(i)[0].toString(), "01:00");
            assertEquals(restaurant.getScheduleAtDay(i)[1].toString(), "18:00");
        }
    }

    @Test
    void testSetSchedule() throws RestaurantNotFoundException {
        Restaurant restaurant = restaurantDatabase.getInstance().getRestaurantById(4);

        assertNotEquals(restaurant.getScheduleAtDay(0)[0].toString(), "01:00");
        restaurant.setSchedule(DayOfWeek.of(1), "01", "18");
        assertEquals(restaurant.getScheduleAtDay(0)[0].toString(), "01:00");
    }

    @Test
    void testUpdateCapacity() throws RestaurantNotFoundException {
        Restaurant restaurant = restaurantDatabase.getInstance().getRestaurantById(1);

        assertEquals(0, restaurant.getCapacity().getCapacityForSlot(0, 0));
        restaurant.setCapacity(0, 0, 718);
        assertEquals(718 * 300, restaurant.getCapacity().getCapacityForSlot(0, 0));
    }

    @Test
    void testAddDeleteMenu() throws RestaurantNotFoundException {
        Restaurant restaurant = restaurantDatabase.getInstance().getRestaurantById(1);

        assertEquals(4, restaurant.getMenus().size());
        Menu menu = new Menu(FoodType.FAST_FOOD, "Poulet", "Juste du poulet", 12, 60, "");
        assertFalse(restaurant.getMenus().contains(menu));
        restaurant.addMenu(menu);
        assertTrue(restaurant.getMenus().contains(menu));
        assertEquals(5, restaurant.getMenus().size());
        restaurant.deleteMenu(menu);
        assertFalse(restaurant.getMenus().contains(menu));
        assertEquals(4, restaurant.getMenus().size());
    }

    @Test
    void testOrderChargeable() throws RestaurantNotFoundException, CustomerNotFoundException, IOException {
        Restaurant restaurant = restaurantDatabase.getRestaurantById(1);
        int orderChargeable = restaurant.getOrderChargeable();
        OrderFacade.getInstance().initOrder(1, 0);
        assertEquals(orderChargeable - 1, restaurant.getOrderChargeable());
        OrderFacade.getInstance().validateAndPayOrder(0);
        assertEquals(orderChargeable, restaurant.getOrderChargeable());
    }

}