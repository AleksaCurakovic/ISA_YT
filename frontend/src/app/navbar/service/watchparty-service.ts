import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { WatchParty } from '../../model/watchParty';
import { Auth } from '../../infrastructure/service/auth';

@Injectable({
  providedIn: 'root',
})
export class WatchpartyService {
  private readonly API_URL = 'http://localhost:8080';
  private stompClient: Client | null = null;
  private activePartySessionSubject = new BehaviorSubject<WatchParty | null>(null);
  public activePartySession$ = this.activePartySessionSubject.asObservable();
  public loggedInUsername: string | null = null;
  constructor(private http: HttpClient, private toastr: ToastrService, private router: Router, private authService: Auth) {
    this.authService.authState$.subscribe(username => {
      if (username) {
        this.loggedInUsername = username;
        this.getCurrentWatchParty(username);
      }
    })
  }

  createWatchParty(username: string) : Observable<any> {
    const newParty: WatchParty = { host: username };
    this.activePartySessionSubject.next(newParty);
    this.configureHostWebSocketConnection(username);
    this.toastr.success('Watch party created');
    return this.http.post(`${this.API_URL}/watchparty/create`, {});
  }

  getCurrentWatchParty(username: string): void {
    this.http.get<WatchParty>(`${this.API_URL}/watchparty/get`).subscribe(party => {
      if (party){ 
        this.activePartySessionSubject.next(party);
        if(party.host === username){
          this.configureHostWebSocketConnection(username);
        }else{
          this.configureGuestWebSocketConnection(party.host, username);
        }
      }
      
    })
  }


  leaveWatchParty(party: WatchParty): void {
    if (party.host === this.loggedInUsername){
      this.stompClient!.publish({ 
      destination: `/socket-message/send`,
      body: JSON.stringify({ host: party.host, videoId: -1 })
      });
      this.toastr.info('Watch party deleted');
      this.http.delete(`${this.API_URL}/watchparty/delete/${party.id}`).subscribe();
    }else{
      this.http.patch<null>(`${this.API_URL}/watchparty/leave`, {}).subscribe();
      this.stompClient!.publish({
            destination: `/socket-message/send`,
            body: JSON.stringify({ host: party.host, guest: this.loggedInUsername , joined: "false" })
        });
    }
    this.activePartySessionSubject.next(null);
    this.stompClient!.deactivate();
  }

  joinWatchParty(party: WatchParty, guestUsername: string): void{
    this.activePartySessionSubject.next(party);
    this.configureGuestWebSocketConnection(party.host, guestUsername);
    if (party.id)
      this.http.patch<WatchParty>(`${this.API_URL}/watchparty/join/${party.id}`, {}).subscribe();
  }

  getWatchParites(): Observable<WatchParty[]> {
    return this.http.get<WatchParty[]>(`${this.API_URL}/watchparty/list`);
  }

  startWatchParty(hostUsername: string, videoId: number): void {
    this.stompClient!.publish({ 
      destination: `/socket-message/send`,
      body: JSON.stringify({ host: hostUsername, videoId: videoId })
    });
  }

  configureHostWebSocketConnection(hostUsername: string): void {
   this.stompClient = new Client({

      webSocketFactory: () =>
        new SockJS(this.API_URL + '/socket'),

      reconnectDelay: 5000,

      debug: (str) => {
        console.log(str);
      },

      onConnect: () => {
        console.log("Host WebSocket connected");
        this.stompClient!.subscribe(`/watchparty/${hostUsername}`, (message: IMessage) => {
          this.toastr.info(message.body);
        });
      },

      onStompError: (frame) => {
        console.error("Broker error:", frame.headers['message']);
      }
    });
    this.stompClient.activate();
  }

  configureGuestWebSocketConnection(hostUsername: string, guestUsername: string): void {
    this.stompClient = new Client({

      webSocketFactory: () =>
        new SockJS(this.API_URL + '/socket'),

      reconnectDelay: 5000,

      debug: (str) => {
        console.log(str);
      },

      onConnect: () => {
        console.log("Guest WebSocket connected");
        this.stompClient!.subscribe(`/watchparty/${hostUsername}/guests`, (message: IMessage) => {
          if (message.body === '-1'){
            this.toastr.info("Host disconneted, leaving watch party")
            this.leaveWatchParty(this.activePartySessionSubject.value!)
            return
          }
          this.toastr.info("Host started the watch party")
          this.router.navigate(['/play'], { queryParams: { videoId: message.body } })
        });
        this.stompClient!.publish({
            destination: `/socket-message/send`,
            body: JSON.stringify({ host: hostUsername, guest: guestUsername, joined: "true" })
        });
      },

      onStompError: (frame) => {
        console.error("Broker error:", frame.headers['message']);
      }
    });
    this.stompClient.activate();
  }
}