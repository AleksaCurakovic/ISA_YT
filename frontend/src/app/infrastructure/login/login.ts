import { Component, OnInit } from '@angular/core';
import { RouterLink, ActivatedRoute, Router } from '@angular/router';
import { Auth } from '../service/auth';
import { ReactiveFormsModule, FormBuilder, Validators, FormGroup } from '@angular/forms';
import { LoginRequest } from '../../model/loginRequest';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-login',
  imports: [RouterLink, ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login  implements OnInit {
  loginForm: FormGroup;
  constructor(private authService: Auth, private formBuilder: FormBuilder,
    private route: ActivatedRoute, private router: Router, private toastr: ToastrService
  ) {
    this.loginForm = this.formBuilder.group(
      {
        username: ['', [Validators.required]],
        password: ['', [Validators.required]]
      }
    );
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
    if (params['verified'] === 'true') {
      this.toastr.success('Account verified successfully!');
    }
    if (params['alreadyVerified'] === 'true') {
      this.toastr.warning('Account arleady verified!');
    }
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {},
      replaceUrl: true, 
    });
  });
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