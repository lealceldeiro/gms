import { Component, OnDestroy, OnInit } from '@angular/core';
import { PermissionService } from '../shared/permission.service';
import { PermissionPd } from '../shared/permission-pd';
import { Permission } from '../shared/permission.model';
import { Subscription } from 'rxjs';

@Component({
  selector: 'gms-permission-list',
  templateUrl: './permission-list.component.html',
  styleUrls: ['./permission-list.component.css']
})
export class PermissionListComponent implements OnInit, OnDestroy {

  /**
   * List of permissions
   */
  permissionList: Array<Permission>;

  /**
   * List of permissions' subscription
   */
  listSubscription: Subscription;

  /**
   * Contains all API pagination information.
   * @type {{total: number; size: number; current: number}}
   */
  page: { total: number, size: number, current: number, totalPages } = {
    total: 0,
    totalPages: 0,
    size: 8,
    current: 0
  };

  /**
   * Contains all angular-bootstrap component pagination information
   * @type {{current: number; maxSize: number; rotate: boolean; ellipses: boolean; boundaryLinks: boolean}}
   */
  ubPagination: { current: number, maxSize: number, rotate: boolean, ellipses: boolean, boundaryLinks: boolean } = {
    current: 0,
    maxSize: 7,
    rotate: true,
    ellipses: false,
    boundaryLinks: true
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
   * @param {number} toPage number of page to which the list will move
   */
  loadList(toPage: number) {
    this.listSubscription = this.permissionService.getPermissions(this.page.size, toPage).subscribe((permissionPd: PermissionPd) => {
      this.permissionList = permissionPd._embedded.permission;
      this.page.total = permissionPd.page.totalElements;
      this.page.totalPages = permissionPd.page.totalPages;
      this.page.current = permissionPd.page.number;
    });
  }

}
