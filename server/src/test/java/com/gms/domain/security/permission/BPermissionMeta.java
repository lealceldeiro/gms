package com.gms.domain.security.permission;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public final class BPermissionMeta {

    /**
     * Privates constructor to make class un-instantiable.
     */
    private BPermissionMeta() {
    }

    /**
     * Meta documentation.
     */
    public static final String NAME_INFO = "Name to be used for allowing (or denying) performing operation over some"
            + " resource(s).";
    /**
     * Meta documentation.
     */
    public static final String LABEL_INFO = "Label to be shown to the final user";
    /**
     * Meta documentation.
     */
    public static final String ROLES_LINK_INFO = "Link for getting the roles where this permissions is associated";
    /**
     * Meta documentation.
     */
    public static final String ID_INFO = "Permission's identifier";

}
