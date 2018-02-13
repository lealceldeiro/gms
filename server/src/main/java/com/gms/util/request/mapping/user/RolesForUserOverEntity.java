package com.gms.util.request.mapping.user;

import com.gms.util.i18n.CodeI18N;
import lombok.Data;

import javax.validation.constraints.NotNull;
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
    @NotNull(message = CodeI18N.FIELD_NOT_NULL)
    List<Long> rolesId;
}
