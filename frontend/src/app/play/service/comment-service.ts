import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comment } from '../../model/comment'

@Injectable({
  providedIn: 'root',
})
export class CommentService {
  private readonly API_URL = 'http://localhost:8080';
  constructor(private http: HttpClient){}

  getVideoComments(id: number): Observable<Comment[]>{
    return this.http.get<Comment[]>(`${this.API_URL}/comments/${id}`)
  }
}
