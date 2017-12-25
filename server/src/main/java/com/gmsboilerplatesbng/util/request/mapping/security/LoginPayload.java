package com.gmsboilerplatesbng.util.request.mapping.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * LoginPayload
 *
 * @author Asiel Leal Celdeiro <lealceldeiro@gmail.com>
 * @version 0.1
 * Dec 25, 2017
 */
@RequiredArgsConstructor
@Getter
public class LoginPayload {

    private final String login;
    private final String password;
}
