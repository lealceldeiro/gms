import { PermissionsRoutingModule } from './permissions-routing.module';

describe('PermissionRoutingModule', () => {
  let permissionRoutingModule: PermissionsRoutingModule;

  beforeEach(() => {
    permissionRoutingModule = new PermissionsRoutingModule();
  });

  it('should create an instance', () => {
    expect(permissionRoutingModule).toBeTruthy();
  });
});
