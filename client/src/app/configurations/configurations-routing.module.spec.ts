import { ConfigurationsRoutingModule } from './configurations-routing.module';

describe('ConfigurationsRoutingModule', () => {
  let configurationsRoutingModule: ConfigurationsRoutingModule;

  beforeEach(() => {
    configurationsRoutingModule = new ConfigurationsRoutingModule();
  });

  it('should create an instance', () => {
    expect(configurationsRoutingModule).toBeTruthy();
  });
});
