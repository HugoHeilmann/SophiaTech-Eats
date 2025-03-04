import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Restaurant } from '../../../models/restaurant.model';
import { RESTAURANT_NULL } from '../../../mocks/restaurant.mock';

@Component({
  selector: 'app-restaurant-card',
  standalone: true,
  templateUrl: './restaurant-card.component.html',
  styleUrls: ['./restaurant-card.component.scss'],
})
export class RestaurantCardComponent {

  @Input()
  public restaurant: Restaurant = RESTAURANT_NULL;

  constructor() {}
}
