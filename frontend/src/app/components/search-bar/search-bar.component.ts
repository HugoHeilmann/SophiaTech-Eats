import { Component, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { EventEmitter } from '@angular/core';

@Component({
  selector: 'app-search-bar',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './search-bar.component.html',
  styleUrl: './search-bar.component.scss'
})
export class SearchBarComponent {

  public searchQuery: string = "";

  @Output()
  public query: EventEmitter<string> = new EventEmitter();

  queryChange(): void {
    this.query.emit(this.searchQuery);
  }

  beginSearch(event: KeyboardEvent) {
    if (event.key === 'Enter') {
      this.queryChange();
    }
  }
}