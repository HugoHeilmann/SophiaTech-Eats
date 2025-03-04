import { Injectable } from "@angular/core";
import { Restaurant } from "../models/restaurant.model";
import { RESTAURANT_NULL } from "../mocks/restaurant.mock";
import { BehaviorSubject } from "rxjs";
import { MENU_NULL } from "../mocks/menu.mock";
import { Menu } from "../models/menu.model";
import { FoodType } from "../models/foodtype.model";
import { HttpClient } from '@angular/common/http';
import { environment } from "../environment/environment";
import { AuthService } from "./auth.service";


@Injectable({
    providedIn: 'root'
})
export class CommonService {

    private actualRestaurant: Restaurant = RESTAURANT_NULL;
    public actualRestaurant$: BehaviorSubject<Restaurant> = new BehaviorSubject(this.actualRestaurant);

    private customer: number = 0;

    private restaurantsUrl: string = `${environment.apiUrl}/common/restaurants?`;
    private restaurants: Restaurant[] = [];
    public restaurants$: BehaviorSubject<Restaurant[]> = new BehaviorSubject(this.restaurants);

    private menusUrl: string = `${environment.apiUrl}/common/menus?`;
    private menus: Menu[] = [];
    public menus$: BehaviorSubject<Menu[]> = new BehaviorSubject(this.menus);

    private basket: Menu[] = [];
    public basket$: BehaviorSubject<Menu[]> = new BehaviorSubject(this.basket);

    constructor(private http: HttpClient, private authService: AuthService) {
        this.authService.getCustomerId().subscribe((customer) => {
            this.customer = customer;
        });
    }


    public browseRestaurants(name: string = "", foodType: FoodType = FoodType.NONE, availability: string = ""): void {
        let url = `${this.restaurantsUrl}${(name != "") ? `name=${name}&` : ""}${(foodType != FoodType.NONE) ? `foodType=${FoodType[foodType]}&` : ""}${(availability != "") ? `availability=${availability}` : ""}`;

        console.log(url);
        this.http.get<Restaurant[]>(url).subscribe((restaurants) => {
            this.restaurants = restaurants;
            console.log(this.restaurants);
            this.restaurants$.next(this.restaurants);
        });
    }

    public browseMenus(restaurant: Restaurant): void {
        this.actualRestaurant = restaurant;
        this.actualRestaurant$.next(this.actualRestaurant);
        let url = `${this.menusUrl}restaurantId=${restaurant.id}&`;
        if (-1 < this.customer) { url += `customerId=${this.customer}` }

        console.log(url);
        this.http.get<Menu[]>(url).subscribe((menus) => {
            this.menus = menus;
            console.log(this.menus);
            this.menus$.next(this.menus);
        });
    }

    public setMenus(menus: Menu[]): void {
        this.menus = menus;
        this.menus$.next(this.menus);
      }
    
      public getMenuByName(menuName: string): Menu | undefined {
        return this.menus.find(menu => menu.name === menuName);
    }

    public clearBasket(): void {
        this.basket = [];
        this.basket$.next(this.basket);
    }
}
