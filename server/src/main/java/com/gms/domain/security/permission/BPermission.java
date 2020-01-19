package com.gms.domain.security.permission;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gms.domain.GmsEntity;
import com.gms.domain.security.role.BRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

import static com.gms.util.constant.PersistenceConstant.STRING_LENGTH_DEFAULT;
import static com.gms.util.constant.SecurityConst.USERNAME_REGEXP;
import static com.gms.util.i18n.CodeI18N.FIELD_NOT_BLANK;
import static com.gms.util.i18n.CodeI18N.FIELD_NOT_NULL;
import static com.gms.util.i18n.CodeI18N.FIELD_PATTERN_INCORRECT_USERNAME;
import static com.gms.util.i18n.CodeI18N.FIELD_SIZE;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "roles")
@ToString(exclude = "roles")
@Entity
public class BPermission extends GmsEntity {

    /**
     * Version number for a Serializable class.
     */
    private static final long serialVersionUID = -1246329856397933423L;

    /**
     * Name to be used for authenticating the user.
     */
    @NotNull(message = FIELD_NOT_NULL)
    @NotBlank(message = FIELD_NOT_BLANK)
    @Size(max = STRING_LENGTH_DEFAULT, message = FIELD_SIZE)
    @Pattern(regexp = USERNAME_REGEXP, message = FIELD_PATTERN_INCORRECT_USERNAME)
    @Column(unique = true, nullable = false)
    private final String name;

    /**
     * Label to be shown to the final user.
     */
    @NotNull(message = FIELD_NOT_NULL)
    @NotBlank(message = FIELD_NOT_BLANK)
    @Size(max = STRING_LENGTH_DEFAULT, message = FIELD_SIZE)
    @Column(unique = true, nullable = false)
    private final String label;

    /**
     * Roles in which the permission is being used.
     */
    @ManyToMany(mappedBy = "permissions")
    @JsonIgnore
    private Set<BRole> roles;

}
