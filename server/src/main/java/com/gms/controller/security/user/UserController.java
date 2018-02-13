package com.gms.controller.security.user;

import com.gms.controller.BaseController;
import com.gms.domain.security.role.BRole;
import com.gms.service.security.user.UserService;
import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.Resource;
import com.gms.util.exception.domain.NotFoundEntityException;
import com.gms.util.request.mapping.user.RolesForUserOverEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
@RequestMapping(Resource.USER_PATH)
public class UserController extends BaseController{

    private final UserService userService;


    @Autowired
    public UserController(UserService userService, DefaultConst defaultConst) {
        super(defaultConst);
        this.userService = userService;
    }

    @GetMapping("roles/{userUsername}")
    public Map<String, List<BRole>> getRolesForUser(@PathVariable String userUsername) throws NotFoundEntityException {
        return userService.getRolesForUser(userUsername);
    }

    @GetMapping("roles/{userUsername}/{entityUsername}")
    public List<BRole> getRolesForUserOverEntity(@PathVariable String userUsername, @PathVariable String entityUsername)
            throws NotFoundEntityException {
        return userService.getRolesForUserOverEntity(userUsername, entityUsername);
    }

    @PostMapping("roles/{userUsername}/{entityUsername}")
    public List<Long> addRolesToUser(@PathVariable String userUsername, @PathVariable String entityUsername,
                                     @Valid @RequestBody RolesForUserOverEntity data) throws NotFoundEntityException {
        return userService.addRolesToUser(userUsername, entityUsername, data.getRolesId());
    }

    @DeleteMapping("roles/{userUsername}/{entityUsername}")
    public List<Long> removeRolesFromUser(@PathVariable String userUsername, @PathVariable String entityUsername,
                                          @Valid @RequestBody RolesForUserOverEntity data) throws NotFoundEntityException {
        return userService.removeRolesFromUser(userUsername, entityUsername, data.getRolesId());
    }
}
