package com.gmsboilerplatesbng.mapping.user;

import lombok.Data;

import java.util.List;

/**
 * Mapper class for defining roles for user over an owned entity. Thi can be used, for instance, for specifying roles
 * to add/remove to an user over an specific entity.
 */

@Data
public class RolesForUserOverEntity {
    Long userId;
    Long entityId;
    List<Long> rolesId;
}
