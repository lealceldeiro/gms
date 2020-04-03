package com.gms.controller.security.user;

import com.gms.domain.security.user.EUser;
import com.gms.service.security.user.UserService;
import com.gms.util.constant.ResourcePath;
import com.gms.util.exception.GmsGeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RequiredArgsConstructor
@BasePathAwareController
public class RestUserController {

    /**
     * Instance of {@link UserService}.
     */
    private final UserService userService;

    /**
     * Registers a new {@link EUser} setting it as active with properties such as emailVerified and enabled to
     * {@code true}. This method overrides the default "user" url in the
     * {@link com.gms.repository.security.user.EUserRepository} by setting its path to {@code user} (like in the
     * {@code EUserRepository} interface, by doing {@code produces = 'application/hal+json'} and by putting in within a
     * controller annotated as {@code @BasePathAwareController}. This method is intended to be used by the Spring
     * framework and should not be overridden. Doing so may produce unexpected results.
     *
     * @param user {@link EUser} data to be created.
     * @return A {@link EUser} mapped into a @{@link ResponseBody}.
     * @throws GmsGeneralException when an unhandled exception occurs.
     */
    @PostMapping(path = ResourcePath.USER, produces = "application/hal+json")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<EUser> register(@RequestBody @Valid final EntityModel<? extends EUser> user)
            throws GmsGeneralException {
        EUser u = userService.signUp(
                user.getContent(),
                UserService.EmailStatus.VERIFIED,
                UserService.RegistrationPrivilege.SUPER_USER
        );
        if (u != null) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            throw GmsGeneralException.builder().messageI18N("user.add.error").finishedOK(false).build();
        }
    }

}
