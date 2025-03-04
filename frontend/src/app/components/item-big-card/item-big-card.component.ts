import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Menu } from '../../../models/menu.model';

@Component({
  selector: 'app-item-big-card',
  standalone: true,
  imports: [],
  templateUrl: './item-big-card.component.html',
  styleUrl: './item-big-card.component.scss'
})
export class ItemBigCardComponent {
  
  @Input() menu!: Menu;
  @Output() addMenu = new EventEmitter<string>();

  @Output()
  closeCard: EventEmitter<void> = new EventEmitter();

  constructor() {}

  addToBasket(menuName: string) {
    this.addMenu.emit(menuName);
  }
  close() {
    this.closeCard.emit();
  }
}