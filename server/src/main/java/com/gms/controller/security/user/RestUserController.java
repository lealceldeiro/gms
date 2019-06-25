package com.gms.controller.security.user;

import javax.validation.Valid;

import com.gms.domain.security.user.EUser;
import com.gms.service.security.user.UserService;
import com.gms.util.constant.ResourcePath;
import com.gms.util.exception.GmsGeneralException;

import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.RequiredArgsConstructor;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RequiredArgsConstructor
@BasePathAwareController
public class RestUserController {

    private final UserService userService;

    /**
     * Registers a new {@link EUser} setting it as active with properties such as emailVerified and enabled to `true`.
     * This method overrides the default "user" url in the {@link com.gms.repository.security.user.EUserRepository}
     * by setting its path to 'user' (like in the `EUserRepository` interface, by doing `produces = 'application/hal+json'`
     * and by putting in within a controller annotated as `@BasePathAwareController`.
     * @param user {@link EUser} data to be created.
     * @return A {@link EUser} mapped into a @{@link ResponseBody}.
     * @throws GmsGeneralException when an unhandled exception occurs.
     */
    @PostMapping(path = ResourcePath.USER, produces = "application/hal+json")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<EUser> register(@Valid @RequestBody Resource<EUser> user)
            throws GmsGeneralException {
        EUser u = userService.signUp(user.getContent(), true, true);
        if (u != null) {
            return new ResponseEntity<EUser>(HttpStatus.CREATED);
        }
        else throw new GmsGeneralException("user.add.error", false);
    }

}
