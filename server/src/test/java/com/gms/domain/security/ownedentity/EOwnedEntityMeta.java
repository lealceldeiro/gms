package com.gms.domain.security.ownedentity;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public final class EOwnedEntityMeta {

    /**
     * Privates constructor to make class un-instantiable.
     */
    private EOwnedEntityMeta() {
    }

    /**
     * Meta documentation.
     */
    public static final String ID_INFO = "Owned entity identifier";
    /**
     * Meta documentation.
     */
    public static final String NAME_INFO = "Natural name which is used commonly for referring to the entity";
    /**
     * Meta documentation.
     */
    public static final String USERNAME_INFO = "A unique string representation of the name. Useful when there are "
            + "other entities with the same name";
    /**
     * Meta documentation.
     */
    public static final String DESCRIPTION_INFO = "A brief description of the entity";

}
