import {FoodType} from "./foodtype.model";

export enum CustomerStatus {
  REGULAR,
  STUDENT,
  VIP
}

export function stringToCustomerStatus(str: string) : CustomerStatus {
  switch (str) {
    case "Student":
      return CustomerStatus.STUDENT;
    case "VIP":
      return CustomerStatus.VIP;
    case "Regular":
      return CustomerStatus.REGULAR;
    default:
      return CustomerStatus.REGULAR;
  }
}
