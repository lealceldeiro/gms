import { Location } from '@angular/common';
import { async, ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { ActivatedRoute, ParamMap } from '@angular/router';

import { BehaviorSubject, Observable, of } from 'rxjs';

import { SharedModule } from '../../shared/shared.module';
import { Permission } from '../../permissions/shared/permission.model';
import { PermissionPd } from '../../permissions/shared/permission.pd';
import { getRandomNumber } from '../../shared/test-util/functions.util';
import { Role } from '../shared/role.model';
import { RolesService } from '../shared/roles.service';
import { RoleInfoComponent } from './role-info.component';

describe('RoleInfoComponent', () => {
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
  const id = getRandomNumber();

  let roleServiceSpy: jasmine.SpyObj<RolesService>;
  let locationSpy: jasmine.SpyObj<Location>;
  let paramMapSpy: jasmine.SpyObj<ParamMap>;
  let activatedRouteStub: ActivatedRoute;

  let component: RoleInfoComponent;
  let fixture: ComponentFixture<RoleInfoComponent>;

  beforeEach(async(() => {
    roleServiceSpy = jasmine.createSpyObj('RolesService', ['getRoleInfo', 'getRolePermissions']);
    roleServiceSpy.getRoleInfo.and.returnValue(of(new Role()));
    roleServiceSpy.getRolePermissions.and.returnValue(fakePermissionsResult);

    locationSpy = jasmine.createSpyObj('Location', ['back']);

    paramMapSpy = jasmine.createSpyObj('paramMapSpy', ['get']);
    paramMapSpy.get.and.returnValue(id.toString());

    activatedRouteStub = (<unknown>{ snapshot: { paramMap: paramMapSpy } }) as ActivatedRoute;

    TestBed.configureTestingModule({
      imports: [SharedModule],
      declarations: [RoleInfoComponent],
      providers: [
        { provide: RolesService, useValue: roleServiceSpy },
        { provide: Location, useValue: locationSpy },
        { provide: ActivatedRoute, useValue: activatedRouteStub }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RoleInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call RolesService#getRoleInfo on init', () => {
    // ngOnInit called by component creation in beforeEach
    expect(roleServiceSpy.getRoleInfo).toHaveBeenCalledTimes(1);
    expect(roleServiceSpy.getRoleInfo).toHaveBeenCalledWith(id);
  });

  it('should not call RolesService#getRoleInfo, but Location#back if the id is an invalid value', () => {
    roleServiceSpy.getRoleInfo.calls.reset();
    paramMapSpy.get.and.returnValue(null);
    component['getRoleInfo']();
    expect(roleServiceSpy.getRoleInfo).not.toHaveBeenCalled();
    expect(locationSpy.back).toHaveBeenCalledTimes(1);
  });

  it('should show a text to allow retrieving the roles using the selected permission and hide the table of roles', () => {
    expect(component.permissionsLoaded).toEqual(false);
    expect(component.permissions).toEqual([]);
    expect(fixture.debugElement.query(By.css('#showPermissionsLnk'))).toBeTruthy();
    expect(fixture.debugElement.query(By.css('#permissionsTable'))).toBeFalsy();
  });

  it('should hide the text to allow retrieving the roles using the selected permission and show the table of roles', fakeAsync(() => {
    const size = getRandomNumber();
    const page = getRandomNumber();
    component.page.size = size;
    component.showInPermissions(page);
    fixture.detectChanges();
    tick();
    expect(roleServiceSpy.getRolePermissions).toHaveBeenCalledTimes(1);
    expect(roleServiceSpy.getRolePermissions).toHaveBeenCalledWith(id, size, page);
    expect(component.permissionsLoaded).toEqual(true);
    expect(component.permissions).toEqual(fakePermissions);
    expect(fixture.debugElement.query(By.css('#showPermissionsLnk'))).toBeFalsy();
    expect(fixture.debugElement.query(By.css('#permissionsTable'))).toBeTruthy();
  }));

  it('should use `this.page.current` as default if not page is provided to `#showInPermissions`', () => {
    const size = getRandomNumber();
    const page = getRandomNumber();
    component.page.current = page;
    component.page.size = size;
    component.showInPermissions();
    fixture.detectChanges();
    expect(roleServiceSpy.getRolePermissions).toHaveBeenCalledTimes(1);
    expect(roleServiceSpy.getRolePermissions).toHaveBeenCalledWith(id, size, page);
  });
});
