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
    private boolean enabled = false;

    private boolean emailVerified = false;

    @Getter(AccessLevel.NONE)
    private boolean accountExpired = false;

    @Getter(AccessLevel.NONE)
    private boolean accountLocked = false;

    @Getter(AccessLevel.NONE)
    private boolean passwordExpired = false;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @Getter(AccessLevel.NONE)
    private HashSet<GrantedAuthority> authorities;

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
