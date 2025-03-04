import {Location} from "./location.model";

export interface Group {
  id: number,
  delivery: string,
  location: Location,
  memberIds: number[]
}
