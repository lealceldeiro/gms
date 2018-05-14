/**
 * Exposes the information provided by the "page" object in paginated data response.
 */
export interface PageModel {

  /**
   * Total elements to be shown per page.
   */
  size: number;

  /**
   * Total possible elements which can be shown.
   */
  totalElements: number;

  /**
   * Total pages according to the size of the page and the total elements.
   */
  totalPages: number;

  /**
   * Current page number.
   */
  number: number;
}
