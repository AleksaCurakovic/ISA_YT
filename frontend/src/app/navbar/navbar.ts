import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterModule } from '@angular/router';
import { Auth } from '../infrastructure/service/auth';
import { WatchpartyService } from './service/watchparty-service';
import { WatchParty } from '../model/watchParty';
import { AsyncPipe } from '@angular/common';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, RouterModule, AsyncPipe],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar implements OnInit {
  public loggedInUsername: string | null = null;
  public activePartySession: WatchParty | null = null;
  constructor(private authService: Auth, private router: Router, public watchPartyServis: WatchpartyService) {}

  ngOnInit(): void {
    this.authService.authState$.subscribe(username  => {
      this.loggedInUsername = username;
    })
    this.watchPartyServis.activePartySession$.subscribe(party => {
      this.activePartySession = party;
    })
  }

  logout(): void {
    if (this.activePartySession)
      this.watchPartyServis.leaveWatchParty(this.activePartySession);
    this.authService.logout();
  }

  login(): void {
    console.log("Navigating to login");
    this.router.navigate(['/login']);
  }

  register(): void {
    this.router.navigate(['/register']);
  }

  createWatchParty(): void {
    this.watchPartyServis.createWatchParty(this.loggedInUsername!).subscribe();
  }

}
