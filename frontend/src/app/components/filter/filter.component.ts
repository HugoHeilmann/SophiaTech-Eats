import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-filter',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './filter.component.html',
  styleUrls: ['./filter.component.scss']
})
export class FilterComponent {

  public filterType: string = "Type de nourriture";
  public filters: string[] = ["Tout", "Veggie", "Fast Food", "Burger", "Asiat", "Pizza", "Halal", "Sushi", "Pate"];
  
  public selectedItem: string = "Tout";
  
  @Output()
  filter = new EventEmitter<string>();

  filterChange(): void {
    this.filter.emit(this.selectedItem);
  }

  select(item: string) {
    this.selectedItem = item;
    this.filterChange();
  }
}
