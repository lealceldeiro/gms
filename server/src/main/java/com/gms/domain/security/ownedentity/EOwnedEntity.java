package com.gms.domain.security.ownedentity;

import com.gms.domain.GmsEntity;
import com.gms.util.constant.SecurityConst;
import com.gms.util.i18n.CodeI18N;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.gms.util.constant.PersistenceConstant.STRING_LENGTH_DEFAULT;
import static com.gms.util.constant.PersistenceConstant.STRING_LENGTH_MAX;
import static com.gms.util.i18n.CodeI18N.FIELD_NOT_BLANK;
import static com.gms.util.i18n.CodeI18N.FIELD_NOT_NULL;
import static com.gms.util.i18n.CodeI18N.FIELD_SIZE;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = "description")
@Entity
public final class EOwnedEntity extends GmsEntity {

    /**
     * Version number for a Serializable class.
     */
    private static final long serialVersionUID = 7808731276142204983L;

    /**
     * Natural name which is used commonly for referring to the entity.
     */
    @NotNull(message = FIELD_NOT_NULL)
    @NotBlank(message = FIELD_NOT_BLANK)
    @Size(max = STRING_LENGTH_DEFAULT, message = FIELD_SIZE)
    @Column(nullable = false)
    private String name;

    /**
     * A unique string representation of the {@link #name}. Useful when there are other entities with the same
     * {@link #name}.
     */
    @NotNull(message = FIELD_NOT_NULL)
    @NotBlank(message = FIELD_NOT_BLANK)
    @Size(max = STRING_LENGTH_DEFAULT, message = FIELD_SIZE)
    @Pattern(regexp = SecurityConst.USERNAME_REGEXP, message = CodeI18N.FIELD_PATTERN_INCORRECT_USERNAME)
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * A brief description of the entity.
     */
    @NotNull(message = CodeI18N.FIELD_NOT_NULL)
    @NotBlank(message = CodeI18N.FIELD_NOT_BLANK)
    @Size(max = STRING_LENGTH_MAX, message = CodeI18N.FIELD_SIZE)
    @Column(nullable = false, length = STRING_LENGTH_MAX)
    private String description;

}
