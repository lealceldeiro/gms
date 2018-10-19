import { PermissionsModule } from './permissions.module';

describe('PermissionModule', () => {
  let permissionModule: PermissionsModule;

  beforeEach(() => {
    permissionModule = new PermissionsModule();
  });

  it('should create an instance', () => {
    expect(permissionModule).toBeTruthy();
  });
});
