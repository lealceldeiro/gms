import { PaginatedDataModel } from '../../core/response/paginated-data/model';
import { Permission } from './permission.model';

/**
 * Exposes the information provided by paginated data responses regarding to Permission.
 */
export interface PermissionPd extends PaginatedDataModel {

  /**
   * Embedded array of permissions.
   */
  _embedded: {
    /**
     * Array of permissions.
     */
    permission: Array<Permission>
  };

}
