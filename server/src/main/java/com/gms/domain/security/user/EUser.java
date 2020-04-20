package com.gms.domain.security.user;

import com.gms.domain.GmsEntity;
import com.gms.util.i18n.CodeI18N;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import static com.gms.util.constant.PersistenceConstant.STRING_LENGTH_254;
import static com.gms.util.constant.PersistenceConstant.STRING_LENGTH_DEFAULT;
import static com.gms.util.constant.PersistenceConstant.STRING_LENGTH_PASSWORD;
import static com.gms.util.constant.SecurityConstant.USERNAME_REGEXP;
import static com.gms.util.i18n.CodeI18N.FIELD_NOT_BLANK;
import static com.gms.util.i18n.CodeI18N.FIELD_NOT_NULL;
import static com.gms.util.i18n.CodeI18N.FIELD_PATTERN_INCORRECT_USERNAME;
import static com.gms.util.i18n.CodeI18N.FIELD_SIZE;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"authorities", "password"})
@ToString(of = {"username", "email", "name", "lastName"})
@Entity
public class EUser extends GmsEntity implements UserDetails {

    /**
     * Version number for a Serializable class.
     */
    private static final long serialVersionUID = 2382305768933278544L;

    /**
     * User's username.
     */
    @Size(max = STRING_LENGTH_DEFAULT, message = FIELD_SIZE)
    @NotNull(message = FIELD_NOT_NULL)
    @NotBlank(message = FIELD_NOT_BLANK)
    @Pattern(regexp = USERNAME_REGEXP, message = FIELD_PATTERN_INCORRECT_USERNAME)
    @Column(unique = true, nullable = false)
    @Setter(AccessLevel.NONE)
    private String username;

    /**
     * For emails max length as described at http://www.rfc-editor.org/errata_search.php?rfc=3696&eid=1690 and explained
     * at https://stackoverflow.com/a/574698/5640649 .
     */
    @Size(max = STRING_LENGTH_254, message = FIELD_SIZE)
    @NotNull(message = FIELD_NOT_NULL)
    @NotBlank(message = FIELD_NOT_BLANK)
    @Email(message = CodeI18N.FIELD_NOT_WELL_FORMED)
    @Column(unique = true, nullable = false, length = STRING_LENGTH_254)
    @Setter(AccessLevel.NONE)
    private String email;

    /**
     * User's name.
     */
    @NotNull(message = FIELD_NOT_NULL)
    @NotBlank(message = FIELD_NOT_BLANK)
    @Size(max = STRING_LENGTH_DEFAULT, message = FIELD_SIZE)
    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private String name;

    /**
     * User's last name.
     */
    @NotNull(message = FIELD_NOT_NULL)
    @NotBlank(message = FIELD_NOT_BLANK)
    @Size(max = STRING_LENGTH_DEFAULT, message = FIELD_SIZE)
    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private String lastName;

    /**
     * User's password.
     */
    @NotNull(message = FIELD_NOT_NULL)
    @NotBlank(message = FIELD_NOT_BLANK)
    @Size(max = STRING_LENGTH_PASSWORD, message = FIELD_SIZE)
    // the bean can actually have a LOT of chars, in database it will be stored as a hashed (a LOT LESSER characters)
    @Column(nullable = false, length = STRING_LENGTH_PASSWORD)
    @RestResource(exported = false)
    private String password;

    /**
     * Whether the user is enabled or not.
     */
    @Getter(AccessLevel.NONE)
    private Boolean enabled;

    /**
     * Whether the user's email is verified or not.
     */
    @Getter(AccessLevel.NONE)
    private Boolean emailVerified;

    /**
     * Whether the user's account is expired or not.
     */
    @Getter(AccessLevel.NONE)
    private Boolean accountNonExpired;

    /**
     * Whether the user's account is locket or not.
     */
    @Getter(AccessLevel.NONE)
    private Boolean accountNonLocked;

    /**
     * Whether the user's credentials are expired or not.
     */
    @Getter(AccessLevel.NONE)
    private Boolean credentialsNonExpired;

    /**
     * User authorities. These are handled via jjwt, this attribute is kept for compatibility with Spring Security.
     */
    @Getter(AccessLevel.NONE)
    private HashSet<GrantedAuthority> authorities;

    /**
     * Returns the user's authorities.
     *
     * @return An unmodifiable {@link Collection} of entities of subtype of {@link GrantedAuthority}.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities != null ? Collections.unmodifiableCollection(authorities) : null;
    }

    /**
     * Returns whether the user's account is expired or not.
     *
     * @return A {@code boolean} {@code false} if the account is expired, {@code true} otherwise.
     */
    @Override
    public boolean isAccountNonExpired() {
        // by default: true
        return accountNonExpired == null || accountNonExpired;
    }

    /**
     * Returns whether the user's account is locket or not.
     *
     * @return A {@code boolean} {@code false} if the account is locked, {@code true} otherwise.
     */
    @Override
    public boolean isAccountNonLocked() {
        // by default: true
        return accountNonLocked == null || accountNonLocked;
    }

    /**
     * Returns whether the user's credentials are expired or not.
     *
     * @return A {@code boolean} {@code false} if the credentials are expired, {@code true} otherwise.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        // by default: true
        return credentialsNonExpired == null || credentialsNonExpired;
    }

    /**
     * Returns whether the user's account is enabled or not.
     *
     * @return A {@code boolean} {@code true} if the account is enabled, {@code false} otherwise.
     */
    @Override
    public boolean isEnabled() {
        // by default: false
        return enabled != null && enabled;
    }

    /**
     * Returns whether the user's email is verified or not.
     *
     * @return A {@code boolean} {@code true} if the account is enabled, {@code false} otherwise.
     */
    public boolean isEmailVerified() {
        // by default: false
        return emailVerified != null && emailVerified;
    }

    /**
     * Creates a new {@link EUser} from the given arguments.
     *
     * @param username {@link EUser}'s username.
     * @param email    {@link EUser}'s email.
     * @param name     {@link EUser}'s name
     * @param lastName {@link EUser}'s last name.
     * @param password {@link EUser}'s password.
     */
    public EUser(final String username, final String email, final String name, final String lastName,
                 final String password) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.password = password;
    }

}
