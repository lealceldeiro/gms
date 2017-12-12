package com.gmsboilerplatesbng.domain.security.ownedEntity;

import com.gmsboilerplatesbng.domain.GmsEntity;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * EOwnedEntity
 *
 * @author Asiel Leal Celdeiro <lealceldeiro@gmail.com>
 *
 * @version 0.1
 * Dec 12, 2017
 */
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class EOwnedEntity extends GmsEntity{

    @NotNull
    @NotBlank
    private final String name;

    @NotNull
    @NotBlank
    @Column(unique = true, nullable = false)
    private final String username;

    @NotNull
    @NotBlank
    @Column(length = 10000)
    private final String description;

}
