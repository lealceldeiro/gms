package com.gmsboilerplatesbng.controller.security.user;

import com.gmsboilerplatesbng.controller.BaseController;
import com.gmsboilerplatesbng.exception.domain.NotFoundEntityException;
import com.gmsboilerplatesbng.mapping.user.AddRolesToUser;
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
    public @ResponseBody GmsResponse addRolesToUser(@RequestBody AddRolesToUser postData) throws NotFoundEntityException {
        userService.addRolesToUser(postData.getUserId(), postData.getEntityId(), postData.getRolesId());
        return new GmsResponse("user.added.success");
    }
}
