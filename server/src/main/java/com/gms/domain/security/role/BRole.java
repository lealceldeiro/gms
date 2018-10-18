package com.gms.domain.security.role;

import com.gms.domain.GmsEntity;
import com.gms.domain.security.permission.BPermission;
import com.gms.util.i18n.CodeI18N;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.gms.util.constant.SecurityConst.USERNAME_REGEXP;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "permissions")
@ToString(of = {"label"})
@Entity
public final class BRole extends GmsEntity{

    /**
     * Label to which the role can be referred to.
     */
    @NotNull(message = CodeI18N.FIELD_NOT_NULL)
    @NotBlank(message = CodeI18N.FIELD_NOT_BLANK)
    @Size(max = 255, message = CodeI18N.FIELD_SIZE)
    @Pattern(regexp = USERNAME_REGEXP, message = CodeI18N.FIELD_PATTERN_INCORRECT_USERNAME)
    @Column(unique = true, nullable = false, length = 255)
    private final String label;

    /**
     * A description of what is this role for.
     */
    @Size(max = 10485760, message = CodeI18N.FIELD_SIZE)
    @Column(length = 10485760)
    private String description;

    /**
     * Whether the role is enabled or not. If a role associated to a user is not enabled, the user will no be granted the
     * associated permissions to this role.
     */
    private Boolean enabled = false;

    /**
     * Permissions associated to this role.
     */
    @ManyToMany
    @JoinTable(
            name = "brole_bpermission",
            joinColumns = @JoinColumn(name = "brole_id"),
            inverseJoinColumns = @JoinColumn(name = "bpermission_id")
    )
    private Set<BPermission> permissions;

    /**
     * Adds a permission p to a role.
     * @param p Permission to be added.
     */
    public void addPermission(BPermission... p) {
        if (permissions == null) permissions = new HashSet<>();
        permissions.addAll(Arrays.asList(p));
    }

    /**
     * Removes a permission from a role.
     * @param p Permission to be removed.
     */
    public void removePermission(BPermission... p) {
        if (permissions != null) {
            for (BPermission iP : p) {
                permissions.remove(iP);
            }
        }
    }

    public void removeAllPermissions() {
        if (permissions != null) permissions.clear();
    }

}
