package com.gmsboilerplatesbng.controller.security.user;

import com.gmsboilerplatesbng.controller.BaseController;
import com.gmsboilerplatesbng.exception.GmsGeneralException;
import com.gmsboilerplatesbng.exception.domain.NotFoundEntityException;
import com.gmsboilerplatesbng.mapping.user.RolesForUserOverEntity;
import com.gmsboilerplatesbng.service.security.user.UserService;
import com.gmsboilerplatesbng.util.GmsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController{

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "roles/add", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody GmsResponse addRolesToUser(@RequestBody RolesForUserOverEntity data)
            throws NotFoundEntityException, GmsGeneralException {
        boolean success = userService.addRolesToUser(data.getUserId(), data.getEntityId(), data.getRolesId());

        if(success) { return new GmsResponse("user.roles.add.success"); }
        else throw new GmsGeneralException();
    }

    @RequestMapping(value = "roles/remove", method = RequestMethod.DELETE)
    public @ResponseBody GmsResponse removeRolesFromUser(@RequestBody RolesForUserOverEntity data)
            throws NotFoundEntityException, GmsGeneralException {
        boolean success = userService.removeRolesFromUser(data.getUserId(), data.getEntityId(), data.getRolesId());

        if(success) { return new GmsResponse("user.roles.remove.success"); }
        else throw new GmsGeneralException();
    }
}
