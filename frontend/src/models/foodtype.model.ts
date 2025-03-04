export enum FoodType {
    NONE,
    VEGGIE,
    FAST_FOOD,
    BURGER,
    ASIAT,
    PIZZA,
    HALAL,
    SUSHI,
    PATE
}

export function stringToFoodType(str: string) {
    switch (str) {
        case "Veggie":
            return FoodType.VEGGIE;
        case "Fast Food":
            return FoodType.FAST_FOOD;
        case "Burger":
            return FoodType.BURGER;
        case "Asiat":
            return FoodType.ASIAT;
        case "Pizza":
            return FoodType.PIZZA;
        case "Halal":
            return FoodType.HALAL;
        case "Sushi":
            return FoodType.SUSHI;
        case "Pate":
            return FoodType.PATE;
        default:
            return FoodType.NONE;
    }
}

export function foodTypeToString(foodType: FoodType) {
    switch (foodType) {
        case FoodType.NONE:
            return "Indéfini";
        case FoodType.VEGGIE:
            return "Veggie";
        case FoodType.FAST_FOOD:
            return "Fast Food";
        case FoodType.BURGER:
            return "Burger";
        case FoodType.ASIAT:
            return "Asiat";
        case FoodType.PIZZA:
            return "Pizza";
        case FoodType.HALAL:
            return "Halal";
        case FoodType.SUSHI:
            return "Sushi";
        case FoodType.PATE:
            return "Pate";
    }
}