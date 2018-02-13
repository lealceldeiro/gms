package com.gms.util.request.mapping.security;

import com.gms.util.i18n.CodeI18N;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * RefreshTokenPayload
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Dec 18, 2017
 */
@RequiredArgsConstructor
@Getter
public class RefreshTokenPayload {

    @NotNull(message = CodeI18N.FIELD_NOT_NULL)
    @NotBlank(message = CodeI18N.FIELD_NOT_BLANK)
    private final String refreshToken;
}
