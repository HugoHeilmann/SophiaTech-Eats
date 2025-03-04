import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-availability',
  standalone: true,
  imports: [],
  templateUrl: './availability.component.html',
  styleUrl: './availability.component.scss'
})
export class AvailabilityComponent {
  public isChecked: boolean = false;

  @Output()
  availability = new EventEmitter<string>();

  constructor() {}

  changeChecking() {
    this.isChecked = !this.isChecked;
    const availability = this.isChecked ? "check" : "";
    this.availability.emit(availability);
  }
}
