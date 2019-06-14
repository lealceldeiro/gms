import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { PermissionPd } from '../shared/permission-pd';
import { Permission } from '../shared/permission.model';
import { PermissionService } from '../shared/permission.service';

/**
 * A component for showing a list with all the permissions.
 */
@Component({
  selector: 'gms-permission-list',
  templateUrl: './permission-list.component.html',
  styleUrls: ['./permission-list.component.scss']
})
export class PermissionListComponent implements OnInit, OnDestroy {

  /**
   * List of permissions
   */
  permissionList: Array<Permission> = [];

  /**
   * List of permissions' subscription
   */
  private listSubscription = new Subscription();

  /**
   * Contains all API pagination information.
   * @type {{total: number; size: number; current: number}}
   */
  page: { total: number, size: number, current: number, totalPages: number } = {
    total: 0,
    size: 10,
    current: 1,
    totalPages: 0,
  };

  /**
   * Component constructor
   */
  constructor(private permissionService: PermissionService) { }

  /**
   * Lifecycle hook that is called after data-bound properties are initialized.
   */
  ngOnInit() {
    this.loadList(this.page.current);
  }

  /**
   * Lifecycle hook that is called when a service is destroyed.
   */
  ngOnDestroy() {
    this.listSubscription.unsubscribe();
  }

  /**
   * Loads the permission list
   * @param {number} toPage number of page to which the list will move. `toPage` starts from 1 (first page).
   */
  loadList(toPage: number): void {
      this.listSubscription = this.permissionService.getPermissions(this.page.size, toPage - 1).subscribe((permissionPd: PermissionPd) => {
        this.permissionList = permissionPd._embedded.permission;
        this.page.total = permissionPd.page.totalElements;
        this.page.totalPages = permissionPd.page.totalPages;
        this.page.current = permissionPd.page.number + 1;
      });
  }

}
