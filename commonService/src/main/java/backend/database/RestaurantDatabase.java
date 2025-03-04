package backend.database;

import backend.strategy.*;
import com.fasterxml.jackson.core.type.TypeReference;
import backend.*;
import backend.exceptions.RestaurantNotFoundException;
import backend.utils.JsonUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RestaurantDatabase {

    /* Singleton design pattern */
    Logger logger = Logger.getLogger(CustomerDatabase.class.getName());
    private static RestaurantDatabase instance;
    private static final String DATABASE_FILE = "../database/restaurants.json";

    public static RestaurantDatabase getInstance() {
        if (instance == null) {
            instance = new RestaurantDatabase();
            instance.loadRestaurants();
        }
        return instance;
    }

    private RestaurantDatabase() {
    }

    private List<Restaurant> restaurants = new ArrayList<>();

    private void loadRestaurants() {
        try {
            File file = new File(DATABASE_FILE);
            restaurants = JsonUtil.readFromFile(file, new TypeReference<List<Restaurant>>() {
            });
        } catch (IOException e) {
            logger.info("Error loading restaurants: " + e.getMessage());
            initializeRestaurants();
        }
    }

    public void saveRestaurants() {
        try {
            JsonUtil.writeToFile(new File(DATABASE_FILE), restaurants);
        } catch (IOException e) {
            logger.info("Error saving restaurants: " + e.getMessage());
        }
    }

    public void initializeRestaurants() {
        Restaurant.resetId();
        restaurants = new ArrayList<>();

        restaurants.addAll(List.of(
                new Restaurant(
                        "Elysée Restauration",
                        "950 Route des Colles, 06410 Biot",
                        "04 94 00 00 00",
                        "elyseerestauration@gmail.com",
                        new ArrayList<>(List.of(
                                new Menu(FoodType.BURGER, "Cheeseburger originel",
                                        "Pain au graines de sésames, steak haché, fromage, ketchup, moutarde, frites",
                                        10, 600,
                                        "https://cdn.pixabay.com/photo/2023/09/23/15/56/ai-generated-8271284_1280.jpg"),
                                new Menu(FoodType.BURGER, "Cheeseburger double",
                                        "Pain au graines de sésames, steaks hachés, fromages, ketchup, moutarde, frites",
                                        15, 700,
                                        "https://cdn.pixabay.com/photo/2023/09/25/22/08/ai-generated-8276129_1280.jpg"),
                                new Menu(FoodType.VEGGIE, "Hamburger vert",
                                        "Pain au graines de sésames, steak haché végétarien, ketchup, moutarde, frites",
                                        12, 600,
                                        "https://cdn.pixabay.com/photo/2014/10/19/20/59/hamburger-494706_1280.jpg"),
                                new Menu(FoodType.VEGGIE, "Salade de pates", "Pates, tomates, salades, haricots verts",
                                        7, 900,
                                        "https://cdn.pixabay.com/photo/2016/03/17/22/56/pasta-1264056_1280.jpg"),
                                new Menu(FoodType.FAST_FOOD, "Menu enfant", "Nuggets de poulet, frites", 8, 400,
                                        "https://cdn.pixabay.com/photo/2014/01/16/01/48/chicken-nuggets-246180_1280.jpg"))),
                        new Capacity(),
                        new FidelityStrategy(),
                        "Un superbe restaurant !",
                        "https://scontent-mrs2-3.xx.fbcdn.net/v/t39.30808-1/305839631_568361811751792_5628456308390340610_n.jpg?stp=dst-jpg_s200x200_tt6&_nc_cat=111&ccb=1-7&_nc_sid=f4b9fd&_nc_ohc=9V8qkdUNC3kQ7kNvgHPpRhl&_nc_zt=24&_nc_ht=scontent-mrs2-3.xx&_nc_gid=AMSck_A0vlSqfoGQ9XRKWVP&oh=00_AYA7cXxs7c1qlhsioR3fY9bBVQM0gCYQsnciSMtFrmlSbA&oe=675CC0D4"),
                new Restaurant(
                        "L'Aliva",
                        "200 Avenue Roumanille, 06410 Biot",
                        "04 92 93 88 21",
                        "laliva@gmail.com",
                        new ArrayList<>(List.of(
                                new Menu(FoodType.PIZZA, "Reine",
                                        "Pâte, sauce tomate, jambons, champignons, mozzarella", 10, 700,
                                        "https://www.lebonfourapizza.com/wp-content/uploads/2022/06/regina.jpg"),
                                new Menu(FoodType.PIZZA, "4 fromages", "Pâte, sauce tomate, fromages", 12, 700,
                                        "https://www.cuisine-et-mets.com/wp-content/uploads/2017/07/Fotolia_121856819_Subscription_Monthly_XXL.jpg"),
                                new Menu(FoodType.PATE, "Pates bolognaise", "Pates, sauce bolognaise maison", 8, 500,
                                        "https://cdn.pixabay.com/photo/2015/05/27/18/53/spaghetti-787048_1280.jpg"),
                                new Menu(FoodType.PATE, "Pates carbonara", "Pates, crême fraiche, lardons", 8, 500,
                                        "https://cdn.pixabay.com/photo/2017/10/03/21/00/tagliatele-2814183_1280.jpg"))),
                        new Capacity(),
                        new HappyHourStrategy(),
                        "Spécialités Italiennes !",
                        "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/2d/c8/9e/db/caption.jpg"),
                new Restaurant(
                        "Subway",
                        "200 Avenue Roumanille, 06410 Biot",
                        "04 93 65 39 43",
                        "subway.biot@gmail.com",
                        new ArrayList<>(List.of(
                                new Menu(FoodType.FAST_FOOD, "Classique jambon",
                                        "Pain complet, américain en tranches, jambon, salade Iceberg, tomates, concombres, sauce Caesar",
                                        10, 300,
                                        "https://www.courier-journal.com/gcdn/presto/2023/07/06/USAT/ad75cb15-0e26-4872-bf93-1baeef175151-The_Beast.jpg?crop=7647,4302,x16,y0&width=3200&height=1801&format=pjpg&auto=webp"),
                                new Menu(FoodType.FAST_FOOD, "Signature Chicken Mexicali",
                                        "Pain complet, poulet façons rotisserie, mozarella, cheddar râpé", 15, 500,
                                        "https://www.restaurantmagazine.com/wp-content/uploads/2023/07/Subway-Titan-Turkey.jpg"),
                                new Menu(FoodType.VEGGIE, "Classique Veggie Delite",
                                        "Pain Parmesan Origan, américain en tranches, salade Iceberg, tomates, concombres, poivrons verts et rouges, oignons rouges et doux",
                                        12, 500,
                                        "https://vegnews.com/media/W1siZiIsIjM1OTk0L1ZlZ05ld3MuVmVnYW5BdFN1YldheS0yYi5qcGciXSxbInAiLCJ0aHVtYiIsIjE2MDB4OTQ2IyIseyJmb3JtYXQiOiJqcGcifV0sWyJwIiwib3B0aW1pemUiXV0/VegNews.VeganAtSubWay-2b.jpg?sha=04fdc4c889655458"))),
                        new Capacity(),
                        new StudentStrategy(),
                        "Chaîne de restaurant bien connue de tous !",
                        "https://www.nrn.com/sites/nrn.com/files/Subway%20Series%20Extended.jpg"),
                new Restaurant(
                        "L'assiette Nature",
                        "200 Avenue Roumanille espace 200, 06410 Biot",
                        "04 97 23 36 91",
                        "assiette.nature@gmail.com",
                        new ArrayList<>(List.of(
                                new Menu(FoodType.FAST_FOOD, "Menu enfant", "Steak haché, frites", 8, 200,
                                        "https://images.prismic.io/lesgrappes-mag/61a9147e-035f-4e28-b1d0-3b5b8c7111d1_steak-hach%C3%A9-frites.png?auto=compress"),
                                new Menu(FoodType.PATE, "Pates bolognaise", "pates, sauce tomate, viande hachée", 10,
                                        140,
                                        "https://cdn.pixabay.com/photo/2015/05/27/18/53/spaghetti-787048_1280.jpg"),
                                new Menu(FoodType.VEGGIE, "Salade de pommes de terre",
                                        "pommes de terre, cornichon, salade, percil", 10, 120,
                                        "https://cdn.pixabay.com/photo/2020/02/10/12/52/potato-salad-4836398_1280.jpg"),
                                new Menu(FoodType.PATE, "Raviolis maison", "Ravioli au boeuf, sauce secrète maison", 10,
                                        150,
                                        "https://cdn.pixabay.com/photo/2023/01/29/20/20/pasta-7754049_1280.jpg"))),
                        new Capacity(),
                        new StudentStrategy(),
                        "Laissez la nature venir à vous !",
                        "https://media-cdn.tripadvisor.com/media/photo-o/18/ca/b6/ee/salad-bar.jpg"),
                new Restaurant(
                        "Green 2.0",
                        "Centre commercial, " +
                                "Avenue Roumanille, 06410 Biot",
                        "04 92 90 14 20",
                        "green20@gmail.com",
                        new ArrayList<>(List.of(
                                new Menu(FoodType.PATE, "Pates huilées", "Pates, huile d'olive", 7, 100,
                                        "https://th.bing.com/th/id/OIP.bDo2uqZ4q6RIRhV95ARJVwHaE8?w=1024&h=683&rs=1&pid=ImgDetMain"),
                                new Menu(FoodType.VEGGIE, "Ratatouille", "Rats, tatouille", 15, 200,
                                        "https://cdn.pixabay.com/photo/2020/06/19/08/26/ratatouille-5316201_1280.jpg"))),
                        new Capacity(),
                        new HappyHourStrategy(),
                        "La restauration 2.0 !",
                        "https://lh3.googleusercontent.com/places/ANXAkqEA9Xq2_-cJmm4JqKffUQgJjeS6NG3eCBOuNBNnvJ9Y8bPrE-X_-m8rEY3OWfRdqgOLwF_UGDYOZfGECnEnaLDstK2bc9L4bcw=s1600-w300")));
        saveRestaurants();
    }

    public Restaurant getRestaurantById(int id) throws RestaurantNotFoundException {
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getId() == id) {
                return restaurant;
            }
        }
        throw new RestaurantNotFoundException("Restaurant with id " + id + " not found");
    }

    private List<Restaurant> getRestaurantsByName(String name) {
        if (name == null) {
            return restaurants;
        }
        return restaurants.stream().filter(restaurant -> restaurant.getName().contains(name)).toList();
    }

    private List<Restaurant> getRestaurantsByFoodType(FoodType foodType) {
        if (foodType == null) {
            return restaurants;
        }
        List<Restaurant> filteredRestaurantByFoodType = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            for (FoodType food : restaurant.getFoodTypes()) {
                if (food == foodType) {
                    filteredRestaurantByFoodType.add(restaurant);
                }
            }
        }

        return filteredRestaurantByFoodType;
    }

    private List<Restaurant> getRestaurantByCapacity(String all) {
        if (all == null) {
            return restaurants;
        }
        return restaurants.stream().filter(Restaurant::isChargeable).toList();
    }

    /**
     * @param name         represents the restaurants name
     * @param foodType     represents the food type the user wants to eat
     * @param availability represents the availability asked by the user in relation
     *                     to what he wants to eat
     * @return restaurants that match the filters
     */
    public List<Restaurant> getFilteredRestaurants(String name, FoodType foodType, String availability) {
        List<Restaurant> filteredRestaurantByName = getRestaurantsByName(name);
        List<Restaurant> filteredRestaurantByFoodType = getRestaurantsByFoodType(foodType);
        List<Restaurant> filteredRestaurantByCapacity = getRestaurantByCapacity(availability);

        List<Restaurant> filteredRestaurant = new ArrayList<>(filteredRestaurantByName);
        filteredRestaurant.retainAll(filteredRestaurantByFoodType);
        filteredRestaurant.retainAll(filteredRestaurantByCapacity);

        return filteredRestaurant.stream().distinct().toList();
    }
}
