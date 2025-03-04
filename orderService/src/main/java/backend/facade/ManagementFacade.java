package backend.facade;

import backend.Menu;
import backend.Restaurant;
import backend.database.RestaurantDatabase;
import backend.exceptions.RestaurantNotFoundException;

import java.time.DayOfWeek;

public final class ManagementFacade {

    /* Singleton design pattern */

    private static ManagementFacade instance;

    public static ManagementFacade getInstance() {
        if (instance == null) {
            instance = new ManagementFacade();
        }
        return instance;
    }

    private ManagementFacade() {
    }


    /* Facade design pattern */

    /**
     * Usage scenarios considered :
     * <p>
     * [R2] Restaurant managers can update their opening hours and menu offerings. For each menu
     * item, a manager specifies the necessary preparation time. They must specify their production
     * capacity for each opening time in terms of the number of personnel per 30-minute slot;
     * otherwise, it is considered 0.
     *
     * @param restaurantId a restaurant's id
     * @param day          a day of week
     * @param openingHour  the opening hour of the day
     * @param closingHour  the closing hour of the day
     * @return the restaurant after changes occurs
     */
    public Restaurant.Data updateSchedule(int restaurantId, DayOfWeek day, String openingHour, String closingHour) throws RestaurantNotFoundException {
        Restaurant restaurant = RestaurantDatabase.getInstance().getRestaurantById(restaurantId);
        restaurant.setSchedule(day, openingHour, closingHour);
        restaurant.updateOrderChargeable();
        return restaurant.toData();
    }

    public Restaurant.Data updateScheduleForAllDays(int restaurantId, String openingHour, String closingHour) throws RestaurantNotFoundException {
        Restaurant restaurant = RestaurantDatabase.getInstance().getRestaurantById(restaurantId);
        restaurant.setScheduleForAllDays(openingHour, closingHour);
        restaurant.updateOrderChargeable();
        return restaurant.toData();
    }


    /**
     * Allows a manager from a given restaurant to update the number of staff members available at a given day and half-hour.
     * <p>
     * Usage scenarios considered :
     * <p>
     * [R2] Restaurant managers can update their opening hours and menu offerings. For each menu
     * item, a manager specifies the necessary preparation time. They must specify their production
     * capacity for each opening time in terms of the number of personnel per 30-minute slot;
     * otherwise, it is considered 0.
     *
     * @param restaurantId represents the restaurant
     * @param dayNumber    represents the day of the week
     * @param hour         represents the hour for which the manager will change the capacity
     * @param minute       represents the minutes for which the manager will change the capacity
     * @param staff        represents the number of staff member available
     */
    public void updateCapacity(int restaurantId, int dayNumber, int hour, int minute, int staff) throws RestaurantNotFoundException {
        Restaurant restaurant = RestaurantDatabase.getInstance().getRestaurantById(restaurantId);
        int slotNumber = minute < 30 ? hour * 2 + 1 : hour * 2 + 2;
        restaurant.setCapacity(dayNumber, slotNumber, staff);
        restaurant.updateOrderChargeable();
    }


    /**
     * Allow a manager to add a new menu to his restaurant if it's not already in it
     * <p>
     * Usage scenarios considered :
     * <p>
     * [R2] Restaurant managers can update their opening hours and menu offerings. For each menu
     * item, a manager specifies the necessary preparation time. They must specify their production
     * capacity for each opening time in terms of the number of personnel per 30-minute slot;
     * otherwise, it is considered 0.
     *
     * @param restaurantId represents the restaurant
     * @param menu         represents the new menu to add to the restaurant
     */
    public void addMenu(int restaurantId, Menu menu) throws RestaurantNotFoundException {
        Restaurant restaurant = RestaurantDatabase.getInstance().getRestaurantById(restaurantId);
        restaurant.addMenu(menu);
        restaurant.updateOrderChargeable();
    }


    /**
     * Allow a manager to delete a menu from his restaurant
     * <p>
     * Usage scenarios considered :
     * <p>
     * [R2] Restaurant managers can update their opening hours and menu offerings. For each menu
     * item, a manager specifies the necessary preparation time. They must specify their production
     * capacity for each opening time in terms of the number of personnel per 30-minute slot;
     * otherwise, it is considered 0.
     *
     * @param restaurantId represents the restaurant
     * @param menu         represents the menu to delete
     */
    public void deleteMenu(int restaurantId, Menu menu) throws RestaurantNotFoundException {
        Restaurant restaurant = RestaurantDatabase.getInstance().getRestaurantById(restaurantId);
        restaurant.deleteMenu(menu);
        restaurant.updateOrderChargeable();
    }


    /**
     * Allow a manager to update a menu already existing in the restaurant
     * <p>
     * Usage scenarios considered :
     * <p>
     * [R2] Restaurant managers can update their opening hours and menu offerings. For each menu
     * item, a manager specifies the necessary preparation time. They must specify their production
     * capacity for each opening time in terms of the number of personnel per 30-minute slot;
     * otherwise, it is considered 0.
     *
     * @param restaurantId      represents the restaurant
     * @param menuName          represents the menu the manager wants to change
     * @param attributeToChange represents the attribute to modify from the menu
     * @param newData           represents the new value to put int the menu
     */
    public void updateMenu(int restaurantId, String menuName, String attributeToChange, Object newData) throws RestaurantNotFoundException {
        Restaurant restaurant = RestaurantDatabase.getInstance().getRestaurantById(restaurantId);
        Menu menu = restaurant.getMenuByName(menuName);
        if (attributeToChange.equals("name") || attributeToChange.equals("description")) {
            try {
                String data = (String) newData;
                if (attributeToChange.equals("name")) {
                    menu.setName(data);
                } else {
                    menu.setDescription(data);
                }
            } catch (ClassCastException e) {
                System.err.println("Erreur : Le type de newData n'est pas une chaîne de caractères.");
            }
        } else {
            try {
                if (attributeToChange.equals("price")) {
                    double data = (double) newData;
                    menu.setPrice(data);
                } else {
                    int data = (int) newData;
                    menu.setPreparation(data);
                    restaurant.updateOrderChargeable();
                }
            } catch (ClassCastException e) {
                System.err.println("Erreur : Le type de newData n'est pas un double.");
            }
        }
    }
}