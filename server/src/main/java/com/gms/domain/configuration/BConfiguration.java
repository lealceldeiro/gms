package com.gms.domain.configuration;

import com.gms.domain.GmsEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

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
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class BConfiguration extends GmsEntity{

    /**
     * Key under the configuration is saved.
     */
    @NotNull
    @NotBlank
    private final String key;

    /**
     * String representation of the configuration value.
     */
    @NotNull
    @NotBlank
    private final String value;

    /**
     * [Optional] Some configurations are user-specific. In those cases the identifier of the user is saved too.
     */
    private long userId;
}
