package com.gms.controller.security.user;

import com.gms.controller.BaseController;
import com.gms.domain.security.role.BRole;
import com.gms.service.security.user.UserService;
import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.ResourcePath;
import com.gms.util.exception.domain.NotFoundEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * UserController
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 *
 * @version 0.1
 * Dec 12, 2017
 */
@RestController
@RequestMapping(ResourcePath.USER)
public class UserController extends BaseController{

    private final UserService userService;


    @Autowired
    public UserController(UserService userService, DefaultConst defaultConst) {
        super(defaultConst);
        this.userService = userService;
    }

    @GetMapping(ResourcePath.ROLE + "s/{userUsername}")
    public Map<String, List<BRole>> getRolesForUser(@PathVariable String userUsername) throws NotFoundEntityException {
        return userService.getRolesForUser(userUsername);
    }

    @GetMapping(ResourcePath.ROLE + "s/{userUsername}/{entityUsername}")
    public List<BRole> getRolesForUserOverEntity(@PathVariable String userUsername, @PathVariable String entityUsername)
            throws NotFoundEntityException {
        return userService.getRolesForUserOverEntity(userUsername, entityUsername);
    }

    @PostMapping(ResourcePath.ROLE + "s/{userUsername}/{entityUsername}")
    public List<Long> addRolesToUser(@PathVariable String userUsername, @PathVariable String entityUsername,
                                     @RequestBody List<Long> rolesId) throws NotFoundEntityException {
        return userService.addRolesToUser(userUsername, entityUsername, rolesId);
    }

    @DeleteMapping(ResourcePath.ROLE + "s/{userUsername}/{entityUsername}")
    public List<Long> removeRolesFromUser(@PathVariable String userUsername, @PathVariable String entityUsername,
                                          @RequestBody  List<Long> rolesId) throws NotFoundEntityException {
        return userService.removeRolesFromUser(userUsername, entityUsername, rolesId);
    }
}
