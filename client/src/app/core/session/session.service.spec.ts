import { inject, TestBed } from '@angular/core/testing';

import { SessionService } from './session.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('SessionService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionService]
    });
  });

  it('should be created', inject([SessionService], (service: SessionService) => {
    expect(service).toBeTruthy();
  }));
});
