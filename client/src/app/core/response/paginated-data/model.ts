import { LinksModel } from './links.model';
import { PageModel } from './page.model';

/**
 * Exposes the information provided by paginated data responses.
 */
export interface PaginatedDataModel {

  /**
   * Embedded array of objects.
   */
  _embedded: object;

  /**
   * Available links for requesting other webservices related to the previous request source.
   */
  _links: LinksModel;

  /**
   * Pagination options.
   */
  page: PageModel;

}
