package com.gms.domain.security.user;

/**
 * UserMeta
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Feb 12, 2018
 */
public class EUserMeta {
    private EUserMeta() {}

    public static final String id = "User's identifier";
    public static final String username = "User's username";
    public static final String email = "User's email";
    public static final String name = "User's name";
    public static final String lastName = "User's last name";
    public static final String password = "User's password";
    public static final String passwordHashed = "Hashed user's password";
    public static final String enabled = "Whether the user is enabled or not.";
    public static final String emailVerified = "Whether the user has verified the associated email or not.";
    public static final String accountNonExpired = "Whether the user's account has expired or not.";
    public static final String accountNonLocked = "Whether the user's account has been locked or not.";
    public static final String credentialsNonExpired = "Whether the user's credentials has expired or not.";
}
