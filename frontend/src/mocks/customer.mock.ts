import {Customer} from '../models/customer.model';
import {CustomerStatus} from '../models/customerStatus.model';

export const CUSTOMER_NULL: Customer = {
  id: -1,
  name: "nomnom",
  balance: -1,
  status: CustomerStatus.REGULAR,
  history: [],
  locations: []
}
