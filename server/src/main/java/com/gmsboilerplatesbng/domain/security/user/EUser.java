package com.gmsboilerplatesbng.domain.security.user;

import com.gmsboilerplatesbng.domain.GmsEntity;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Set;

@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class EUser extends GmsEntity implements UserDetails {

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

    @Getter(AccessLevel.NONE)
    private Boolean enabled = true;

    private Boolean emailVerified = true;

    @Getter(AccessLevel.NONE)
    private Boolean accountExpired = false;

    @Getter(AccessLevel.NONE)
    private Boolean accountLocked = false;

    @Getter(AccessLevel.NONE)
    private Boolean passwordExpired = false;

    @Getter(AccessLevel.NONE)
    private Set<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.passwordExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
