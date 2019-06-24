import { Component, OnInit } from '@angular/core';
import { Role } from '../shared/role.model';
import { Subscription } from 'rxjs';
import { RolesService } from '../shared/roles.service';
import { RolePd } from '../shared/role.pd';

/**
 * A component for showing a list with all the roles.
 */
@Component({
  selector: 'gms-role-list',
  templateUrl: './role-list.component.html',
  styleUrls: ['./role-list.component.scss']
})
export class RoleListComponent implements OnInit {

  /**
   * List of roles
   */
  roleList: Array<Role> = [];

  /**
   * List of roles' subscription
   */
  private listSubscription = new Subscription();

  /**
   * Contains all API pagination information.
   */
  page: { total: number, size: number, current: number, totalPages: number } = {
    total: 0,
    size: 10,
    current: 1,
    totalPages: 0,
  };

  /**
   * Component constructor
   * @param roleService RolesService for making request to backend.
   */
  constructor(private roleService: RolesService) { }

  /**
   * Lifecycle hook that is called after data-bound properties are initialized.
   */
  ngOnInit() {
    this.loadList(this.page.current);
  }

  /**
   * Loads the role list
   * @param {number} toPage number of page to which the list will move. `toPage` starts from 1 (first page).
   */
  loadList(toPage: number): void {
    this.listSubscription = this.roleService.getRoles(this.page.size, toPage - 1).subscribe((rolePd: RolePd) => {
      this.roleList = rolePd._embedded.role;
      this.page.total = rolePd.page.totalElements;
      this.page.totalPages = rolePd.page.totalPages;
      this.page.current = rolePd.page.number + 1;
    });
  }

}
