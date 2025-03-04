package backend;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Menu {

    /* Attributes */

    private FoodType foodType;
    private String name;
    private String description;
    private double price;
    private int preparation;
    private String image;


    /* Construction */
    @JsonCreator
    public Menu(
            @JsonProperty("foodType") FoodType foodType,
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("price") double price,
            @JsonProperty("preparation") int preparation,
            @JsonProperty("image") String image) {
        this.foodType = foodType;
        this.name = name;
        this.description = description;
        this.price = price;
        this.preparation = preparation;
        this.image = image;
    }

    /* Methods */

    public FoodType getFoodType() {
        return this.foodType;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public double getPrice() {
        return this.price;
    }

    public int getPreparation() {
        return this.preparation;
    }

    public String getImage() {
        return this.image;
    }

    /* Data : used by the Facade design pattern */

    public record Data(FoodType foodType, String name, String description, double price, int preparation,
                       String image) {

        public Menu toMenu() {
            return new Menu(this.foodType, this.name, this.description, this.price, this.preparation, this.image);
        }

    }


    public Data toData() {
        return new Data(this.foodType, this.name, this.description, this.price, this.preparation, this.image);
    }

}