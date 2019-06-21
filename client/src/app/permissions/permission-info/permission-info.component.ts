import { Location } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { Permission } from '../shared/permission.model';
import { PermissionService } from '../shared/permission.service';

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
  permissionSub = new Subscription;

  /**
   * Component constructor
   * @param route {ActivatedRoute} ActivatedRoute for getting url params.
   * @param permissionService {PermissionService} Service for requesting permissions information.
   * @param location {Location} Location object for triggering the platform navigation.
   */
  constructor(private route: ActivatedRoute, private permissionService: PermissionService, private location: Location) { }

  /**
   * Lifecycle hook that is called after data-bound properties are initialized.
   */
  ngOnInit() {
    this.getPermissionInfo();
  }

  /**
   * Gets the permission's info taking as argument the :id present in the url.
   */
  private getPermissionInfo(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id !== null && !isNaN(+id)) {
      this.permissionSub = this.permissionService.getPermissionInfo(+id).subscribe(info => this.permission = info);
    } else {
      this.location.back();
    }
  }

  /**
   * Lifecycle hook that is called when the component is destroyed.
   */
  ngOnDestroy() {
    this.permissionSub.unsubscribe();
  }

  /**
   * Navigates back in the platform's history.
   */
  back(): void {
    this.location.back();
  }
}
