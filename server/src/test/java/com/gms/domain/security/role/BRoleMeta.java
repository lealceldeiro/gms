package com.gms.domain.security.role;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public class BRoleMeta {

    private BRoleMeta() {}

    public static final String id = "Role's identifier";
    public static final String label = "Label to which the role can be referred to";
    public static final String description = "A description of what is this role for";
    public static final String permissions = "Permissions which this role will be associated to. These must be links to the permission resource";
    public static final String enabled = "Whether the role is enabled or not." +
            " If a role associated to a user is not enabled, the user will no be granted the" +
            " associated permissions to this role.";
    public static final String permissionsLink = "Link to get the associated permissions to the role";
}
