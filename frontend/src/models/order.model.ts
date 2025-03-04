import {Menu} from "./menu.model";
import {Location} from "./location.model";


export interface Order {
  delivery : string,
  orderDate : string,
  location : Location,
  price : number,
  menus : Menu[]
  restaurantId : number,
  customerId : number,
  preparationTime : number
}

