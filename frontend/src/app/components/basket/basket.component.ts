import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Menu } from '../../../models/menu.model';
import { CommonService } from '../../../services/common.service';
import { ItemCardComponent } from "../item-card/item-card.component";
import { OrderService } from '../../../services/order.service';
import { Location } from '../../../models/location.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-basket',
  standalone: true,
  imports: [CommonModule, ItemCardComponent, FormsModule],
  templateUrl: './basket.component.html',
  styleUrl: './basket.component.scss'
})
export class BasketComponent {
  public showDetails: boolean = false;
  public state: string = "create-order";
  public RestorantId: number = 0;
  public hour: string = "";
  public location: Location = { latitude: 0, longitude: 0, address: "" };
  public suggestions: any[] = [];
  public basket_content: Menu[] = [];
  public isConnected: boolean = false;
  public customerId: number = 0;
  public groupId: number = 0;
  public showJoinGroupModal: boolean = false;

  public restaurantAvailability: string[] = this.generateHalfHourIntervals();

  constructor(public commonService: CommonService, public orderService: OrderService, private http: HttpClient, private authService: AuthService) {
    this.commonService.basket$.subscribe((basket) => {
      this.basket_content = basket;
    })
    this.commonService.actualRestaurant$.subscribe((restaurant) => {
        this.RestorantId = restaurant.id;
    })
    this.orderService.delivery$.subscribe((delivery) => {
        this.hour = delivery;
    })
    this.orderService.location$.subscribe((location) => {
        this.location = location;
    })
    this.authService.isLoggedIn().subscribe((status) => {
      this.isConnected = status;
    });

    this.authService.getCustomerId().subscribe((customerId) => {
      this.customerId = customerId;
    });
  }

  generateHalfHourIntervals(): string[] {
    const intervals: string[] = [];
    for (let hour = 0; hour < 24; hour++) {
      for (let minute = 0; minute < 60; minute += 30) {
        const formattedHour = hour.toString().padStart(2, '0');
        const formattedMinute = minute.toString().padStart(2, '0');
        intervals.push(`${formattedHour}h${formattedMinute}`);
      }
    }
    return intervals;
  }

  show() {
    this.showDetails = !this.showDetails;
  }

  formatHeure(hour: string): string {
    return hour.replace('h', ':');
  }

  setStepValidate() {
    const formattedHeure = this.formatHeure(this.hour);
    this.orderService.changeOrderBuilderDelivery(formattedHeure);
    this.orderService.changeOrderBuilderLocation(0);
    this.state = "validate";
  }

  setStepFinishOrder() {
    if (this.state !== "validate") {
      alert("Vous n'avez pas de commande en cours");
      return;
    }
    this.showDetails = !this.showDetails;
    this.state = "create-order";
    this.hour = "";
    this.location.address = "";
    this.orderService.closeOrderBuilder();
    this.commonService.clearBasket();
    alert("Commande validée");
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

  onTimeChange(event: any) {
    this.hour = event.target.value;
  }

  selectSuggestion(suggestion: any) {
    this.location.address = suggestion.label;
    this.location.latitude = suggestion.coordinates[1];
    this.location.longitude = suggestion.coordinates[0];
    this.suggestions = [];
    const formattedAddress = this.location.address.replace(/ /g, '_');
    this.orderService.changeCustomerLocation(formattedAddress);
  }

  handleAddMenu(menuName: string) {
    if (this.state !== "validate") {
      alert("Vous n'avez pas de commande en cours");
      return;
    }
    this.orderService.addMenuToOrderBuilder(menuName);
    const menu = this.commonService.getMenuByName(menuName);
    if (menu) {
      this.basket_content.push(menu);
    }
  }

  createOrder() {
    this.orderService.createOrderBuilder(this.RestorantId);
    this.state = "init-order";
  }

  joinGroupOrder() {
    this.orderService.addCustomerToGroup(this.groupId);
    this.closeJoinGroupModal();
    alert("Vous avez rejoint le groupe : " + this.groupId);
    this.hour = "";
    this.location.address = "";
  }

  openJoinGroupModal() {
    this.showJoinGroupModal = true;
  }

  closeJoinGroupModal() {
    this.showJoinGroupModal = false;
  }

  getTotalPrice(): number {
    return this.basket_content.reduce((total, menu) => total + menu.price, 0);
  }
}