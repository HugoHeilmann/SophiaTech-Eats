import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RestaurantCardComponent } from '../../components/restaurant-card/restaurant-card.component';
import { CommonService } from '../../../services/common.service';
import { Restaurant } from '../../../models/restaurant.model';
import { SidebarComponent } from "../../components/sidebar/sidebar.component";
import { Router } from '@angular/router';
import { FoodType, stringToFoodType } from '../../../models/foodtype.model';
import { SearchBarComponent } from '../../components/search-bar/search-bar.component';
import { FilterComponent } from '../../components/filter/filter.component';
import { AvailabilityComponent } from '../../components/availability/availability.component';

@Component({
	selector: 'app-home',
	standalone: true,
	imports: [CommonModule, RestaurantCardComponent, SidebarComponent, SearchBarComponent, FilterComponent, AvailabilityComponent],
	templateUrl: './home.component.html',
	styleUrl: './home.component.scss'
})
export class HomeComponent {
	restaurants: Restaurant[] = [];

	private name: string = "";
	private foodType: FoodType = FoodType.NONE;
	private availability: string = "";

	constructor(public router: Router, public commonService: CommonService) {
		this.commonService.restaurants$.subscribe((restaurants) => {
			this.restaurants = restaurants;
		})
	}


	ngOnInit() {
		this.name = "";
		this.foodType = FoodType.NONE;
		this.availability = "";
		this.commonService.browseRestaurants();
	}


	getRestaurantByName(query: string) {
		this.name = query;
		this.commonService.browseRestaurants(this.name, this.foodType);
	}


	getRestaurantByFilter(filter: string) {
		this.foodType = stringToFoodType(filter);
		this.commonService.browseRestaurants(this.name, this.foodType);
	}


	getRestaurantByAvailability(availability: string) {
		this.availability = availability;
		this.commonService.browseRestaurants(this.name, this.foodType, this.availability);
	}

	
	getRestaurantMenus(restaurant: Restaurant) {
		this.commonService.browseMenus(restaurant);
		this.router.navigate(['home/restaurants/' + restaurant.name]);
	}
}