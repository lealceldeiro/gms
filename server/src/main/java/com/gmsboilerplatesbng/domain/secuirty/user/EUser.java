package com.gmsboilerplatesbng.domain.secuirty.user;

import com.gmsboilerplatesbng.domain.GmsEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class EUser extends GmsEntity{

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NaturalId
    @NotBlank
    private String username;

    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String lastName;

    @NotNull
    @NotBlank
    private String password;

    private Boolean enabled = true;

    private Boolean emailVerified = true;

    private Boolean accountExpired = false;

    private Boolean accountLocked = false;

    private Boolean passwordExpired = false;

}
