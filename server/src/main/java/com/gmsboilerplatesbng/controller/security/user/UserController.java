package com.gmsboilerplatesbng.controller.security.user;

import com.gmsboilerplatesbng.controller.BaseController;
import com.gmsboilerplatesbng.exception.GmsGeneralException;
import com.gmsboilerplatesbng.exception.domain.NotFoundEntityException;
import com.gmsboilerplatesbng.mapping.user.RolesForUserOverEntity;
import com.gmsboilerplatesbng.service.security.user.UserService;
import com.gmsboilerplatesbng.util.GmsResponse;
import com.gmsboilerplatesbng.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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
        ArrayList<Long> addedRoles = userService.addRolesToUser(data.getUserId(), data.getEntityId(), data.getRolesId());
        String key = "label.roles";
        ResponseData[] rData = {new ResponseData<>(key, addedRoles)};
        return new GmsResponse("user.roles.add.success", rData);
    }

    @RequestMapping(value = "roles/remove", method = RequestMethod.DELETE)
    public @ResponseBody GmsResponse removeRolesFromUser(@RequestBody RolesForUserOverEntity data)
            throws NotFoundEntityException, GmsGeneralException {
        ArrayList<Long> removedRoles  = userService.removeRolesFromUser(data.getUserId(), data.getEntityId(), data.getRolesId());
        String key = "label.roles";
        ResponseData[] rData = {new ResponseData<>(key, removedRoles)};
        return new GmsResponse("user.roles.remove.success", rData);
    }
}
