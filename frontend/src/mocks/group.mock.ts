import { Group } from '../models/group.model';

export const GROUP_NULL: Group = {
  id: -1,
  delivery: "",
  location: {
    latitude: 0,
    longitude: 0,
    address: ""
  },
  memberIds: []
}
