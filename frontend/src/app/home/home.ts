import { Component, OnInit, signal } from '@angular/core';
import { UploadService } from '../upload/service/upload-service';
import { VideoPreview } from '../model/videoPreview';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { WatchpartyService } from '../navbar/service/watchparty-service';
import { WatchParty } from '../model/watchParty';
import { Auth } from '../infrastructure/service/auth';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-home',
  imports: [CommonModule],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {

  videoPreview = signal<VideoPreview[]> ([]);
  watchParties = signal<WatchParty[]> ([]);
  public loggedInUsername: string | null = null
  public activePartySession: WatchParty | null = null;

  constructor(private authService: Auth, private uploadService: UploadService, private router: Router,
     private watchPartyService: WatchpartyService, private toastr: ToastrService) { }

  ngOnInit(): void {
    this.authService.authState$.subscribe(username  => {
      this.loggedInUsername = username;
    })
    this.watchPartyService.activePartySession$.subscribe(party => {
      this.activePartySession = party;
    })
    this.uploadService.getAllUploads().subscribe(uploads => {
      this.videoPreview.set(uploads) 
      console.log('Fetched uploads:', this.videoPreview);
    });
    this.watchPartyService.getWatchParites().subscribe(parties => {
      this.watchParties.set(parties);
    });
  }

  joinWatchParty(party: WatchParty): void  {
    if (!this.loggedInUsername)
      {
        this.toastr.error('Please login first to join')
        this.router.navigate(['/login'])
        return
      };
    this.watchPartyService.joinWatchParty(party, this.loggedInUsername);
  }

  leaveWatchParty(party: WatchParty): void {
    this.watchPartyService.leaveWatchParty(party);
    if (this.loggedInUsername !== party.host)
      this.watchParties.set(this.watchParties().filter(p => p.id !== party.id));
  }

  playVideo(id:number): void {
    this.router.navigate(['/play'], { queryParams: { videoId: id } })
  }

  getDurationLabel(seconds: number): string {
    if (!seconds || seconds <= 0) return "0:00";

    const total = Math.floor(seconds);
    const h = Math.floor(total / 3600);
    const m = Math.floor((total % 3600) / 60);
    const s = total % 60;

    if (h > 0) {
      return `${h}:${m.toString().padStart(2, "0")}:${s.toString().padStart(2, "0")}`;
    }

    return `${m}:${s.toString().padStart(2, "0")}`;
  }

    
}
