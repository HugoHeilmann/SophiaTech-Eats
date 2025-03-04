import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Location } from '../../../models/location.model';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { OrderService } from '../../../services/order.service';
import { Group } from '../../../models/group.model';
import { GROUP_NULL } from '../../../mocks/group.mock';
import { AuthService } from '../../../services/auth.service';


@Component({
	selector: 'app-group',
	standalone: true,
	imports: [CommonModule, FormsModule],
	templateUrl: './group.component.html',
	styleUrl: './group.component.scss'
})
export class GroupComponent {
	public showDetails: boolean = false;
	public created: boolean = false;
	public  isConnected: boolean = false;

	public location: Location = { latitude: 0, longitude: 0, address: "" };
	public suggestions: any[] = [];

	public hourly: string = "";
	public restaurantAvailability: string[] = this.generateHalfHourIntervals();

	public group: Group = GROUP_NULL;


	constructor(private http: HttpClient, private orderService: OrderService, private authService: AuthService) {
		this.orderService.group$.subscribe((group) => { this.group = group; });
		this.authService.isLoggedIn().subscribe((status) => { this.isConnected = status; });
	}

	ngOnInit() {
		this.showDetails = false;
		this.created = false;
		this.location = { latitude: 0, longitude: 0, address: "" };
		this.suggestions = [];
		this.hourly = "";
		this.restaurantAvailability = this.generateHalfHourIntervals();
		this.group = GROUP_NULL;
	}


	show() {
		this.showDetails = true;
	}

	close() {
		this.ngOnInit();
	}

	generateHalfHourIntervals(): string[] {
		const intervals: string[] = [];
		for (let hour = 0; hour < 24; hour++) {
		  for (let minute = 0; minute < 60; minute += 30) {
			const formattedHour = hour.toString().padStart(2, '0');
			const formattedMinute = minute.toString().padStart(2, '0');
			intervals.push(`${formattedHour}:${formattedMinute}`);
		  }
		}
		return intervals;
	  }


	onAddressInput(event: any) {
		const query = event.target.value;
		if (query.length > 2) {
			this.http.get(`https://api.openrouteservice.org/geocode/autocomplete?api_key=5b3ce3597851110001cf62481eb154289a7f4fadadda56bb8c1ad971&text=${query}&boundary.country=FR`)
				.subscribe((response: any) => {
					console.log(response);
					this.suggestions = response.features.map((feature: any) => ({
						label: feature.properties.label,
						coordinates: feature.geometry.coordinates
					}));
				});
		} else {
			this.suggestions = [];
		}
	}

	selectSuggestion(suggestion: any) {
		this.location.address = suggestion.label;
		this.location.latitude = suggestion.coordinates[1];
		this.location.longitude = suggestion.coordinates[0];
		this.suggestions = [];
		const formattedAddress = this.location.address.replace(/ /g, '_');
		// this.orderService.changeCustomerLocation(formattedAddress, 0);
	}


	onTimeChange(event: any) {
		this.hourly = event.target.value;
	}


	create() {
		this.orderService.askToCreateGroup(this.hourly, this.location.address.replace(/ /g, '_'));
		this.created = true;
		if (this.hourly != "" && this.location.address != "") {
			this.orderService.askToCreateGroup(this.hourly.replace('h', ':'), this.location.address.replace(/ /g, '_'));
			this.created = true;
		}
		else { alert("Veuillez remplir les champs"); }
	}
}
