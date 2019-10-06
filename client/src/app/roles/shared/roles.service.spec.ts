import { HttpClient, HttpParams } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { ParamsService } from '../../core/request/params/params.service';
import { getRandomNumber } from '../../shared/test-util/functions.util';
import { RolesService } from './roles.service';
import { AppConfig } from '../../core/config/app.config';
import { MockAppConfig } from '../../shared/test-util/mock/app.config';

describe('RolesService', () => {
  const url = MockAppConfig.settings.apiServer.url;
  const httpParamsValue: HttpParams = (<unknown>{ someRandomProperty: 'someRandomValue' + getRandomNumber() }) as HttpParams;

  let paramsServiceSpy: jasmine.SpyObj<ParamsService>;
  let httpClientSpy: jasmine.SpyObj<HttpClient>;
  let roleService: RolesService;

  beforeEach(() => {
    paramsServiceSpy = jasmine.createSpyObj('ParamsService', ['getHttpParams']);
    paramsServiceSpy.getHttpParams.and.returnValue(httpParamsValue);

    httpClientSpy = jasmine.createSpyObj('HttpClient', ['get', 'delete']);

    TestBed.configureTestingModule({
      providers: [
        RolesService,
        { provide: ParamsService, useValue: paramsServiceSpy },
        { provide: HttpClient, useValue: httpClientSpy }
      ]
    });
    AppConfig.settings = MockAppConfig.settings;
    roleService = TestBed.get(RolesService);
  });

  it('should be created', () => {
    expect(roleService).toBeTruthy();
  });

  it('#getRoles should call HttpClient#get with the proper parameters, retrieved from ParamsService#getHttpParams', () => {
    const size = getRandomNumber(1, 200);
    const page = getRandomNumber(1, 300);
    const params: { [key: string]: number } = {};
    params[ParamsService.SIZE] = size;
    params[ParamsService.PAGE] = page;

    roleService.getRoles(size, page);

    expect(paramsServiceSpy.getHttpParams).toHaveBeenCalledTimes(1);
    expect(paramsServiceSpy.getHttpParams).toHaveBeenCalledWith(params);
    expect(httpClientSpy.get).toHaveBeenCalledTimes(1);
    expect(httpClientSpy.get).toHaveBeenCalledWith(`${url}role`, { params: httpParamsValue });
  });

  it('#getRoleInfo should call HttpClient#get with the provided id as parameter', () => {
    const id = getRandomNumber();
    roleService.getRoleInfo(id);

    expect(httpClientSpy.get).toHaveBeenCalledTimes(1);
    expect(httpClientSpy.get).toHaveBeenCalledWith(`${url}role/${id}`);
  });

  it('#deleteRoleInfo should call HttpClient#delete with the provided id as parameter', () => {
    const id = getRandomNumber();
    roleService.deleteRoleInfo(id);

    expect(httpClientSpy.delete).toHaveBeenCalledTimes(1);
    expect(httpClientSpy.delete.calls.mostRecent().args[0]).toEqual(`${url}role/${id}`);
  });

  it('#getRolePermissions should call HttpClient#get with the provided proper parameters', () => {
    const id = getRandomNumber();
    const size = getRandomNumber(1, 200);
    const page = getRandomNumber(1, 300);
    const params: { [key: string]: number } = {};
    params[ParamsService.SIZE] = size;
    params[ParamsService.PAGE] = page - 1;

    roleService.getRolePermissions(id, size, page);

    expect(paramsServiceSpy.getHttpParams).toHaveBeenCalledTimes(1);
    expect(paramsServiceSpy.getHttpParams).toHaveBeenCalledWith(params);

    expect(httpClientSpy.get).toHaveBeenCalledTimes(1);
    expect(httpClientSpy.get.calls.mostRecent().args[0]).toEqual(`${url}role/${id}/permissions`);
  });
});
