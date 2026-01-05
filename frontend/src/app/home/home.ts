import { Component, OnInit, signal } from '@angular/core';
import { UploadService } from '../upload/service/upload-service';
import { VideoUpload } from '../model/videoUpload';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [CommonModule],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {

  videoUploads = signal<VideoUpload[]> ([]);

  constructor(private uploadService: UploadService, private router: Router) { }

  ngOnInit(): void {
    this.uploadService.getAllUploads().subscribe(uploads => {
      this.videoUploads.set(uploads) 
      console.log('Fetched uploads:', this.videoUploads);
    });
  }

  playVideo(id:number): void {
    this.router.navigate(['/play'], { queryParams: { videoId: id } })
  }
}
