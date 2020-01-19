import { Location } from '@angular/common';
import { async, ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { ActivatedRoute, ParamMap } from '@angular/router';

import { BehaviorSubject, Observable, of } from 'rxjs';

import { Role } from '../../roles/shared/role.model';
import { RolePd } from '../../roles/shared/role.pd';
import { SharedModule } from '../../shared/shared.module';
import { getRandomNumber } from '../../shared/test-util/functions.util';
import { Permission } from '../shared/permission.model';
import { PermissionService } from '../shared/permission.service';
import { PermissionInfoComponent } from './permission-info.component';

describe('PermissionInfoComponent', () => {
  const fakeRoles: Array<Role> = [
    new Role(),
    {
      description: 'des-' + getRandomNumber(),
      enabled: (getRandomNumber() % 2 === 0),
      id: getRandomNumber(),
      label: ('label-' + getRandomNumber())
    }
  ];
  const fakeRolesResult: Observable<RolePd> = new BehaviorSubject<RolePd>({
    _embedded: { role: fakeRoles },
    _links: { self: { href: 'href' } },
    page: {
      number: 0,
      totalElements: 0,
      size: 0,
      totalPages: 0
    }
  } as RolePd).asObservable();
  const id = getRandomNumber();

  let component: PermissionInfoComponent;
  let fixture: ComponentFixture<PermissionInfoComponent>;

  let permissionServiceSpy: jasmine.SpyObj<PermissionService>;
  let locationSpy: jasmine.SpyObj<Location>;
  let paramMapSpy: jasmine.SpyObj<ParamMap>;
  let activatedRouteStub: ActivatedRoute;

  beforeEach(async(() => {
    permissionServiceSpy = jasmine.createSpyObj('PermissionService', ['getPermissionInfo', 'getPermissionRoles']);
    permissionServiceSpy.getPermissionInfo.and.returnValue(of(new Permission()));
    permissionServiceSpy.getPermissionRoles.and.returnValue(fakeRolesResult);

    locationSpy = jasmine.createSpyObj('Location', ['back']);

    paramMapSpy = jasmine.createSpyObj('paramMapSpy', ['get']);
    paramMapSpy.get.and.returnValue(id.toString());

    activatedRouteStub = ({ snapshot: { paramMap: paramMapSpy } } as unknown) as ActivatedRoute;

    TestBed.configureTestingModule({
      imports: [SharedModule],
      declarations: [PermissionInfoComponent],
      providers: [
        { provide: PermissionService, useValue: permissionServiceSpy },
        { provide: Location, useValue: locationSpy },
        { provide: ActivatedRoute, useValue: activatedRouteStub }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PermissionInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call PermissionService#getPermissionInfo on init', () => {
    // ngOnInit called by component creation in beforeEach
    expect(permissionServiceSpy.getPermissionInfo).toHaveBeenCalledTimes(1);
    expect(permissionServiceSpy.getPermissionInfo).toHaveBeenCalledWith(id);
  });

  it('should not call PermissionService#getPermissionInfo, but Location#back if the id is an invalid value', () => {
    permissionServiceSpy.getPermissionInfo.calls.reset();
    paramMapSpy.get.and.returnValue(null);
    component['getPermissionInfo']();
    expect(permissionServiceSpy.getPermissionInfo).not.toHaveBeenCalled();
    expect(locationSpy.back).toHaveBeenCalledTimes(1);
  });

  it('should call Location#back on call to back function', () => {
    component['back']();
    expect(locationSpy.back).toHaveBeenCalledTimes(1);
  });

  it('should show a text to allow retrieving the roles using the selected permission and hide the table of roles',
    () => {
      expect(component.rolesLoaded).toEqual(false);
      expect(component.roles).toEqual([]);
      expect(fixture.debugElement.query(By.css('#showRolesLnk'))).toBeTruthy();
      expect(fixture.debugElement.query(By.css('#rolesTable'))).toBeFalsy();
    }
  );

  it('should hide the text to allow retrieving the roles using the selected permission and show the table of roles',
    fakeAsync(() => {
      const size = getRandomNumber();
      const page = getRandomNumber();
      component.page.size = size;
      component.showInRoles(page);
      fixture.detectChanges();
      tick();
      expect(permissionServiceSpy.getPermissionRoles).toHaveBeenCalledTimes(1);
      expect(permissionServiceSpy.getPermissionRoles).toHaveBeenCalledWith(id, size, page);
      expect(component.rolesLoaded).toEqual(true);
      expect(component.roles).toEqual(fakeRoles);
      expect(fixture.debugElement.query(By.css('#showRolesLnk'))).toBeFalsy();
      expect(fixture.debugElement.query(By.css('#rolesTable'))).toBeTruthy();
    }));

  it('should use `this.page.current` as default if not page is provided to `#showInPermissions`', () => {
    const size = getRandomNumber();
    const page = getRandomNumber();
    component.page.current = page;
    component.page.size = size;
    component.showInRoles();
    fixture.detectChanges();
    expect(permissionServiceSpy.getPermissionRoles).toHaveBeenCalledTimes(1);
    expect(permissionServiceSpy.getPermissionRoles).toHaveBeenCalledWith(id, size, page);
  });
});
