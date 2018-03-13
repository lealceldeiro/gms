package com.gms.domain.security.permission;

import com.gms.domain.GmsEntity;
import com.gms.domain.security.role.BRole;
import com.gms.util.i18n.CodeI18N;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

import static com.gms.util.constant.SecurityConst.USERNAME_REGEXP;

/**
 * BPermission
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 *
 * @version 0.1
 * Dec 12, 2017
 */
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "roles")
@ToString(callSuper = true, exclude = "roles")
@Entity
public class BPermission extends GmsEntity {

    /**
     * Name to be used for authenticating the user.
     */
    @NotNull(message = CodeI18N.FIELD_NOT_NULL)
    @NotBlank(message = CodeI18N.FIELD_NOT_BLANK)
    @Size(max = 255, message = CodeI18N.FIELD_SIZE)
    @Pattern(regexp = USERNAME_REGEXP, message = CodeI18N.FIELD_PATTERN_INCORRECT_USERNAME)
    @Column(unique = true, nullable = false, length = 255)
    private final String name;

    /**
     * Label to be shown to the final user.
     */
    @NotNull(message = CodeI18N.FIELD_NOT_NULL)
    @NotBlank(message = CodeI18N.FIELD_NOT_BLANK)
    @Size(max = 255, message = CodeI18N.FIELD_SIZE)
    @Column(unique = true, nullable = false, length = 255)
    private final String label;

    /**
     * Roles in which the permission is being used.
     */
    @ManyToMany(mappedBy = "permissions")
    private Set<BRole> roles;
}
