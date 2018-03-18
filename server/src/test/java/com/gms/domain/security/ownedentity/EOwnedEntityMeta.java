package com.gms.domain.security.ownedentity;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public class EOwnedEntityMeta {

    private EOwnedEntityMeta() {}

    public static final String id = "Owned entity identifier";
    public static final String name = "Natural name which is used commonly for referring to the entity";
    public static final String username = "A unique string representation of the name. Useful when there are other entities with the same name";
    public static final String description = "A brief description of the entity";
}