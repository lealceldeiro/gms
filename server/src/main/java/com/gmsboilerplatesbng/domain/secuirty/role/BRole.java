package com.gmsboilerplatesbng.domain.secuirty.role;

import com.gmsboilerplatesbng.domain.GmsEntity;
import com.gmsboilerplatesbng.domain.secuirty.permission.BPermission;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class BRole extends GmsEntity{

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NaturalId
    @NotBlank
    @Column(unique = true)
    private String label;

    @Lob
    private String description;

    private Boolean enabled = false;

    @ManyToMany
    @JoinTable(
            name = "brole_bpermission",
            joinColumns = @JoinColumn(name = "brole_id"),
            inverseJoinColumns = @JoinColumn(name = "bpermission_id")
    )
    private Set<BPermission> permissions = new HashSet<>();

    /**
     * Adds a permission p to a role.
     * @param p Permission to be added.
     */
    public void addPermission(BPermission p) {
        permissions.add(p);
        p.getRoles().add(this);
    }

    /**
     * Removes a permission from a role.
     * @param p Permission to be removed.
     */
    public void removePermission(BPermission p) {
        permissions.remove(p);
        p.getRoles().remove(this);
    }
}
