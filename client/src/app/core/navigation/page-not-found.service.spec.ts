import { inject, TestBed } from '@angular/core/testing';

import { PageNotFoundService } from './page-not-found.service';

describe('PageNotFoundService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [PageNotFoundService]
    });
  });

  it('should be created', inject([PageNotFoundService], (service: PageNotFoundService) => {
    expect(service).toBeTruthy();
  }));
});
