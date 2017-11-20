package com.gmsboilerplatesbng.domain.configuration;

import com.gmsboilerplatesbng.domain.GmsEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class BConfiguration extends GmsEntity{

    @NotNull
    @NotBlank
    private final String key;

    @NotNull
    @NotBlank
    private final String value;

    private Long userId;
}
