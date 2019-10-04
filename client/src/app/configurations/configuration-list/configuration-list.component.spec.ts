import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { getRandomNumber } from '../../shared/test-util/functions.util';
import { ConfigurationService } from '../shared/configuration.service';
import { ConfigurationListComponent } from './configuration-list.component';

describe('ConfigurationListComponent', () => {
  let component: ConfigurationListComponent;
  let fixture: ComponentFixture<ConfigurationListComponent>;
  const configurationObject: { [key: string]: string } = {};
  const numOfProps = getRandomNumber();

  for (let i = 0; i < numOfProps; i++) {
    configurationObject['randomKey-' + getRandomNumber() + '-' + i] = 'randomValue-' + getRandomNumber() + '-' + i;
  }

  let configurationServiceSpy: jasmine.SpyObj<ConfigurationService>;
  let loadConfigurationsSpy: jasmine.Spy;
  let loadUserConfigurationsSpy: jasmine.Spy;

  beforeEach(async(() => {
    configurationServiceSpy = jasmine.createSpyObj('ConfigurationService', ['getConfigurations', 'getUserConfigurations']);
    configurationServiceSpy.getConfigurations.and.returnValue(of<object>(configurationObject));
    configurationServiceSpy.getUserConfigurations.and.returnValue(of<object>(configurationObject));

    TestBed.configureTestingModule({
      declarations: [ConfigurationListComponent],
      providers: [{ provide: ConfigurationService, useValue: configurationServiceSpy }]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfigurationListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    loadConfigurationsSpy = spyOn(component, 'loadConfigurations');
    loadUserConfigurationsSpy = spyOn(component, 'loadUserConfigurations');
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call loadConfigurations on init', () => {
    component.ngOnInit();
    expect(loadConfigurationsSpy).toHaveBeenCalledTimes(1);
  });

  it('should call ConfigurationService#getConfigurations on call to loadConfigurations and set values from the observable ', () => {
    component.loadConfigurations();
    expect(loadConfigurationsSpy).toHaveBeenCalledTimes(1);
    expect(configurationServiceSpy.getConfigurations).toHaveBeenCalledTimes(1);
    expect(component.keys.system).toEqual(Object.keys(configurationObject));
    expect(component.values.system).toEqual(Object.values(configurationObject));
  });

  it('should call ConfigurationService#getUserConfigurations on call to loadUserConfigurations and set values from the observable ', () => {
    component.loadUserConfigurations();
    expect(loadUserConfigurationsSpy).toHaveBeenCalledTimes(1);
    expect(configurationServiceSpy.getUserConfigurations).toHaveBeenCalledTimes(1);
    expect(component.keys.user).toEqual(Object.keys(configurationObject));
    expect(component.values.user).toEqual(Object.values(configurationObject));
  });

});
