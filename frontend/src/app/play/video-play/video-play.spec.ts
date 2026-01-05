import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VideoPlay } from './video-play';

describe('VideoPlay', () => {
  let component: VideoPlay;
  let fixture: ComponentFixture<VideoPlay>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VideoPlay]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VideoPlay);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
