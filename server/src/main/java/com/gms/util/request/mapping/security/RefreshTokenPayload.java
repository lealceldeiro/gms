package com.gms.util.request.mapping.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * RefreshTokenPayload
 *
 * @author Asiel Leal Celdeiro <lealceldeiro@gmail.com>
 * @version 0.1
 * Dec 18, 2017
 */
@RequiredArgsConstructor
@Getter
public class RefreshTokenPayload {

    private final String refreshToken;
}
