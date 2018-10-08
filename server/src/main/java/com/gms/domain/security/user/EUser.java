package com.gms.domain.security.user;

import com.gms.domain.GmsEntity;
import com.gms.util.i18n.CodeI18N;
import lombok.*;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.*;
import java.util.Collection;
import java.util.HashSet;

import static com.gms.util.constant.SecurityConst.USERNAME_REGEXP;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"authorities", "password"})
@ToString(callSuper = true, exclude = {"password", "authorities"})
@Entity
public class EUser extends GmsEntity implements UserDetails {

    @Size(max = 255, message = CodeI18N.FIELD_SIZE)
    @NotNull(message = CodeI18N.FIELD_NOT_NULL)
    @NotBlank(message = CodeI18N.FIELD_NOT_BLANK)
    @Pattern(regexp = USERNAME_REGEXP, message = CodeI18N.FIELD_PATTERN_INCORRECT_USERNAME)
    @Column(unique = true, nullable = false, length = 255)
    private final String username;

    /**
     * For emails, max length  as here:
     * http://www.rfc-editor.org/errata_search.php?rfc=3696&eid=1690
     * explained here:
     * https://stackoverflow.com/a/574698/5640649
     */
    @Size(max = 254, message = CodeI18N.FIELD_SIZE)
    @NotNull(message = CodeI18N.FIELD_NOT_NULL)
    @NotBlank(message = CodeI18N.FIELD_NOT_BLANK)
    @Email(message = CodeI18N.FIELD_NOT_WELL_FORMED)
    @Column(unique = true, nullable = false, length = 254)
    private final String email;

    @NotNull(message = CodeI18N.FIELD_NOT_NULL)
    @NotBlank(message = CodeI18N.FIELD_NOT_BLANK)
    @Size(max = 255, message = CodeI18N.FIELD_SIZE)
    @Column(nullable = false, length = 255)
    private final String name;

    @NotNull(message = CodeI18N.FIELD_NOT_NULL)
    @NotBlank(message = CodeI18N.FIELD_NOT_BLANK)
    @Size(max = 255, message = CodeI18N.FIELD_SIZE)
    @Column(nullable = false, length = 255)
    private final String lastName;

    @NotNull(message = CodeI18N.FIELD_NOT_NULL)
    @NotBlank(message = CodeI18N.FIELD_NOT_BLANK)
    @Size(max = 10485760, message = CodeI18N.FIELD_SIZE)
    // the bean can actually have a LOT of chars, in db it will be stored hashed (a LOT LESSER characters)
    @Column(nullable = false, length = 10485760)
    @RestResource(exported = false)
    private String password;

    @Getter(AccessLevel.NONE)
    private Boolean enabled;

    @Getter(AccessLevel.NONE)
    private Boolean emailVerified;

    @Getter(AccessLevel.NONE)
    private Boolean accountNonExpired;

    @Getter(AccessLevel.NONE)
    private Boolean accountNonLocked;

    @Getter(AccessLevel.NONE)
    private Boolean credentialsNonExpired;

    // user authorities are handled via jjwt, this attribute is kept for compatibility with Spring Security
    @SuppressWarnings("JpaAttributeTypeInspection")
    @Getter(AccessLevel.NONE)
    private HashSet<GrantedAuthority> authorities = null;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        // by default: true
        return accountNonExpired == null || accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        // by default: true
        return accountNonLocked == null || accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // by default: true
        return credentialsNonExpired == null || credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        // by default: false
        return enabled != null && enabled;
    }

    public boolean isEmailVerified() {
        // by default: false
        return emailVerified != null && emailVerified;
    }

    public EUser(String username, String email, String name, String lastName, String password) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.password = password;
    }

}
