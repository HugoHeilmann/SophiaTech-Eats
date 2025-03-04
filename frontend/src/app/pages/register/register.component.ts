import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { AuthService } from '../../../services/auth.service';
import { Router } from '@angular/router';
import { ToasterComponent } from "../../components/toaster/toaster.component";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, MatInputModule, ToasterComponent],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  public loginForm: FormGroup;
  public name: FormControl;
  public email: FormControl;
  public password: FormControl;
  public isError: boolean = false;
  public displayToaster: boolean = false;
  public toasterMessage: string = 'Une erreur est survenue';

  constructor(
    private authService: AuthService, private router: Router
  ) {
    this.name = new FormControl('', [Validators.required]);
    this.email = new FormControl('', [Validators.required, Validators.email]);
    this.password = new FormControl('', [Validators.required, Validators.minLength(6)]);

    this.loginForm = new FormGroup({
      name: this.name,
      email: this.email,
      password: this.password
    });
  }

  public close(): void {
    this.router.navigate(['/login']);
  }

  public onSubmit(): void {
    this.displayToaster = false;
    if (this.loginForm.valid) {
      const { name, email, password } = this.loginForm.value;
      this.authService.register(name, email, password).subscribe(
        (response) => {
          console.log('Register success', response);
          this.toasterMessage = 'Inscription réussie';
          this.isError = false;
          this.displayToaster = true;

          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 2000);
        },
        (error) => {
          console.error('Register error', error);
          this.toasterMessage = 'Une erreur est survenue';
          this.isError = true;
          this.displayToaster = true;
        }
      );
    }
  }
}
