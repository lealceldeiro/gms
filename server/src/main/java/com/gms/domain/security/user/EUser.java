package com.gms.domain.security.user;

import com.gms.domain.GmsEntity;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;

/**
 * EUser
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 *
 * @version 0.1
 * Dec 12, 2017
 */
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"authorities", "password"})
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
    private Boolean enabled;

    @Getter(AccessLevel.NONE)
    private Boolean emailVerified;

    @Getter(AccessLevel.NONE)
    private Boolean accountExpired;

    @Getter(AccessLevel.NONE)
    private Boolean accountLocked;

    @Getter(AccessLevel.NONE)
    private Boolean passwordExpired;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @Getter(AccessLevel.NONE)
    private HashSet<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        // by default: true
        return this.accountExpired == null || !this.accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        // by default: true
        return this.accountLocked == null || !this.accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // by default: true
        return this.passwordExpired == null || !this.passwordExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled != null && this.enabled;
    }

    public boolean isEmailVerified() {
        return this.emailVerified != null && this.emailVerified;
    }
}
