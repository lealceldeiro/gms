package com.gmsboilerplatesbng.controller.security.user;

import com.gmsboilerplatesbng.controller.BaseController;
import com.gmsboilerplatesbng.domain.security.user.EUser;
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


    @Autowired
    public UserController(UserService userService, MessageResolver messageResolver) {
        this.userService = userService;
        this.msg = messageResolver;
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody EUser signUp(@RequestBody EUser user) throws GmsGeneralException{
        EUser u = this.userService.signUp(user);
        if (u != null) {
            return u;
        }
        else throw new GmsGeneralException(this.msg.getMessage("exception.general"), false);
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
