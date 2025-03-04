import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';
import { Group } from '../models/group.model';
import { Customer } from '../models/customer.model';
import { Order } from '../models/order.model';
import { Location } from '../models/location.model';
import {Menu} from "../models/menu.model";
import {MENU_NULL} from "../mocks/menu.mock";
import {Restaurant} from "../models/restaurant.model";
import {RESTAURANT_NULL} from "../mocks/restaurant.mock";
import {LOCATION_NULL} from "../mocks/location.model";
import { GROUP_NULL } from '../mocks/group.mock';
import { environment } from '../environment/environment';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private groupUrl: string = `${environment.apiUrl}/order/groups?`;
  private customerUrl: string = `${environment.apiUrl}/order/customer/location?`;
  private orderUrl: string = `${environment.apiUrl}/order/order?`;
  private orderCustomerLocationUrl: string = `${environment.apiUrl}/order/order/customer/location?`;
  private orderCustomerDeliveryUrl: string = `${environment.apiUrl}/order/order/customer/delivery?`;
  private orderCustomerMenuUrl: string = `${environment.apiUrl}/order/order/customer/menu?`;
  private orderCustomerHistoryUrl: string = `${ environment.apiUrl }/order/order/customer/history?`;
  private customerId: number = 0;
  private group: Group = GROUP_NULL;
  public group$: BehaviorSubject<Group> = new BehaviorSubject(this.group);

  private groups: Group[] = [];
  public groups$: BehaviorSubject<Group[]> = new BehaviorSubject(this.groups);

  private customer: Customer[] = [];
  public customer$: BehaviorSubject<Customer[]> = new BehaviorSubject(this.customer);

  private order: Order[] = [];
  public order$: BehaviorSubject<Order[]> = new BehaviorSubject(this.order);

  private delivery: string = "";
  public delivery$: BehaviorSubject<string> = new BehaviorSubject(this.delivery);

  private location: Location = LOCATION_NULL;
  public location$: BehaviorSubject<Location> = new BehaviorSubject(this.location);

  constructor(private http: HttpClient, private authService: AuthService) {
    this.authService.getCustomerId().subscribe((customerId) => {
      this.customerId = customerId;
    });
  }

  public getGroup(groupId: number): void {
    let url = `${this.groupUrl}groupId=${groupId}`;
    this.http.get<Group>(url).subscribe((group: Group) => {
      this.group = group;
      this.group$.next(this.group);
    });
  }

  public askToCreateGroup(delivery: string, location: string): void {
    let url = `${this.groupUrl}delivery=${delivery}&location=${location}&customerId=${this.customerId}`;
    this.http.post<Group>(url, {}).subscribe((group: Group) => {
      this.groups.push(group);
      this.groups$.next(this.groups);
      this.group = group;
      this.group$.next(this.group);
    });
  }

  public addCustomerToGroup(groupId: number): void {
    let url = `${this.groupUrl}groupId=${groupId}&customerId=${this.customerId}`;
    this.http.put<Group>(url, {}).subscribe((group: Group) => {
      this.groups.push(group);
      this.groups$.next(this.groups);
      this.group = group;
      this.group$.next(this.group);
    });
  }

  public askToDeleteGroup(groupId: number, delivery: string): void {
    let url = `${this.groupUrl}groupId=${groupId}&customerId=${this.customerId}&delivery=${delivery}`;
    this.http.delete<Group>(url).subscribe((group: Group) => {
      this.groups.push(group);
      this.groups$.next(this.groups);
      this.group = group;
      this.group$.next(this.group);
    });
  }

  public changeCustomerLocation(location: string): void {
    let decodedLocation = decodeURIComponent(location);
    console.log("ici la location decodee");
    console.log(decodedLocation);
    let url = `${this.customerUrl}location=${decodedLocation}&customerId=${this.customerId}`;
    this.http.put<Order>(url, {}).subscribe((order: Order) => {
      this.order.push(order);
      this.order$.next(this.order);
    });
  }

  public deleteCustomerLocation(locationIndex: number): void {
    let url = `${this.customerUrl}locationIndex=${locationIndex}&customerId=${this.customerId}`;
    this.http.delete<Customer>(url).subscribe((customer: Customer) => {
      this.customer.push(customer);
      this.customer$.next(this.customer);
    });
  }

  public createOrderBuilder(restaurantId: number): void {
    let url = `${this.orderUrl}restaurantId=${restaurantId}&customerId=${this.customerId}`;
    this.http.post<Order>(url, {}).subscribe((order: Order) => {
      this.order.push(order);
      this.order$.next(this.order);
    });
  }

  public changeOrderBuilderLocation(locationIndex: number): void {
    let url = `${this.orderCustomerLocationUrl}locationIndex=${locationIndex}&customerId=${this.customerId}`;
    this.http.put<Order>(url, {}).subscribe((order: Order) => {
      this.order.push(order);
      this.order$.next(this.order);
    });
  }

  public changeOrderBuilderDelivery(delivery: string): void {
    let url = `${this.orderCustomerDeliveryUrl}delivery=${delivery}&customerId=${this.customerId}`;
    this.http.put<Order>(url, {}).subscribe((order: Order) => {
      this.order.push(order);
      this.order$.next(this.order);
    });
  }

  public addMenuToOrderBuilder(menuName: string): void {
    const formattedMenuName = menuName.replace(/ /g, '_');
    const url = `${this.orderCustomerMenuUrl}menuName=${formattedMenuName}&customerId=${this.customerId}`;
    this.http.put<Order>(url, {}).subscribe((order: Order) => {
      console.log('Menu added to order:', order);
    });
  }

  public closeOrderBuilder(): void {
    let url = `${this.orderCustomerHistoryUrl}customerId=${this.customerId}`;
    this.http.put<Order>(url, {}).subscribe((order: Order) => {
      this.order.push(order);
      this.order$.next(this.order);
    });
  }
}
