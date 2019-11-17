import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { of } from 'rxjs';

import { DummyStubComponent } from '../../shared/test-util/mock/dummy-stub.component';
import { MockModule } from '../../shared/test-util/mock/mock.module';
import { SharedModule } from '../../shared/shared.module';
import { getRandomNumber } from '../../shared/test-util/functions.util';
import { PermissionPd } from '../shared/permission.pd';
import { PermissionService } from '../shared/permission.service';
import { PermissionListComponent } from './permission-list.component';

describe('PermissionListComponent', () => {
  const getSampleData = () => {
    return {
      _embedded: {
        permission: []
      },
      _links: {
        self: {
          href: 'href'
        }
      },
      page: {
        number: 0,
        // these are changed later
        totalElements: 0,
        size: 0,
        totalPages: 0
      }
    };
  };
  const addPermissionsToSampleData = (sampleData: { _embedded: any; _links?: { self: { href: string; }; }; page: any; }) => {
    const rangeBottom = 25;
    const rangeTop = 40;
    const randomAmountValue = getRandomNumber(rangeBottom, rangeTop);
    for (let i = 0; i < randomAmountValue; i++) {
      sampleData._embedded.permission.push({ name: 'n' + i, label: 'l' + i, id: i });
    }
    // re-calculate values accordingly to the random value
    sampleData.page.size = randomAmountValue - 7;
    sampleData.page.totalElements = randomAmountValue;
    sampleData.page.totalPages = Math.floor(sampleData.page.totalElements / sampleData.page.size);
  };

  let component: PermissionListComponent;
  let fixture: ComponentFixture<PermissionListComponent>;
  let componentEl: HTMLElement;

  let sampleDataValue: PermissionPd;
  let permissionServiceSpy: jasmine.SpyObj<PermissionService>;

  beforeEach(async(() => {
    sampleDataValue = getSampleData();
    addPermissionsToSampleData(sampleDataValue); // make the observable return a random value for each test

    permissionServiceSpy = jasmine.createSpyObj('PermissionService', ['getPermissions']);
    permissionServiceSpy.getPermissions.and.returnValue(of(sampleDataValue));

    TestBed.configureTestingModule({
      declarations: [PermissionListComponent],
      imports: [SharedModule, MockModule, RouterTestingModule.withRoutes([{ path: './', component: DummyStubComponent }])],
      providers: [{ provide: PermissionService, useValue: permissionServiceSpy }]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PermissionListComponent);
    component = fixture.componentInstance;
    componentEl = fixture.nativeElement;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render a table with as many rows as elements returned by the (mock) service', () => {
    const tr = componentEl.querySelectorAll('tr.p-row');
    expect(tr.length).toBe(sampleDataValue._embedded.permission.length, 'rows count does not match the total elements');
  });

  it('should call service for getting new permissions list when #loadList is called', () => {
    const toPage = 3;
    permissionServiceSpy.getPermissions.calls.reset();

    component.loadList(toPage);

    expect(permissionServiceSpy.getPermissions).toHaveBeenCalledTimes(1);
    expect(permissionServiceSpy.getPermissions).toHaveBeenCalledWith(component.page.size, toPage);
  });
});
