import { GmsLoginRoutingModule } from './gms-login-routing.module';

describe('GmsLoginRoutingModule', () => {
  let loginRoutingModule: GmsLoginRoutingModule;

  beforeEach(() => {
    loginRoutingModule = new GmsLoginRoutingModule();
  });

  it('should create an instance', () => {
    expect(loginRoutingModule).toBeTruthy();
  });
});
