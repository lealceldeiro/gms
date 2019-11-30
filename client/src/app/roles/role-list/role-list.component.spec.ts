import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { of } from 'rxjs';

import { SharedModule } from '../../shared/shared.module';
import { getRandomNumber } from '../../shared/test-util/functions.util';
import { DummyStubComponent } from '../../shared/test-util/mock/dummy-stub.component';
import { MockModule } from '../../shared/test-util/mock/mock.module';
import { RolePd } from '../shared/role.pd';
import { RolesService } from '../shared/roles.service';
import { RoleListComponent } from './role-list.component';

describe('RoleListComponent', () => {
  const getSampleData = () => {
    return {
      _embedded: { role: [] },
      _links: { self: { href: 'href' } },
      page: {
        number: 0,
        // these are changed later
        totalElements: 0,
        size: 0,
        totalPages: 0
      }
    };
  };
  const addRolesToSampleData = (sampleData: { _embedded: any; _links?: { self: { href: string; }; }; page: any; }) => {
    const rangeBottom = 25;
    const rangeTop = 40;
    const randomAmountValue = getRandomNumber(rangeBottom, rangeTop);
    for (let i = 0; i < randomAmountValue; i++) {
      sampleData._embedded.role.push({ description: 'd' + i, label: 'l' + i, id: i, enabled: i % 2 === 0 });
    }
    // re-calculate values accordingly to the random value
    sampleData.page.size = randomAmountValue - 7;
    sampleData.page.totalElements = randomAmountValue;
    sampleData.page.totalPages = Math.floor(sampleData.page.totalElements / sampleData.page.size);
  };

  let sampleDataValue: RolePd;
  let rolesServiceSpy: jasmine.SpyObj<RolesService>;

  let component: RoleListComponent;
  let fixture: ComponentFixture<RoleListComponent>;
  let componentEl: HTMLElement;

  beforeEach(async(() => {
    // make the observable return a random value for each test
    sampleDataValue = getSampleData();
    addRolesToSampleData(sampleDataValue);

    rolesServiceSpy = jasmine.createSpyObj('RolesService', ['getRoles']);
    rolesServiceSpy.getRoles.and.returnValue(of(sampleDataValue));

    TestBed.configureTestingModule({
      declarations: [RoleListComponent],
      imports: [SharedModule, MockModule, RouterTestingModule.withRoutes([{ path: './', component: DummyStubComponent }])],
      providers: [{ provide: RolesService, useValue: rolesServiceSpy }]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RoleListComponent);
    component = fixture.componentInstance;
    componentEl = fixture.nativeElement;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render a table with as many rows as elements returned by the (mock) service', () => {
    const tr = componentEl.querySelectorAll('tr.p-row');
    expect(tr.length).toBe(sampleDataValue._embedded.role.length, 'rows count does not match the total elements');
  });

  it('should call service for getting new roles list when #loadList is called', () => {
    const toPage = 3;
    rolesServiceSpy.getRoles.calls.reset();

    component.loadList(toPage);

    expect(rolesServiceSpy.getRoles).toHaveBeenCalledTimes(1);
    expect(rolesServiceSpy.getRoles).toHaveBeenCalledWith(component.page.size, toPage - 1);
  });
});
