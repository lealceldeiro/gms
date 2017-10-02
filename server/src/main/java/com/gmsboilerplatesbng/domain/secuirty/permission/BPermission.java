package com.gmsboilerplatesbng.domain.secuirty.permission;

import com.gmsboilerplatesbng.domain.GmsEntity;
import com.gmsboilerplatesbng.domain.secuirty.role.BRole;
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
public class BPermission extends GmsEntity {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NaturalId
    @NotBlank
    @Column(unique = true)
    private String name;    //name to use for authenticating

    @NotNull
    @NotBlank
    @Column(unique = true)
    private String label;   //label to show to final user

    @ManyToMany(mappedBy = "permissions")
    Set<BRole> roles = new HashSet<>();
}
