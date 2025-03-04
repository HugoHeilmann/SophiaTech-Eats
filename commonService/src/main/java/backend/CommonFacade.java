package backend;

import backend.database.RestaurantDatabase;
import backend.exceptions.CustomerNotFoundException;
import backend.exceptions.RestaurantNotFoundException;

import java.util.List;
import java.util.Optional;

public final class CommonFacade {

    private static CommonFacade instance;

    public static CommonFacade getInstance() {
        if (instance == null) {
            instance = new CommonFacade();
        }
        return instance;
    }

    private CommonFacade() {
    }


    /**
     * Usage scenarios considered :
     * <p>
     * [N1] Any internet user can browse menus from different campus restaurants.
     *
     * @param restaurantId a restaurant id
     * @return the menus array of the restaurant
     */
    public Menu.Data[] browseMenus(int restaurantId, Optional<Integer> customerId) throws RestaurantNotFoundException, CustomerNotFoundException {
        List<Menu> menus;
        if (customerId.isPresent()) {
            menus = RestaurantDatabase.getInstance().getRestaurantById(restaurantId).getMenuByAvailability(customerId.get());
        } else {
            menus = RestaurantDatabase.getInstance().getRestaurantById(restaurantId).getMenus();
        }
        Menu.Data[] MenusData = new Menu.Data[menus.size()];
        for (int i = 0; i < MenusData.length; i++) {
            MenusData[i] = menus.get(i).toData();
        }
        return MenusData;
    }

    
    /**
     * Usage scenarios considered :
     * <p>
     * [N2] Any internet user can search for a restaurant by name, type of food, or availability.
     *
     * @param name         a restaurant's name
     * @param foodType     a restaurant's type of food
     * @param availability a restaurant's availability
     * @return an array of restaurant matching the name, the type of food or the availability
     */
    public Restaurant.Data[] browseRestaurants(String name, FoodType foodType, String availability) {
        List<Restaurant> restaurants = RestaurantDatabase.getInstance().getFilteredRestaurants(name, foodType, availability);
        Restaurant.Data[] restaurantsData = new Restaurant.Data[restaurants.size()];
        for (int i = 0; i < restaurantsData.length; i++) {
            restaurantsData[i] = restaurants.get(i).toData();
        }
        return restaurantsData;
    }
}