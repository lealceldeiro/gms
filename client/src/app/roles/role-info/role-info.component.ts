import { Location } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';

import { Permission } from '../../permissions/shared/permission.model';
import { PermissionPd } from '../../permissions/shared/permission.pd';
import { Role } from '../shared/role.model';
import { RolesService } from '../shared/roles.service';

/**
 * A component for showing a role's info.
 */
@Component({
  selector: 'gms-role-info',
  templateUrl: './role-info.component.html',
  styleUrls: ['./role-info.component.scss']
})
export class RoleInfoComponent implements OnInit, OnDestroy {
  /**
   * Object holding the role's info.
   */
  role: Role = {} as Role;

  /**
   * Subscription to the observable returning the role's info.
   */
  roleSub = new Subscription();

  /**
   * Array of permissions assigned to this role.
   */
  permissions: Array<Permission> = [];

  /**
   * Subscription to the observable returning the permissions array.
   */
  permissionsSub = new Subscription();

  /**
   * Indicated whether the permissions are loaded or not already.
   */
  permissionsLoaded = false;

  /**
   * Contains all API pagination information regarding to the role's permissions.
   */
  page: { total: number, size: number, current: number } = {
    total: 0,
    size: 6,
    current: 1
  };

  /**
   * Message to be shown in case there is not any permission associated to this role.
   */
  noPermissionsMessage = 'No permissions assigned to this role';

  /**
   * Component constructor
   * @param route ActivatedRoute for getting url params.
   * @param rolesService Service for requesting role information.
   * @param location Location object for triggering the platform navigation.
   */
  constructor(private route: ActivatedRoute, private rolesService: RolesService, private location: Location) { }

  /**
   * Lifecycle hook that is called after data-bound properties are initialized.
   */
  ngOnInit() {
    this.getRoleInfo();
  }

  /**
   * Lifecycle hook that is called when the component is destroyed.
   */
  ngOnDestroy() {
    this.roleSub.unsubscribe();
  }

  /**
   * Gets the role's info taking as argument the :id present in the url.
   */
  private getRoleInfo(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id !== null && !isNaN(+id)) {
      this.roleSub = this.rolesService.getRoleInfo(+id).subscribe(info => {
        this.role = info;
      });
    } else {
      this.location.back();
    }
  }

  /**
   * Loads the permissions assigned to this role.
   * @param toPage number of page to which the list will move. `toPage` starts from 1 (first page).
   */
  showInPermissions(toPage: number = this.page.current): void {
    // at this point the id is not null, otherwise a location.back would have been performed in #getPermissionInfo
    const id = +(this.route.snapshot.paramMap.get('id') || 0);
    this.permissionsSub = this.rolesService.getRolePermissions(id, this.page.size, toPage).subscribe((permissionPd: PermissionPd) => {
      this.permissions = permissionPd._embedded ? permissionPd._embedded.permission : [];
      this.page.current = toPage;
      this.page.total = permissionPd.page.totalElements;
      this.permissionsLoaded = true;
    });
  }
}
