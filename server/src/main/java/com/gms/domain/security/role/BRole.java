package com.gms.domain.security.role;

import com.gms.domain.GmsEntity;
import com.gms.domain.security.permission.BPermission;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.gms.util.constant.PersistenceConstant.STRING_LENGTH_DEFAULT;
import static com.gms.util.constant.PersistenceConstant.STRING_LENGTH_MAX;
import static com.gms.util.constant.SecurityConstant.USERNAME_REGEXP;
import static com.gms.util.i18n.CodeI18N.FIELD_NOT_BLANK;
import static com.gms.util.i18n.CodeI18N.FIELD_NOT_NULL;
import static com.gms.util.i18n.CodeI18N.FIELD_PATTERN_INCORRECT_USERNAME;
import static com.gms.util.i18n.CodeI18N.FIELD_SIZE;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "permissions")
@ToString(of = {"label"})
@Entity
public final class BRole extends GmsEntity {

    /**
     * Version number for a Serializable class.
     */
    private static final long serialVersionUID = -4748551801572430779L;

    /**
     * Label to which the role can be referred to.
     */
    @NotNull(message = FIELD_NOT_NULL)
    @NotBlank(message = FIELD_NOT_BLANK)
    @Size(max = STRING_LENGTH_DEFAULT, message = FIELD_SIZE)
    @Pattern(regexp = USERNAME_REGEXP, message = FIELD_PATTERN_INCORRECT_USERNAME)
    @Column(unique = true, nullable = false)
    @Setter(AccessLevel.NONE)
    private String label;

    /**
     * A description of what is this role for.
     */
    @Size(max = STRING_LENGTH_MAX, message = FIELD_SIZE)
    @Column(length = STRING_LENGTH_MAX)
    private String description;

    /**
     * Whether the role is enabled or not. If a role associated to a user is not enabled, the user will no be granted
     * the associated permissions to this role.
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
     * Creates a new {@link BRole} with a label given by parameter.
     *
     * @param label Label to assign to the new role.
     */
    public BRole(final String label) {
        this.label = label;
    }

    /**
     * Adds a permission p to a role.
     *
     * @param p Permission to be added.
     */
    public void addPermission(final BPermission... p) {
        if (permissions == null) {
            permissions = new HashSet<>();
        }
        permissions.addAll(Arrays.asList(p));
    }

    /**
     * Removes a permission from a role.
     *
     * @param p Permission to be removed.
     */
    public void removePermission(final BPermission... p) {
        if (permissions != null) {
            for (BPermission iP : p) {
                permissions.remove(iP);
            }
        }
    }

    /**
     * Removes a permission from a role.
     */
    public void removeAllPermissions() {
        if (permissions != null) {
            permissions.clear();
        }
    }

}
