import { FoodType } from "./foodtype.model";

export interface Menu {
    foodType: FoodType,
    name: string,
    description: string,
    price: number,
    preparation: number,
    image: string
}