package com.gmsboilerplatesbng.domain.secuirty.permission;

import com.gmsboilerplatesbng.domain.GmsEntity;
import com.gmsboilerplatesbng.domain.secuirty.role.BRole;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class BPermission extends GmsEntity {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NaturalId
    @NotBlank
    @Column(unique = true, nullable = false)
    private final String name;    //name to use for authenticating

    @NotNull
    @NotBlank
    @Column(unique = true, nullable = false)
    private final String label;   //label to show to final user

    @ManyToMany(mappedBy = "permissions")
    Set<BRole> roles = new HashSet<>();
}
