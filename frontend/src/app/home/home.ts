import { Component, OnInit, signal } from '@angular/core';
import { UploadService } from '../upload/service/upload-service';
import { VideoPreview } from '../model/videoPreview';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [CommonModule],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {

  videoPreview = signal<VideoPreview[]> ([]);

  constructor(private uploadService: UploadService, private router: Router) { }

  ngOnInit(): void {
    this.uploadService.getAllUploads().subscribe(uploads => {
      this.videoPreview.set(uploads) 
      console.log('Fetched uploads:', this.videoPreview);
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
