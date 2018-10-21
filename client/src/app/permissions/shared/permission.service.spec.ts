import { inject, TestBed } from '@angular/core/testing';

import { PermissionService } from './permission.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ParamsService } from '../../core/request/params/params.service';

describe('PermissionService', () => {

  const paramsServiceStub = {
    getPermissions: () => {}
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [PermissionService, { provide: ParamsService, useValue: paramsServiceStub }]
    });
  });

  it('should be created', inject([PermissionService], (service: PermissionService) => {
    expect(service).toBeTruthy();
  }));
});
