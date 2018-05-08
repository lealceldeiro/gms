import { GmsLoginModule } from './gms-login.module';

describe('GmsLoginModule', () => {
  let loginModule: GmsLoginModule;

  beforeEach(() => {
    loginModule = new GmsLoginModule();
  });

  it('should create an instance', () => {
    expect(loginModule).toBeTruthy();
  });
});
