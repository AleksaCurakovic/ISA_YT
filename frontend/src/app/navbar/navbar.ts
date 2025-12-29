import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { Auth } from '../infrastructure/service/auth';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar implements OnInit {
  public loggedInUsername: string | null = null;
  constructor(private authService: Auth, private router: Router) {}

  ngOnInit(): void {
    this.authService.authState$.subscribe(username  => {
      this.loggedInUsername = username;
    })
  }

  logout(): void {
    this.authService.logout();
  }

  login(): void {
    console.log("Navigating to login");
    this.router.navigate(['/login']);
  }

  register(): void {
    this.router.navigate(['/register']);
  }
}
