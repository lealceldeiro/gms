package com.gms.domain.security.permission;

import com.gms.domain.GmsEntity;
import com.gms.domain.security.role.BRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * BPermission
 *
 * @author Asiel Leal Celdeiro <lealceldeiro@gmail.com>
 *
 * @version 0.1
 * Dec 12, 2017
 */
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "roles")
@Entity
public class BPermission extends GmsEntity {

    /**
     * Name to be used for authenticating the user
     */
    @NotNull
    @NotBlank
    @Column(unique = true, nullable = false)
    private final String name;

    /**
     * Label to be shown to the final user
     */
    @NotNull
    @NotBlank
    @Column(unique = true, nullable = false)
    private final String label;

    @ManyToMany(mappedBy = "permissions")
    private Set<BRole> roles = new HashSet<>();
}
