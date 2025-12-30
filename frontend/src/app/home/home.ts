import { Component, OnInit, signal } from '@angular/core';
import { UploadService } from '../upload/service/upload-service';
import { VideoUpload } from '../model/videoUpload';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  imports: [CommonModule],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {

  videoUploads = signal<VideoUpload[]> ([]);

  constructor(private uploadService: UploadService) { }

  ngOnInit(): void {
    this.uploadService.getAllUploads().subscribe(uploads => {
      this.videoUploads.set(uploads) 
      console.log('Fetched uploads:', this.videoUploads);
    });
  }
}
