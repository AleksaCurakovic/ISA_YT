import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Auth } from '../service/auth';
import { ReactiveFormsModule, FormBuilder, Validators, 
        AbstractControl, ValidationErrors, ValidatorFn, FormGroup } from '@angular/forms';
import { RegisterRequest } from '../../model/registerRequest';


export const passwordMatchValidator: ValidatorFn =
  (control: AbstractControl): ValidationErrors | null => {

    const password = control.get('password')?.value;
    const confirmPassword = control.get('confirmPassword')?.value;

    if (!password || !confirmPassword) return null;

    return password === confirmPassword
      ? null
      : { passwordsMismatch: true };
  };

@Component({
  selector: 'app-register',
  imports: [RouterLink, ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  registerForm: FormGroup;
  constructor(private authService: Auth, private formBuilder: FormBuilder) {
    this.registerForm = this.formBuilder.group(
      {
        username: ['', [Validators.required, Validators.minLength(3)]],
        email: ['', [Validators.required, Validators.email]],
        password: ['', [Validators.required, Validators.minLength(6)]],
        confirmPassword: ['', Validators.required],
        firstName: ['', Validators.required],
        lastName: ['', Validators.required],
        address: ['', Validators.required]
      },
      { validators: passwordMatchValidator } 
    );
  }



  register(): void {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }
    const requestPayload : RegisterRequest = {
      username: this.registerForm.value.username,
      password: this.registerForm.value.password,
      email: this.registerForm.value.email,
      firstname: this.registerForm.value.firstName,
      lastname: this.registerForm.value.lastName,
      address: this.registerForm.value.address
    };
    this.authService.register(requestPayload);
  }

}
