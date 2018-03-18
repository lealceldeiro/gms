package com.gms.domain.security.permission;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public class BPermissionMeta {

    private BPermissionMeta() {}

    public static final String name = "Name to be used for allowing (or denying) performing operation over some resource(s).";
    public static final String label = "Label to be shown to the final user";
    public static final String rolesLink = "Link for getting the roles where this permissions is associated";
    public static final String id = "Permission's identifier";
}
