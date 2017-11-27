package com.gmsboilerplatesbng.util.constant;

public enum BPermissionConst {

    /*USER*/
    MANAGE__USER,
    CREATE__USER,
    READ__USER,
    READ_ALL__USER,
    READ_ASSOCIATED__USER, //read all users given the ids of several entities
    UPDATE__USER,
    DELETE__USER,

    /*ROLE*/
    MANAGE__ROLE,
    CREATE__ROLE,
    READ__ROLE,
    READ_ALL__ROLE,
    UPDATE__ROLE,
    DELETE__ROLE,

    /*PERMISSION*/
    MANAGE__PERMISSION,
    CREATE__PERMISSION,
    READ__PERMISSION,
    UPDATE__PERMISSION,
    DELETE__PERMISSION,

    /*OWNED_ENTITY*/
    MANAGE__OWNED_ENTITY,
    CREATE__OWNED_ENTITY,
    READ__OWNED_ENTITY,
    READ_ALL__OWNED_ENTITY,
    UPDATE__OWNED_ENTITY,
    DELETE__OWNED_ENTITY,

    /*PROFILE*/
    MANAGE__PROFILE,
    READ__PROFILE,
    UPDATE__PROFILE,

    MANAGE__CONFIGURATION

}
