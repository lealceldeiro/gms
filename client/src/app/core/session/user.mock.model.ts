/**
 * Domain object which represents a user.
 */
import { User } from './user.model';

export const userMock = new User();
userMock.username = 'name';
userMock.email = 'email';
userMock.name = 'name';
userMock.lastName = 'lastName';
userMock.password = 'password';
userMock.enabled = false;
userMock.emailVerified = false;
userMock.accountNonExpired = true;
userMock.accountNonLocked = true;
userMock.credentialsNonExpired = true;
