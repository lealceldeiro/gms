import { PaginatedDataModel } from '../../core/response/paginated-data/model';
import { Role } from './role.model';

/**
 * Exposes the information provided by paginated data responses regarding to Role.
 */
export interface RolePd extends PaginatedDataModel {

  /**
   * Embedded array of roles.
   */
  _embedded: {
    /**
     * Array of roles.
     */
    role: Array<Role>
  };

}
