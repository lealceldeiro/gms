package com.gmsboilerplatesbng.controller.security;

import com.gmsboilerplatesbng.controller.BaseController;
import com.gmsboilerplatesbng.domain.security.user.EUser;
import com.gmsboilerplatesbng.service.configuration.ConfigurationService;
import com.gmsboilerplatesbng.service.security.user.UserService;
import com.gmsboilerplatesbng.util.constant.DefaultConst;
import com.gmsboilerplatesbng.util.exception.GmsGeneralException;
import com.gmsboilerplatesbng.util.i18n.MessageResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
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
     * Registers a new {@link EUser}
     * @param user {@link EUser} data to be created
     * @return A {@link EUser} mapped into a @{@link ResponseBody}
     * @throws GmsGeneralException when an unhandled exception occurs
     */
    @PostMapping("user/")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody EUser register(@RequestBody EUser user) throws GmsGeneralException {
        return signUpUser(user, true); //todo: overwrite the default call to the repository
    }

    //todo: make sign-up and login url changeable
    /**
     * Registers a new {@link EUser}
     * @param user {@link EUser} data to be created
     * @return A {@link EUser} mapped into a @{@link ResponseBody}
     * @throws GmsGeneralException when an unhandled exception occurs
     */
    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("permitAll()")
    public @ResponseBody EUser signUp(@RequestBody EUser user) throws GmsGeneralException {
        if (this.configuration.isUserUserRegistrationAllowed()) {
            return signUpUser(user, false);
        }
        else throw new GmsGeneralException("user.add.not_allowed", false);
    }

    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public @ResponseBody Map login(@RequestBody Object authData) {
        return  new HashMap();// todo
    }

    private EUser signUpUser(EUser user, Boolean emailVerified) throws GmsGeneralException{
        EUser u = this.userService.signUp(user, true, emailVerified);
        if (u != null) {
            return u;
        }
        else throw new GmsGeneralException("user.add.error", false);
    }
}
