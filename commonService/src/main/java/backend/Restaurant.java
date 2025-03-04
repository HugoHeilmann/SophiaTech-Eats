package backend;

import backend.customer.Customer;
import backend.database.CustomerDatabase;
import backend.exceptions.CustomerNotFoundException;
import backend.strategy.OfferStrategy;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Restaurant {

    private static int ID = 0;

    public static void resetId() {
        ID = 0;
    }

    /* Attributes */

    private final int id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private HashMap<Integer, LocalTime[]> schedule;
    private Capacity capacity;
    private int orderChargeable;
    private List<Menu> menus;
    private List<FoodType> foodTypes = new ArrayList<>();
    private OfferStrategy strategy;
    private String description;
    private String image;


    /* Construction */

    public Restaurant(String name, String address, String phone, String email, List<Menu> menus, Capacity capacity, OfferStrategy strategy, String description, String image) {
        this.id = ID++;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;

        this.schedule = new HashMap<>();
        LocalTime openingTime = LocalTime.of(9, 0);
        LocalTime closingTime = LocalTime.of(22, 0);

        for (int day = 0; day < 7; day++) {
            this.schedule.put(day, new LocalTime[]{openingTime, closingTime});
        }

        this.capacity = capacity;
        this.capacity.setClosedMoments(this.schedule);

        // Number of order chargeable for a restaurant. Define by the capacity and the mean time for an order
        int meanTimeForOrder = getMeanTimeForOrder();
        if (meanTimeForOrder != 0) {
            this.orderChargeable = (this.capacity.getCapacityForNow() / meanTimeForOrder);
        } else {
            this.orderChargeable = 0;
        }
        this.menus = new ArrayList<>(); // According to the class diagram, a restaurant must create its menus

        for (Menu menu : menus) {
            if (!foodTypes.contains(menu.getFoodType())) {
                this.foodTypes.add(menu.getFoodType());
            }
        }
        this.menus = menus;
        this.strategy = strategy;
        this.description = description;
        this.image = image;
    }

    @JsonCreator
    public Restaurant(@JsonProperty("id") int id,
                      @JsonProperty("name") String name,
                      @JsonProperty("address") String address,
                      @JsonProperty("phone") String phone,
                      @JsonProperty("email") String email,
                      @JsonProperty("schedule") HashMap<Integer, LocalTime[]> schedule,
                      @JsonProperty("capacity") Capacity capacity,
                      @JsonProperty("orderChargeable") int orderChargeable,
                      @JsonProperty("menus") List<Menu> menus,
                      @JsonProperty("foodTypes") List<FoodType> foodTypes,
                      @JsonProperty("strategy") OfferStrategy strategy,
                      @JsonProperty("description") String description,
                      @JsonProperty("image") String image) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.capacity = capacity;
        this.schedule = schedule;
        this.orderChargeable = orderChargeable;
        this.menus = menus;
        this.foodTypes = foodTypes;
        this.strategy = strategy;
        this.description = description;
        this.image = image;
    }


    /* Data : used by the Facade design pattern */

    public record Data(int id, String name, String address, String phone, String email,
                       Menu.Data[] menus,
                       FoodType[] foodTypes, String description, String image) {
    }

    public Data toData() {
        Menu.Data[] menusData = new Menu.Data[this.menus.size()];
        for (int i = 0; i < menusData.length; i++) {
            menusData[i] = this.menus.get(i).toData();
        }

        FoodType[] foodTypesArray = new FoodType[this.foodTypes.size()];
        for (int i = 0; i < foodTypesArray.length; i++) {
            foodTypesArray[i] = this.foodTypes.get(i);
        }

        return new Data(this.id, this.name, this.address, this.phone, this.email, menusData, foodTypesArray, this.description, this.image);
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public HashMap<Integer, LocalTime[]> getSchedule() {
        return schedule;
    }

    public Capacity getCapacity() {
        return capacity;
    }

    public List<Menu> getMenus() {
        return this.menus;
    }

    public List<FoodType> getFoodTypes() {
        return foodTypes;
    }

    public OfferStrategy getStrategy() {
        return strategy;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    @JsonIgnore
    public int getMeanTimeForOrder() {
        if (menus != null) {
            int sumTimeMenus = 0;
            for (Menu menu : menus) {
                sumTimeMenus += menu.getPreparation();
            }
            int meanTimeMenu = sumTimeMenus / menus.size();
            return (meanTimeMenu / 3);
        }
        return 0;
    }


    /**
     * Specify if the restaurant can accept a new order
     *
     * @return a boolean saying if the restaurant can accept a new order
     */
    public boolean isChargeable() {
        return orderChargeable > 0;
    }


    public List<Menu> getMenuByAvailability(int customerId) throws CustomerNotFoundException {
        Customer customer = CustomerDatabase.getInstance().getCustomerById(customerId);
        if (!(customer.getBasket() != null && customer.getBasket().getRestaurantId() == this.getId()
                && customer.getBasket().getDelivery() != null)) {
            return this.getMenus();
        }

        LocalTime delivery = customer.getBasket().getDelivery();
        List<Menu> availableMenus = new ArrayList<>();
        for (Menu menu : this.getMenus()) {
            if (menu.getPreparation() + customer.getBasket().getPreparationTime() + Order.AVG_DELIVERY_TIME * 60 <= delivery.toSecondOfDay() - LocalTime.now().toSecondOfDay()) {
                availableMenus.add(menu);
            }
        }
        return availableMenus;
    }
}