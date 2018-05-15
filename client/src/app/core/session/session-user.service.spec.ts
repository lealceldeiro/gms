import { inject, TestBed } from '@angular/core/testing';

import { SessionUserService } from './session-user.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('SessionUserService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionUserService]
    });
  });

  it('should be created', inject([SessionUserService], (service: SessionUserService) => {
    expect(service).toBeTruthy();
  }));
});
