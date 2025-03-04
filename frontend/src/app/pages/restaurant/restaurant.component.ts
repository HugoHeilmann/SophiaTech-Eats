import { Component, ViewChild } from '@angular/core';
import { SidebarComponent } from "../../components/sidebar/sidebar.component";
import { ItemCardComponent } from '../../components/item-card/item-card.component';
import { CommonModule } from '@angular/common';
import { Menu } from '../../../models/menu.model';
import { CommonService } from '../../../services/common.service';
import { BasketComponent } from "../../components/basket/basket.component";
import { OrderService } from '../../../services/order.service';
import { Router } from '@angular/router';

@Component({
	selector: 'app-restaurant',
	standalone: true,
	imports: [CommonModule, SidebarComponent, ItemCardComponent, BasketComponent],
	templateUrl: './restaurant.component.html',
	styleUrl: './restaurant.component.scss'
})
export class RestaurantComponent {
	photo: string = "";
	menus: Menu[] = [];

	@ViewChild(BasketComponent) basketComponent!: BasketComponent;

	constructor(public commonService: CommonService, public orderService: OrderService, public router: Router) {
		this.commonService.menus$.subscribe((menus) => {
			this.menus = menus;
		});
		this.commonService.actualRestaurant$.subscribe((restaurant) => {
			this.photo = restaurant.image;
		})
	}

	handleAddMenu(menuName: string) {
		this.basketComponent.handleAddMenu(menuName);
	}

	back() {
		this.router.navigate(["/home"]);
	}
}
