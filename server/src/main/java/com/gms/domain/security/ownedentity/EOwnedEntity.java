package com.gms.domain.security.ownedentity;

import com.gms.domain.GmsEntity;
import com.gms.util.constant.SecurityConst;
import com.gms.util.i18n.CodeI18N;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = "description")
@Entity
public class EOwnedEntity extends GmsEntity{

    /**
     * Natural name which is used commonly for referring to the entity.
     */
    @NotNull(message = CodeI18N.FIELD_NOT_NULL)
    @NotBlank(message = CodeI18N.FIELD_NOT_BLANK)
    @Size(max = 255, message = CodeI18N.FIELD_SIZE)
    @Column(nullable = false, length = 255)
    private final String name;

    /**
     * A unique string representation of the {@link #name}. Useful when there are other entities with the same {@link #name}.
     */
    @NotNull(message = CodeI18N.FIELD_NOT_NULL)
    @NotBlank(message = CodeI18N.FIELD_NOT_BLANK)
    @Size(max = 255, message = CodeI18N.FIELD_SIZE)
    @Pattern(regexp = SecurityConst.USERNAME_REGEXP, message = CodeI18N.FIELD_PATTERN_INCORRECT_USERNAME)
    @Column(unique = true, nullable = false, length = 255)
    private final String username;

    /**
     * A brief description of the entity.
     */
    @NotNull(message = CodeI18N.FIELD_NOT_NULL)
    @NotBlank(message = CodeI18N.FIELD_NOT_BLANK)
    @Size(max = 10485760, message = CodeI18N.FIELD_SIZE)
    @Column(nullable = false, length = 10485760)
    private final String description;

}
