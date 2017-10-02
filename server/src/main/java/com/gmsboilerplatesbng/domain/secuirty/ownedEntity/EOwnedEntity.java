package com.gmsboilerplatesbng.domain.secuirty.ownedEntity;

import com.gmsboilerplatesbng.domain.GmsEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class EOwnedEntity extends GmsEntity{

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NaturalId
    @NotBlank
    @Column(unique = true)
    private String username;

    @Lob
    @NotNull
    @NotBlank
    private String description;

}
