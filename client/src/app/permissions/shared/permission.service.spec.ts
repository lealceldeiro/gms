import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, TestBed } from '@angular/core/testing';

import { AppConfig } from '../../core/config/app.config';
import { ParamsService } from '../../core/request/params/params.service';
import { getRandomNumber } from '../../shared/test-util/functions.util';
import { MockAppConfig } from '../../shared/test-util/mock/app.config';
import { PermissionService } from './permission.service';

describe('PermissionService', () => {
  const url = MockAppConfig.settings.apiServer.url;
  const httpParamsValue: HttpParams = ({ someRandomProperty: 'someRandomValue' + getRandomNumber() } as unknown) as HttpParams;
  let paramsServiceSpy: jasmine.SpyObj<ParamsService>;
  let httpClientSpy: jasmine.SpyObj<HttpClient>;
  let permissionService: PermissionService;

  beforeEach(() => {
    paramsServiceSpy = jasmine.createSpyObj('ParamsService', ['getHttpParams']);
    paramsServiceSpy.getHttpParams.and.returnValue(httpParamsValue);

    httpClientSpy = jasmine.createSpyObj('HttpClient', ['get']);

    TestBed.configureTestingModule({
      providers: [
        PermissionService,
        { provide: ParamsService, useValue: paramsServiceSpy },
        { provide: HttpClient, useValue: httpClientSpy }
      ]
    });
    AppConfig.settings = MockAppConfig.settings;
    permissionService = TestBed.get(PermissionService);
  });

  it('should be created', inject([PermissionService], (service: PermissionService) => {
    expect(service).toBeTruthy();
  }));

  it('#getPermissions should call HttpClient#get with the proper parameters, retrieved from ParamsService#getHttpParams', () => {
    const size = getRandomNumber(1, 200);
    const page = getRandomNumber(1, 300);
    const params: { [key: string]: number } = {};
    params[ParamsService.SIZE] = size;
    params[ParamsService.PAGE] = page - 1;

    permissionService.getPermissions(size, page);

    expect(paramsServiceSpy.getHttpParams).toHaveBeenCalledTimes(1);
    expect(paramsServiceSpy.getHttpParams).toHaveBeenCalledWith(params);
    expect(httpClientSpy.get).toHaveBeenCalledTimes(1);
    expect(httpClientSpy.get).toHaveBeenCalledWith(`${url}permission`, { params: httpParamsValue });
  });

  it('#getPermissionInfo should call HttpClient#get with the provided id as parameter', () => {
    const id = getRandomNumber();
    permissionService.getPermissionInfo(id);

    expect(httpClientSpy.get).toHaveBeenCalledTimes(1);
    expect(httpClientSpy.get).toHaveBeenCalledWith(`${url}permission/${id}`);
  });

  it('#getPermissionRoles should call HttpClient#get with the provided id as parameter', () => {
    const size = getRandomNumber(1, 200);
    const page = getRandomNumber(1, 300);
    const params: { [key: string]: number } = {};
    params[ParamsService.SIZE] = size;
    params[ParamsService.PAGE] = page - 1;
    const id = getRandomNumber();
    permissionService.getPermissionRoles(id, size, page);

    expect(paramsServiceSpy.getHttpParams).toHaveBeenCalledTimes(1);
    expect(paramsServiceSpy.getHttpParams).toHaveBeenCalledWith(params);

    expect(httpClientSpy.get).toHaveBeenCalledTimes(1);
    expect(httpClientSpy.get.calls.mostRecent().args[0]).toEqual(`${url}permission/${id}/roles`);
  });
});
