import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { VideoUpload } from '../../model/videoUpload';
import { HttpEvent } from '@angular/common/http';


@Injectable({
  providedIn: 'root',
})
export class UploadService  {
  private readonly API_URL = 'http://localhost:8080';
  constructor(private http: HttpClient) {}

  uploadVideo(formData: FormData): Observable<HttpEvent<VideoUpload>> {
    return this.http.post<VideoUpload>(`${this.API_URL}/upload`, formData, {
      reportProgress: true,
      observe: 'events',
    })
  }

  getAllUploads(): Observable<VideoUpload[]> {
    return this.http.get<VideoUpload[]>(`${this.API_URL}/getAllUploads`);
  }
}
