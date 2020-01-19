package com.gms.domain.configuration;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public final class BConfigurationMeta {

    /**
     * Privates constructor to make class un-instantiable.
     */
    private BConfigurationMeta() {
    }

    /**
     * Meta documentation.
     */
    public static final String KEY_INFO = "Key under the configuration is being saved.";
    /**
     * Meta documentation.
     */
    public static final String VALUE_INFO = "A string representation of the actual value of the configuration that is "
            + "being saved under the ley.";
    /**
     * Meta documentation.
     */
    public static final String USER_ID_INFO = "User's identifier. Some configurations are user-specific. "
            + "In those cases the identifier of the user is saved too.";

}
