package com.gms.domain.configuration;

import com.gms.domain.GmsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.gms.util.constant.PersistenceConstant.STRING_LENGTH_DEFAULT;
import static com.gms.util.i18n.CodeI18N.FIELD_NOT_BLANK;
import static com.gms.util.i18n.CodeI18N.FIELD_NOT_NULL;
import static com.gms.util.i18n.CodeI18N.FIELD_SIZE;

/**
 * Represents a configuration through a string-representation under a key and a value.
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public final class BConfiguration extends GmsEntity {

    /*
     * Version number for a Serializable class.
     */
    private static final long serialVersionUID = 5750664701833456936L;

    /**
     * Key under the configuration is saved.
     */
    @NotNull(message = FIELD_NOT_NULL)
    @NotBlank(message = FIELD_NOT_BLANK)
    @Size(max = STRING_LENGTH_DEFAULT, message = FIELD_SIZE)
    @Column(nullable = false)
    private String key;

    /**
     * String representation of the configuration value.
     */
    @NotNull(message = FIELD_NOT_NULL)
    @NotBlank(message = FIELD_NOT_BLANK)
    @Size(max = STRING_LENGTH_DEFAULT, message = FIELD_SIZE)
    @Column(nullable = false)
    private String value;

    /**
     * User's identifier. Some configurations are user-specific. In those cases the identifier of the user is saved too.
     */
    private Long userId;

    /**
     * Creates a new {@link BConfiguration} from the given arguments.
     *
     * @param key   Key under the configuration is saved.
     * @param value String representation of the configuration value.
     */
    public BConfiguration(final String key, final String value) {
        super();
        this.key = key;
        this.value = value;
    }

}
