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
@NoArgsConstructor(force = true)
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
    private final String name;

    @NotNull
    @NotBlank
    @Column(unique = true, nullable = false)
    private final String label;

    @ManyToMany(mappedBy = "permissions")
    Set<BRole> roles = new HashSet<>();
}
