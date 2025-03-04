import { Order } from '../models/order.model';

export const ORDER_NULL: Order = {
  delivery: "",
  orderDate: "",
  location: {
    latitude: -1,
    longitude: -1,
    address: ""
  },
  price: -1,
  menus: [],
  restaurantId: -1,
  customerId: -1,
  preparationTime: -1
}
