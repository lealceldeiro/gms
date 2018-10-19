import { inject, TestBed } from '@angular/core/testing';

import { PermissionService } from './permission.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('PermissionService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [PermissionService]
    });
  });

  it('should be created', inject([PermissionService], (service: PermissionService) => {
    expect(service).toBeTruthy();
  }));
});
