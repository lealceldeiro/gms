import { Location } from '@angular/common';
import { Component, Input } from '@angular/core';
import { async, ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { SharedModule } from 'src/app/shared/shared.module';
import { Permission } from '../../permissions/shared/permission.model';
import { PermissionPd } from '../../permissions/shared/permission.pd';
import { getRandomNumber } from '../../shared/test-util/functions.util';
import { Role } from '../shared/role.model';
import { RolesService } from '../shared/roles.service';
import { RoleInfoComponent } from './role-info.component';

describe('RoleInfoComponent', () => {

  let component: RoleInfoComponent;
  let fixture: ComponentFixture<RoleInfoComponent>;
  const fakePermissions: Array<Permission> = [
    new Permission(),
    {
      name: 'name-' + getRandomNumber(),
      id: getRandomNumber(),
      label: ('label-' + getRandomNumber())
    }
  ];
  const fakePermissionsResult: Observable<PermissionPd> =
    new BehaviorSubject<PermissionPd>({
      _embedded: { permission: fakePermissions },
      _links: { self: { href: 'href' } },
      page: {
        number: 0,
        totalElements: 0,
        size: 0,
        totalPages: 0
      }
    } as PermissionPd).asObservable();
  const spy = {
    location: { back: () => { } },
    role: { getInfo: (_id: any) => { }, getPermissions: (_id: any, _size: any, _page: any) => { } }
  };
  const roleServiceStub = {
    getRoleInfo: (_id: number): Observable<Role> => {
      spy.role.getInfo(_id);
      return new BehaviorSubject<Role>(new Role()).asObservable();
    },
    getRolePermissions: (_id: any, _size: any, _page: any) => {
      spy.role.getPermissions(_id, _size, _page);
      return fakePermissionsResult;
    }
  };
  const locationStub = { back: () => { spy.location.back(); } };
  const id = getRandomNumber();
  let idGetter = (): any => id;
  const activatedRouteStub = { snapshot: { paramMap: { get: (): any => idGetter() } } };

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedModule],
      declarations: [RoleInfoComponent],
      providers: [
        { provide: RolesService, useValue: roleServiceStub },
        { provide: Location, useValue: locationStub },
        { provide: ActivatedRoute, useValue: activatedRouteStub }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    idGetter = (): any => id;
    fixture = TestBed.createComponent(RoleInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call RolesService#getRoleInfo on init', () => {
    const getRoleInfoSpy = spyOn(spy.role, 'getInfo');
    component.ngOnInit();
    expect(getRoleInfoSpy).toHaveBeenCalledTimes(1);
    expect(getRoleInfoSpy).toHaveBeenCalledWith(id);
  });

  it('should not call RolesService#getRoleInfo, but Location#back if the id is an invalid value', () => {
    const getRoleInfoSpy = spyOn(spy.role, 'getInfo');
    const locationBackSpy = spyOn(spy.location, 'back');
    idGetter = (): any => 'someInvalidId';
    component['getRoleInfo']();
    expect(getRoleInfoSpy).not.toHaveBeenCalled();
    expect(locationBackSpy).toHaveBeenCalledTimes(1);
  });

  it('should show a text to allow retrieving the roles using the selected permission and hide the table of roles', () => {
    expect(component.permissionsLoaded).toEqual(false);
    expect(component.permissions).toEqual([]);
    expect(fixture.debugElement.query(By.css('#showPermissionsLnk'))).toBeTruthy();
    expect(fixture.debugElement.query(By.css('#permissionsTable'))).toBeFalsy();
  });

  it('should hide the text to allow retrieving the roles using the selected permission and show the table of roles', fakeAsync(() => {
    const getRolePermissionsSpy = spyOn(spy.role, 'getPermissions');
    const size = getRandomNumber();
    const page = getRandomNumber();
    component.page.size = size;
    component.showInPermissions(page);
    fixture.detectChanges();
    tick();
    expect(getRolePermissionsSpy).toHaveBeenCalledTimes(1);
    expect(getRolePermissionsSpy).toHaveBeenCalledWith(id, size, page);
    expect(component.permissionsLoaded).toEqual(true);
    expect(component.permissions).toEqual(fakePermissions);
    expect(fixture.debugElement.query(By.css('#showPermissionsLnk'))).toBeFalsy();
    expect(fixture.debugElement.query(By.css('#permissionsTable'))).toBeTruthy();
  }));

  it('should use `this.page.current` as default if not page is provided to `#showInPermissions`', () => {
    const getRolePermissionsSpy = spyOn(spy.role, 'getPermissions');
    const size = getRandomNumber();
    const page = getRandomNumber();
    component.page.current = page;
    component.page.size = size;
    component.showInPermissions();
    fixture.detectChanges();
    expect(getRolePermissionsSpy).toHaveBeenCalledTimes(1);
    expect(getRolePermissionsSpy).toHaveBeenCalledWith(id, size, page);
  });
});
