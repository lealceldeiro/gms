package com.gmsboilerplatesbng.domain.configuration;

import com.gmsboilerplatesbng.domain.GmsEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class BConfiguration extends GmsEntity{

    @NotNull
    @NotBlank
    private final String key;

    @NotNull
    @NotBlank
    private final String value;

    private Long userId;
}
