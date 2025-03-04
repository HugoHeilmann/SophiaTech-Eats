import {CustomerStatus} from "./customerStatus.model";
import {Order} from "./order.model";
import {Location} from "./location.model";


export interface Customer {
  id: number;
  name: string;
  balance: number;
  status : CustomerStatus;
  history: Order[];
  locations : Location[];
}


