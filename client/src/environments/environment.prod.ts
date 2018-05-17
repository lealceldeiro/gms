/**
 * Environment variable set for production mode
 *
 * @type {{production: boolean}}
 */
export const environment = {
  production: true
};

/**
 * All backend API related information.
 * @type {{url: string}}
 */
export const api = {
  baseUrl: 'http://127.0.0.1/gms/api/',
  loginUrl: 'login'
};

/**
 * All app meta information.
 * @type {{name: string}}
 */
export const appMeta = {
  name: 'General Management System'
};
