package com.gmsboilerplatesbng.component.security;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {

    Authentication getAuthentication();

}
