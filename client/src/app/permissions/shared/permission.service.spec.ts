import { HttpClient } from '@angular/common/http';
import { inject, TestBed } from '@angular/core/testing';
import { environment } from '../../../environments/environment';
import { ParamsService } from '../../core/request/params/params.service';
import { getRandomNumber } from '../../shared/test-util/functions.util';
import { PermissionService } from './permission.service';

describe('PermissionService', () => {
  const spy = { getHttpParams: (_a: any) => { }, get: (_a: any, _b: any) => { } };
  const httpParamsValue = { someRandomProperty: 'someRandomValue' + getRandomNumber() };
  const paramsServiceStub = { getHttpParams: (a: any) => { spy.getHttpParams(a); return httpParamsValue; } };
  const httpClientMock = { get: (a: any, b: any) => spy.get(a, b) };
  let permissionService: PermissionService;
  let httpClientGetSpy: jasmine.Spy;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        PermissionService,
        { provide: ParamsService, useValue: paramsServiceStub },
        { provide: HttpClient, useValue: httpClientMock }
      ]
    });
    permissionService = TestBed.get(PermissionService);
    httpClientGetSpy = spyOn(spy, 'get');
  });

  it('should be created', inject([PermissionService], (service: PermissionService) => {
    expect(service).toBeTruthy();
  }));

  it('#getPermissions should call HttpClient#get with the proper parameters, retrieved from ParamsService#getHttpParams', () => {
    const paramsServiceGetParamsSpy = spyOn(spy, 'getHttpParams');
    const size = getRandomNumber(1, 200);
    const page = getRandomNumber(1, 300);
    const params: { [key: string]: number } = {};
    params[ParamsService.SIZE] = size;
    params[ParamsService.PAGE] = page - 1;

    permissionService.getPermissions(size, page);

    expect(paramsServiceGetParamsSpy).toHaveBeenCalledTimes(1);
    expect(paramsServiceGetParamsSpy).toHaveBeenCalledWith(params);
    expect(httpClientGetSpy).toHaveBeenCalledTimes(1);
    expect(httpClientGetSpy).toHaveBeenCalledWith(`${environment.apiBaseUrl}permission`, { params: httpParamsValue });
  });

  it('#getPermissionInfo should call HttpClient#get with the provided id as parameter', () => {
    const id = getRandomNumber();
    permissionService.getPermissionInfo(id);

    expect(httpClientGetSpy).toHaveBeenCalledTimes(1);
    expect(httpClientGetSpy).toHaveBeenCalledWith(`${environment.apiBaseUrl}permission/${id}`, undefined); // undefined for no params
  });

  it('#getPermissionRoles should call HttpClient#get with the provided id as parameter', () => {
    const paramsServiceGetParamsSpy = spyOn(spy, 'getHttpParams');
    const size = getRandomNumber(1, 200);
    const page = getRandomNumber(1, 300);
    const params: { [key: string]: number } = {};
    params[ParamsService.SIZE] = size;
    params[ParamsService.PAGE] = page - 1;
    const id = getRandomNumber();
    permissionService.getPermissionRoles(id, size, page);

    expect(paramsServiceGetParamsSpy).toHaveBeenCalledTimes(1);
    expect(paramsServiceGetParamsSpy).toHaveBeenCalledWith(params);

    expect(httpClientGetSpy).toHaveBeenCalledTimes(1);
    expect(httpClientGetSpy.calls.mostRecent().args[0]).toEqual(`${environment.apiBaseUrl}permission/${id}/roles`);
  });

});
