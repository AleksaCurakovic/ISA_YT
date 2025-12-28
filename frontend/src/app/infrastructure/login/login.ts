import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Auth } from '../auth';
import { ReactiveFormsModule, FormBuilder, Validators, FormGroup } from '@angular/forms';
import { LoginRequest } from '../../model/loginRequest';

@Component({
  selector: 'app-login',
  imports: [RouterLink, ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login  {
  loginForm: FormGroup;
  constructor(private authService: Auth, private formBuilder: FormBuilder) {
    this.loginForm = this.formBuilder.group(
      {
        username: ['', [Validators.required]],
        password: ['', [Validators.required]]
      }
    );
  }

  login(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }
    const loginPayload: LoginRequest = {
      username: this.loginForm.value.username,
      password: this.loginForm.value.password
    };
    this.authService.login(loginPayload);
  }
}