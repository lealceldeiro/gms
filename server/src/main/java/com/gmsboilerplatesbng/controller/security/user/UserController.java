package com.gmsboilerplatesbng.controller.security.user;

import com.gmsboilerplatesbng.controller.BaseController;
import com.gmsboilerplatesbng.domain.security.user.EUser;
import com.gmsboilerplatesbng.service.configuration.ConfigurationService;
import com.gmsboilerplatesbng.service.security.user.UserService;
import com.gmsboilerplatesbng.util.exception.GmsGeneralException;
import com.gmsboilerplatesbng.util.exception.domain.NotFoundEntityException;
import com.gmsboilerplatesbng.util.i18n.MessageResolver;
import com.gmsboilerplatesbng.util.request.mapping.user.RolesForUserOverEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController{

    private final UserService userService;

    private final ConfigurationService configuration;


    @Autowired
    public UserController(UserService userService, MessageResolver messageResolver, ConfigurationService configuration) {
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
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody EUser register(@RequestBody EUser user) throws GmsGeneralException {
        return signUpUser(user, true); //todo: overwrite the default call to the repository
    }

    /**
     * Registers a new {@link EUser}
     * @param user {@link EUser} data to be created
     * @return A {@link EUser} mapped into a @{@link ResponseBody}
     * @throws GmsGeneralException when an unhandled exception occurs
     */
    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody EUser signUp(@RequestBody EUser user) throws GmsGeneralException {
        if (this.configuration.isUserUserRegistrationAllowed()) {
            return signUpUser(user, false);
        }
        else throw new GmsGeneralException("user.add.not_allowed", false);
    }

    private EUser signUpUser(EUser user, Boolean emailVerified) throws GmsGeneralException{
        EUser u = this.userService.signUp(user, true, emailVerified);
        if (u != null) {
            return u;
        }
        else throw new GmsGeneralException("user.add.error", false);
    }

    @PostMapping(value = "roles/add")
    public @ResponseBody ArrayList<Long> addRolesToUser(@RequestBody RolesForUserOverEntity data)
            throws NotFoundEntityException, GmsGeneralException {
        return this.userService.addRolesToUser(data.getUserId(), data.getEntityId(), data.getRolesId());
    }

    @DeleteMapping(value = "roles/remove")
    public @ResponseBody ArrayList<Long> removeRolesFromUser(@RequestBody RolesForUserOverEntity data)
            throws NotFoundEntityException, GmsGeneralException {
        return this.userService.removeRolesFromUser(data.getUserId(), data.getEntityId(), data.getRolesId());
    }
}
