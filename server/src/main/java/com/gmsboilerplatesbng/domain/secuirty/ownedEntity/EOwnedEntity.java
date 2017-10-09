package com.gmsboilerplatesbng.domain.secuirty.ownedEntity;

import com.gmsboilerplatesbng.domain.GmsEntity;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true, of = "id")
@Entity
public class EOwnedEntity extends GmsEntity{

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotBlank
    private final String name;

    @NotNull
    @NotBlank
    @Column(unique = true, nullable = false)
    private final String username;

    @Lob
    @NotNull
    @NotBlank
    private final String description;

}
