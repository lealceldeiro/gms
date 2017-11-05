package com.gmsboilerplatesbng.domain.security.user;

import com.gmsboilerplatesbng.domain.GmsEntity;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true, of = "id")
@Entity
public class EUser extends GmsEntity{

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotBlank
    private final String username;

    @NotNull
    @NotBlank
    private final String email;

    @NotNull
    @NotBlank
    private final String name;

    @NotNull
    @NotBlank
    private final String lastName;

    @NotNull
    @NotBlank
    private final String password;

    private Boolean enabled = true;

    private Boolean emailVerified = true;

    private Boolean accountExpired = false;

    private Boolean accountLocked = false;

    private Boolean passwordExpired = false;

}
