package com.gms.domain.configuration;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public class BConfigurationMeta {

    private BConfigurationMeta() {}

    public static final String key = "Key under the configuration is being saved.";
    public static final String value = "A string representation of the actual value of the configuration that is being " +
            "saved under the ley.";
    public static final String userId = "User's identifier. Some configurations are user-specific. In those cases the " +
            "identifier of the user is saved too.";
}
