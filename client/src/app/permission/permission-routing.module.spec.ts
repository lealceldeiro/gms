import { PermissionRoutingModule } from './permission-routing.module';

describe('PermissionRoutingModule', () => {
  let permissionRoutingModule: PermissionRoutingModule;

  beforeEach(() => {
    permissionRoutingModule = new PermissionRoutingModule();
  });

  it('should create an instance', () => {
    expect(permissionRoutingModule).toBeTruthy();
  });
});
