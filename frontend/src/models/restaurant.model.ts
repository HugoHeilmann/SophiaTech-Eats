import { FoodType } from "./foodtype.model";
import { Menu } from "./menu.model";

export interface Restaurant {
    id: number,
    name: string,
    address: string,
    phone: string,
    email: string,
    menus: Menu[],
    foodTypes: FoodType[],
    description: string
    image: string,
}