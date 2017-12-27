package com.gms.controller.security.user;

import com.gms.controller.BaseController;
import com.gms.service.security.user.UserService;
import com.gms.util.constant.DefaultConst;
import com.gms.util.exception.domain.NotFoundEntityException;
import com.gms.util.i18n.MessageResolver;
import com.gms.util.request.mapping.user.RolesForUserOverEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UserController
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 *
 * @version 0.1
 * Dec 12, 2017
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController{

    private final UserService userService;


    @Autowired
    public UserController(UserService userService, MessageResolver messageResolver, DefaultConst defaultConst) {
        super(defaultConst);
        this.userService = userService;
        this.msg = messageResolver;
    }

    @PostMapping("roles/add")
    public List<Long> addRolesToUser(@RequestBody RolesForUserOverEntity data) throws NotFoundEntityException {
        return userService.addRolesToUser(data.getUserId(), data.getEntityId(), data.getRolesId());
    }

    @DeleteMapping("roles/remove")
    public List<Long> removeRolesFromUser(@RequestBody RolesForUserOverEntity data) throws NotFoundEntityException {
        return userService.removeRolesFromUser(data.getUserId(), data.getEntityId(), data.getRolesId());
    }
}
