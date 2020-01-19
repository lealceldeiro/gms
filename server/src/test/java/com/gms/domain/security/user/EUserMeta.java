package com.gms.domain.security.user;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public final class EUserMeta {

    /**
     * Privates constructor to make class un-instantiable.
     */
    private EUserMeta() {
    }

    /**
     * Meta documentation.
     */
    public static final String ID_INFO = "User's identifier";
    /**
     * Meta documentation.
     */
    public static final String USERNAME_INFO = "User's username";
    /**
     * Meta documentation.
     */
    public static final String EMAIL_INFO = "User's email";
    /**
     * Meta documentation.
     */
    public static final String NAME_INFO = "User's name";
    /**
     * Meta documentation.
     */
    public static final String LAST_NAME_INFO = "User's last name";
    /**
     * Meta documentation.
     */
    public static final String PASSWORD_INFO = "User's password";
    /**
     * Meta documentation.
     */
    public static final String PASSWORD_HASHED_INFO = "Hashed user's password";
    /**
     * Meta documentation.
     */
    public static final String ENABLED_INFO = "Whether the user is enabled or not.";
    /**
     * Meta documentation.
     */
    public static final String EMAIL_VERIFIED_INFO = "Whether the user has verified the associated email or not.";
    /**
     * Meta documentation.
     */
    public static final String ACCOUNT_NON_EXPIRED_INFO = "Whether the user's account has expired or not.";
    /**
     * Meta documentation.
     */
    public static final String ACCOUNT_NON_LOCKED_INFO = "Whether the user's account has been locked or not.";
    /**
     * Meta documentation.
     */
    public static final String CREDENTIALS_NON_EXPIRED_INFO = "Whether the user's credentials has expired or not.";

}
