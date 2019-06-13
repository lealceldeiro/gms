import { HttpClient } from '@angular/common/http';
import { inject, TestBed } from '@angular/core/testing';
import { environment } from '../../../environments/environment';
import { ParamsService } from '../../core/request/params/params.service';
import { getRandomNumber } from '../../shared/test-util/functions.util';
import { PermissionService } from './permission.service';

describe('PermissionService', () => {
  const spy = { getHttpParams: (a) => { }, get: (a, b) => { } };
  const httpParamsValue = { someRandomProperty: 'someRandomValue' + getRandomNumber() };
  const paramsServiceStub = {
    getHttpParams: (a) => { spy.getHttpParams(a); return httpParamsValue; }
  };

  const httpClientMock = {
    get: (a, b) => spy.get(a, b)
  };

  let permissionService: PermissionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        PermissionService,
        { provide: ParamsService, useValue: paramsServiceStub },
        { provide: HttpClient, useValue: httpClientMock }
      ]
    });
    permissionService = TestBed.get(PermissionService);
  });

  it('should be created', inject([PermissionService], (service: PermissionService) => {
    expect(service).toBeTruthy();
  }));

  it('#getPermissions should call HttpClient#get with the proper parameters, retrieved from ParamsService#getHttpParams', () => {
    const paramsServiceGetParamsSpy = spyOn(spy, 'getHttpParams');
    const httpClientGetSpy = spyOn(spy, 'get');
    const size = getRandomNumber(1, 200);
    const page = getRandomNumber(1, 300);
    const params = {};
    params[ParamsService.SIZE] = size;
    params[ParamsService.PAGE] = page;

    permissionService.getPermissions(size, page);

    expect(paramsServiceGetParamsSpy).toHaveBeenCalledTimes(1);
    expect(paramsServiceGetParamsSpy).toHaveBeenCalledWith(params);
    expect(httpClientGetSpy).toHaveBeenCalledTimes(1);
    expect(httpClientGetSpy).toHaveBeenCalledWith(environment.apiBaseUrl + 'permission', { params: httpParamsValue });
  });
});
