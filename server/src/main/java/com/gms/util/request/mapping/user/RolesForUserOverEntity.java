package com.gms.util.request.mapping.user;

import lombok.Data;

import java.util.List;

/**
 * MessageResolver
 * Mapper class for defining roles for user over an owned entity. Thi can be used, for instance, for specifying roles
 * to add/remove to an user over an specific entity.
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 *
 * @version 0.1
 * Dec 12, 2017
 */
@Data
public class RolesForUserOverEntity {
    Long userId;
    Long entityId;
    List<Long> rolesId;
}
