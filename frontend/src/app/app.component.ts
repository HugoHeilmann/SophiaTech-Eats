import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,  // Indiquer que ce composant est standalone
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  imports: [RouterModule],  // Importation explicite du composant RestaurantCard
})
export class AppComponent {
  title = 'Mon Application';

  constructor(private authService: AuthService,private router: Router) {
    
  }

  ngOnInit() {
    this.authService.validateToken().subscribe({});
  }
}
