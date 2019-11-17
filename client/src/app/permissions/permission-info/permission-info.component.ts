import { Location } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { Subscription } from 'rxjs';

import { Permission } from '../shared/permission.model';
import { PermissionService } from '../shared/permission.service';
import { Role } from '../../roles/shared/role.model';
import { RolePd } from '../../roles/shared/role.pd';

/**
 * A component for showing a permission's info.
 */
@Component({
  selector: 'gms-permission-info',
  templateUrl: './permission-info.component.html',
  styleUrls: ['./permission-info.component.scss']
})
export class PermissionInfoComponent implements OnInit, OnDestroy {
  /**
   * Object holding the permission's info.
   */
  permission: Permission = {} as Permission;

  /**
   * Subscription to the observable returning the permission's info.
   */
  permissionSub = new Subscription();

  /**
   * Array of roles where this permissions is being used.
   */
  roles: Array<Role> = [];

  /**
   * Indicated whether the roles are loaded or not already.
   */
  rolesLoaded = false;

  /**
   * Contains all API pagination information regarding to the permission's roles.
   */
  page: { total: number, size: number, current: number } = {
    total: 0,
    size: 6,
    current: 1
  };

  /**
   * Message to be shown in case there is not any role using this permission.
   */
  noRoleMessage = 'This permission is not being used by any role';

  /**
   * Component constructor
   * @param route ActivatedRoute for getting url params.
   * @param permissionService Service for requesting permissions information.
   * @param location Location object for triggering the platform navigation.
   */
  constructor(private route: ActivatedRoute, private permissionService: PermissionService, private location: Location) { }

  /**
   * Lifecycle hook that is called after data-bound properties are initialized.
   */
  ngOnInit() {
    this.getPermissionInfo();
  }

  /**
   * Lifecycle hook that is called when the component is destroyed.
   */
  ngOnDestroy() {
    this.permissionSub.unsubscribe();
  }

  /**
   * Gets the permission's info taking as argument the :id present in the url.
   */
  private getPermissionInfo(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id !== null && !isNaN(+id)) {
      this.permissionSub = this.permissionService.getPermissionInfo(+id).subscribe(info => {
        this.permission = info;
      });
    } else {
      this.location.back();
    }
  }

  /**
   * Loads the roles where this permissions is being used
   * @param toPage number of page to which the list will move. `toPage` starts from 1 (first page).
   */
  showInRoles(toPage: number = this.page.current): void {
    // at this point the id is not null, otherwise a location.back would have been performed in #getPermissionInfo
    const id = +(this.route.snapshot.paramMap.get('id') || 0);
    this.permissionService.getPermissionRoles(id, this.page.size, toPage).subscribe((rolePd: RolePd) => {
      this.roles = rolePd._embedded ? rolePd._embedded.role : [];
      this.page.current = toPage;
      this.page.total = rolePd.page.totalElements;
      this.rolesLoaded = true;
    });
  }

  /**
   * Navigates back in the platform's history.
   */
  back(): void {
    this.location.back();
  }
}
