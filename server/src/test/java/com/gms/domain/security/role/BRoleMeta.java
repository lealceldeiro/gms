package com.gms.domain.security.role;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public final class BRoleMeta {

    /**
     * Privates constructor to make class un-instantiable.
     */
    private BRoleMeta() {
    }

    /**
     * Meta documentation.
     */
    public static final String ID_INFO = "Role's identifier";
    /**
     * Meta documentation.
     */
    public static final String LABEL_INFO = "Label to which the role can be referred to";
    /**
     * Meta documentation.
     */
    public static final String DESCRIPTION_INFO = "A description of what is this role for";
    /**
     * Meta documentation.
     */
    public static final String PERMISSIONS_INFO = "Permissions which this role will be associated to. "
            + "These must be links to the permission resource";
    /**
     * Meta documentation.
     */
    public static final String ENABLED_INFO = "Whether the role is enabled or not."
            + " If a role associated to a user is not enabled, the user will no be granted the"
            + " associated permissions to this role.";
    /**
     * Meta documentation.
     */
    public static final String PERMISSION_LINK_INFO = "Link to get the associated permissions to the role";

}
