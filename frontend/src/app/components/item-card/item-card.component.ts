import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Menu } from '../../../models/menu.model';
import { MENU_NULL } from '../../../mocks/menu.mock';
import { ItemBigCardComponent } from '../item-big-card/item-big-card.component';  // Make sure the path is correct
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-item-card',
  standalone: true,
  imports: [CommonModule, ItemBigCardComponent],
  templateUrl: './item-card.component.html',
  styleUrls: ['./item-card.component.scss']
})
export class ItemCardComponent {

  public showCard: boolean = false;

  @Input()
  public menu: Menu = MENU_NULL;

  @Input()
  public isClickAllowed: boolean = true;

  @Output()
  addMenu = new EventEmitter<string>();

  constructor() {}

  showBigCard() {
    if (this.isClickAllowed) {
      this.showCard = !this.showCard;
    }
  }

  closeBigCard() {
    if (this.isClickAllowed) {
      this.showBigCard();
    }
  }

  handleAddMenu(menuName: string) {
    this.addMenu.emit(menuName);
  }
}