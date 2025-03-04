// login.component.ts
import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { environment } from '../../../environment/environment';
import { AuthService } from '../../../services/auth.service';
import { Router } from '@angular/router';
import { ToasterComponent } from "../../components/toaster/toaster.component";

declare const google: any;
declare const FB: any;

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, MatInputModule, ToasterComponent],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  public loginForm: FormGroup;
  public email: FormControl;
  public password: FormControl;
  public isError: boolean = false;
  public displayToaster: boolean = false;
  public toasterMessage: string = 'Une erreur est survenue';

  constructor(
    private authService: AuthService, private router: Router
  ) {
    this.email = new FormControl('', [Validators.required, Validators.email]);
    this.password = new FormControl('', [Validators.required, Validators.minLength(6)]);

    this.loginForm = new FormGroup({
      email: this.email,
      password: this.password
    });
  }

  ngOnInit() {
    this.loadGoogleSdk();
    this.loadFacebookSdk();
  }

  public close(): void {
    this.router.navigate(['/']);
  }

  private loadGoogleSdk(): void {
    if (typeof window !== 'undefined' && typeof document !== 'undefined') {
      if (typeof google === 'undefined') {
        const script = document.createElement('script');
        script.src = 'https://accounts.google.com/gsi/client';
        script.async = true;
        script.defer = false;
        script.onload = () => {
          console.log('Google SDK loaded');
          this.initGoogleSdk();
        };
        script.onerror = () => {
          console.error('Google SDK failed to load');
        };
        document.body.appendChild(script);
      } else {
        this.initGoogleSdk();
      }
    }
  }

  private initGoogleSdk(): void {
    console.log('Google SDK loaded');
    google.accounts.id.initialize({
      client_id: environment.googleClientId,
      callback: this.handleGoogleResponse.bind(this)
    });
  }

  private handleGoogleResponse(response: any): void {
    this.displayToaster = false;
    if (response.error) {
      console.error(response.error);
    } else {
      console.log('Google Response:', response.credential);
      this.authService.loginWithGoogle(response.credential).subscribe(
        (res) => {
          console.log('Google login success', res);
          this.router.navigate(['/']);
          // Handle the response (e.g., store JWT token)
        },
        (err) => {
          console.error('Google login failed', err);
          this.toasterMessage = 'Une erreur est survenue';
          this.isError = true;
          this.displayToaster = true;
        }
      );
    }
  }

  private loadFacebookSdk(): void {
    if (typeof window !== 'undefined' && typeof FB === 'undefined') {
      const script = document.createElement('script');
      script.src = 'https://connect.facebook.net/en_US/sdk.js';
      script.async = true;
      script.defer = true;
      script.onload = () => this.initFacebookSdk();
      document.body.appendChild(script);
    }
  }

  private initFacebookSdk(): void {
    FB.init({
      appId: environment.facebookAppId,
      cookie: true,
      xfbml: true,
      version: 'v13.0'
    });
  }

  public loginWithGoogle(): void {
    google.accounts.id.prompt();
  }

  public loginWithFacebook(): void {
    this.displayToaster = false;
    FB.login((response: any) => {
      if (response.authResponse) {
        console.log('Facebook login successful:', response.authResponse);
        this.authService.loginWithFacebook(response.authResponse.accessToken).subscribe(
          (res) => {
            console.log('Facebook login success', res);
            this.router.navigate(['/']);
          },
          (err) => {
            console.error('Facebook login failed', err);
            this.toasterMessage = 'Une erreur est survenue';
            this.isError = true;
            this.displayToaster = true;
          }
        );
      } else {
        console.log('User cancelled login or did not fully authorize.');
      }
    }, { scope: 'email' });
  }

  public onSubmit(): void {
    this.displayToaster = false;
    if (this.loginForm.valid) {
      const { email, password } = this.loginForm.value;
      this.authService.loginWithCredentials(email, password).subscribe(
        (res) => {
          console.log('Login successful', res);
          // Handle successful login (e.g., store JWT token)
          this.router.navigate(['/']);
        },
        (err) => {
          console.error('Login failed', err);
          this.toasterMessage = 'Une erreur est survenue';
          this.isError = true;
          this.displayToaster = true;
        }
      );
    }
  }
}
