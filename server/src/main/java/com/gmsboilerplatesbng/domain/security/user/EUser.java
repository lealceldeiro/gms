package com.gmsboilerplatesbng.domain.security.user;

import com.gmsboilerplatesbng.domain.GmsEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class EUser extends GmsEntity{

    @NotNull
    @NotBlank
    @Column(unique = true)
    private final String username;

    @NotNull
    @NotBlank
    @Column(unique = true)
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
