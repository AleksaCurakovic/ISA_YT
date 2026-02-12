import { TestBed } from '@angular/core/testing';

import { WatchpartyService } from './watchparty-service';

describe('WatchpartyService', () => {
  let service: WatchpartyService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WatchpartyService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
