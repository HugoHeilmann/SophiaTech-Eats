import { CommonModule } from '@angular/common';
import { Component, HostListener, ElementRef } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent {
  public showDetails: boolean = false;
  public isLogged: boolean = false;

  public logginMenu = [
    { name: 'Se connecter', action: () => this.login() },
    { name: 'S\'enregistrer', action: () => this.register() }
  ];

  public logoutMenu = [
    { name: 'Se deconnecter', action: () => this.logout() }
  ];

  constructor(private authService: AuthService, private router: Router, private elementRef: ElementRef) {
    this.authService.isLoggedIn().subscribe((status) => {
      this.isLogged = status;
    });
  }

  show() {
    this.showDetails = !this.showDetails;
  }



  public login() {
    this.router.navigate(['/login']);
  }

  public register() {
    this.router.navigate(['/register']);
  }

  public logout() {
    this.authService.logout();
  }

  @HostListener('document:click', ['$event'])
  onClickOutside(event: MouseEvent) {
    if (!this.elementRef.nativeElement.contains(event.target)) {
      this.showDetails = false;
    }
  }
}
