import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Auth } from '../../infrastructure/service/auth'
import { ActivatedRoute, Router } from '@angular/router';
import { Profile } from '../../model/profile'
import { VideoPreview } from '../../model/videoPreview'
import { UploadService } from '../../upload/service/upload-service'

@Component({
  selector: 'app-profile-overview',
  imports: [CommonModule],
  templateUrl: './profile-overview.html',
  styleUrl: './profile-overview.css',
})
export class ProfileOverview implements OnInit {
  profile = signal<Profile | null>(null)
  videos: VideoPreview[] = []
  activeTab: 'videos' | 'about' = 'videos';


  constructor(private authService: Auth, private route: ActivatedRoute, private uploadService: UploadService,
              private router: Router
  ){}


  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const userName = params['userName'];
      this.authService.whoAreYou(userName).subscribe(profile => {
        this.profile.set(profile)
      })
      this.uploadService.getUserUploads(userName).subscribe(videos => {
        this.videos = videos
      })
    });
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