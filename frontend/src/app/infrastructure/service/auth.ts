import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LoginRequest } from '../../model/loginRequest';
import { RegisterRequest } from '../../model/registerRequest';
import { TokenResponse } from '../../model/tokenResponse';
import { jwtDecode } from 'jwt-decode';
import { BehaviorSubject } from 'rxjs';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Profile } from '../../model/profile'

@Injectable({
  providedIn: 'root',
})
export class Auth {
  private readonly API_URL = 'http://localhost:8080';
  private authStateSubject = new BehaviorSubject<string | null>(this.decodeToken());
  public authState$ = this.authStateSubject.asObservable();
  constructor(private http: HttpClient, private router: Router, private toastr: ToastrService) { }

  login(payload: LoginRequest): void {
    this.http
      .post<TokenResponse>(`${this.API_URL}/login`, payload)
      .subscribe({
        next: (res) => {
          this.storeSession(res);
          this.authStateSubject.next(this.decodeToken());
          this.toastr.success('Login Successful!')
          this.router.navigate(['/']);
          console.log('Login successful');
        },
        error: (err) => {
          console.error('Login failed', err);
          this.toastr.error('Incorrect or unverified account details')
        }
      });
  }
  
   register(payload: RegisterRequest): void {
    this.http
      .post<string>(`${this.API_URL}/signup`, payload)
      .subscribe({
        next: (res) => {
          this.toastr.info('Verification email sent')
          this.router.navigate(['/login']);
          console.log(res);
        },
        error: (err) => {
          console.error('Registration failed', err);
          this.toastr.error('Username or email already in use')
        }
      });
  }

  private storeSession(res: TokenResponse): void {
    localStorage.setItem('jwt', res.accessToken);
  }

  getToken(): string | null {
    return localStorage.getItem('jwt');
  }

  decodeToken(): string | null {
    const token = this.getToken();
    if (!token) return null;
    return jwtDecode(token).sub ?? null;
  }

  logout(): void {
    localStorage.removeItem('jwt');
    this.authStateSubject.next(null);
    this.toastr.success('Successfully logged out')
    this.router.navigate(['/home'])
  }

  whoAmI(): Observable<Profile> {
    return this.http.get<Profile>(`${this.API_URL}/whoAmI`)
  }

  whoAreYou(userName: string): Observable<Profile> {
    return this.http.get<Profile>(`${this.API_URL}/whoAreYou/${userName}`)
  }
}
