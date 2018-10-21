import { TestBed } from '@angular/core/testing';

import { ParamsService } from './params.service';

describe('ParamsService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ParamsService = TestBed.get(ParamsService);
    expect(service).toBeTruthy();
  });
});
