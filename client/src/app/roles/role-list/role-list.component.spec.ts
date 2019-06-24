import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RoleListComponent } from './role-list.component';
import { getRandomNumber } from '../../shared/test-util/functions.util';
import { BehaviorSubject } from 'rxjs';
import { RolePd } from '../shared/role.pd';
import { SharedModule } from 'src/app/shared/shared.module';
import { MockModule } from 'src/app/shared/mock/mock.module';
import { RouterTestingModule } from '@angular/router/testing';
import { DummyStubComponent } from 'src/app/shared/mock/dummy-stub.component';
import { RolesService } from '../shared/roles.service';

describe('RoleListComponent', () => {
  let component: RoleListComponent;
  let fixture: ComponentFixture<RoleListComponent>;
  let componentEl: HTMLElement;

  const getSampleData = () => {
    return {
      _embedded: { role: [] },
      _links: { self: { href: 'href' } },
      page: {
        number: 0,
        totalElements: 0, // changed later
        size: 0,          // changed later
        totalPages: 0     // changed later
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

  const subjectLRM = new BehaviorSubject<RolePd>(getSampleData());
  const ret = () => subjectLRM.asObservable();
  const spy = { getRoles: (a: any, b: any) => { } };
  const rolesServiceStub = { getRoles: (size: number, page: number) => { spy.getRoles(size, page); return ret(); } };

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RoleListComponent ],
      imports: [SharedModule, MockModule, RouterTestingModule.withRoutes([{ path: './', component: DummyStubComponent }])],
      providers: [ { provide: RolesService, useValue: rolesServiceStub }]
    }).compileComponents();
  }));

  beforeEach(() => {
    // make the observable return a random value for each test
    const sampleData = getSampleData();
    addRolesToSampleData(sampleData);
    subjectLRM.next(sampleData);
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
    expect(tr.length).toBe(subjectLRM.getValue()._embedded.role.length, 'rows count does not match the total elements');
  });

  it('should call service for getting new permissions list when #loadList is called', () => {
    const getPermissionsSpy = spyOn(spy, 'getRoles');

    const toPage = 3;
    component.loadList(toPage);

    expect(getPermissionsSpy).toHaveBeenCalledTimes(1);
    expect(getPermissionsSpy).toHaveBeenCalledWith(component.page.size, toPage - 1);
  });
});
