import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { animate, style, transition, trigger } from '@angular/animations';

@Component({
  selector: 'app-toaster',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './toaster.component.html',
  styleUrl: './toaster.component.scss',
  animations: [
    trigger('toasterCollapse', [
      transition(':leave', [
        style({ transform: 'translateX(0)', opacity: 1 }),
        animate('0.5s ease-out', style({ transform: 'translateX(100%)', opacity: 0 })),
      ]),
    ]),
    trigger('toasterExpand', [
      transition(':enter', [
        style({ transform: 'translateX(100%)', opacity: 0 }),
        animate('0.5s ease-in', style({ transform: 'translateX(0)', opacity: 1 })),
      ]),
    ]),
  ],
})
export class ToasterComponent {
  @Input() public message: string = 'Une erreur est survenue';
  @Input() public isError: boolean = false;
  @Input() public isDisplayed: boolean = false;
  
  show() {
    setTimeout(() => {
      this.isDisplayed = false;
    }, 2000);
  }

  ngOnChanges() {
    if (this.isDisplayed) {
      this.show();
    }
  }
    
}
