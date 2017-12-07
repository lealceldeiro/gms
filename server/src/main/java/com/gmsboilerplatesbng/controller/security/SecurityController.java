package com.gmsboilerplatesbng.controller.security;

import com.gmsboilerplatesbng.controller.BaseController;
import com.gmsboilerplatesbng.domain.security.user.EUser;
import com.gmsboilerplatesbng.service.configuration.ConfigurationService;
import com.gmsboilerplatesbng.service.security.user.UserService;
import com.gmsboilerplatesbng.util.constant.DefaultConst;
import com.gmsboilerplatesbng.util.exception.GmsGeneralException;
import com.gmsboilerplatesbng.util.i18n.MessageResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@BasePathAwareController
public class SecurityController extends BaseController{

    private final UserService userService;

    private final ConfigurationService configuration;


    @Autowired
    public SecurityController(UserService userService, MessageResolver messageResolver,
                              ConfigurationService configuration, DefaultConst defaultConst) {
        super(defaultConst);
        this.userService = userService;
        this.msg = messageResolver;
        this.configuration = configuration;
    }

    /**
     * Registers a new {@link EUser} setting it as active with properties such as emailVerified and enabled to `true`.
     * This method overrides the default "user" url in the EUserRepository by setting it path to 'user' (like in the
     * `EUserRepository` interface, by produces = 'application/hal+json' and by putting in within a controller
     * annotated as `@BasePathAwareController`.
     * @param user {@link EUser} data to be created.
     * @param pra Injected automatically by Spring.
     * @return A {@link EUser} mapped into a @{@link ResponseBody}.
     * @throws GmsGeneralException when an unhandled exception occurs.
     */
    @PostMapping(path = "user", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public PersistentEntityResource register(@RequestBody Resource<EUser> user, PersistentEntityResourceAssembler pra)
            throws GmsGeneralException {
        return signUpUser(user, true, pra);
    }

    /**
     * Registers a new {@link EUser} setting it as active with some properties such as enabled to `true`.
     * @param user {@link EUser} data to be created
     * @return A {@link EUser} mapped into a @{@link ResponseBody}
     * @throws GmsGeneralException when an unhandled exception occurs.
     */
    @PostMapping("${gms.security.jwt.sign_up_url}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("permitAll()")
    @ResponseBody
    public PersistentEntityResource signUp(@RequestBody Resource<EUser> user, PersistentEntityResourceAssembler pra)
            throws GmsGeneralException {
        if (configuration.isUserUserRegistrationAllowed()) {
            return signUpUser(user, false, pra);
        }
        else throw new GmsGeneralException("user.add.not_allowed", false);
    }

    private PersistentEntityResource signUpUser(Resource<EUser> user, Boolean emailVerified, PersistentEntityResourceAssembler pra)
            throws GmsGeneralException{
        EUser u = userService.signUp(user.getContent(), emailVerified);
        if (u != null) {
            return pra.toResource(u);
        }
        else throw new GmsGeneralException("user.add.error", false);
    }
}
