import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LoginRequest } from '../../model/loginRequest';
import { RegisterRequest } from '../../model/registerRequest';
import { TokenResponse } from '../../model/tokenResponse';
import { jwtDecode } from 'jwt-decode';
import { BehaviorSubject } from 'rxjs';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';


@Injectable({
  providedIn: 'root',
})
export class Auth {
  private readonly API_URL = 'http://localhost:8080';
  private authStateSubject = new BehaviorSubject<string | null>(this.decodeToken());
  public authState$ = this.authStateSubject.asObservable();
  constructor(private http: HttpClient, private router: Router) { }

  login(payload: LoginRequest): void {
    this.http
      .post<TokenResponse>(`${this.API_URL}/login`, payload)
      .subscribe({
        next: (res) => {
          this.storeSession(res);
          this.authStateSubject.next(this.decodeToken());
          this.router.navigate(['/']);
          console.log('Login successful');
        },
        error: (err) => {
          console.error('Login failed', err);
        }
      });
  }
  
   register(payload: RegisterRequest): void {
    this.http
      .post<string>(`${this.API_URL}/signup`, payload)
      .subscribe({
        next: (res) => {
          this.router.navigate(['/login']);
          console.log(res);
        },
        error: (err) => {
          console.error('Registration failed', err);
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
  }
}
