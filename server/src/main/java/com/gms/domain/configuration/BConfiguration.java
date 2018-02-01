package com.gms.domain.configuration;

import com.gms.domain.GmsEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 * BConfiguration
 *
 * Represents a configuration through a string-representation under a key and a value.
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 *
 * @version 0.1
 * Dec 12, 2017
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Entity
public class BConfiguration extends GmsEntity{

    /**
     * Key under the configuration is saved.
     */
    @NotNull(message = "validation.field.notNull")
    @NotBlank(message = "validation.field.notBlank")
    @Column(nullable = false)
    @Getter @Setter private String key;

    /**
     * String representation of the configuration value.
     */
    @NotNull(message = "validation.field.notNull")
    @NotBlank(message = "validation.field.notBlank")
    @Column(nullable = false)
    @Getter @Setter private String value;

    /**
     * User's identifier. Some configurations are user-specific. In those cases the identifier of the user is saved too.
     */
    @Getter @Setter private Long userId;

    public BConfiguration() {}

    public BConfiguration(String key, String value) {
        super();
        this.key = key;
        this.value = value;
    }

    public BConfiguration(String key, String value, long userId) {
        super();
        this.key = key;
        this.value = value;
        this.userId = userId;
    }
}
