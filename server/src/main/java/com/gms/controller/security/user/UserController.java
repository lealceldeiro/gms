package com.gms.controller.security.user;

import com.gms.controller.BaseController;
import com.gms.domain.security.role.BRole;
import com.gms.service.security.user.UserService;
import com.gms.util.constant.ResourcePath;
import com.gms.util.exception.domain.NotFoundEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(ResourcePath.USER)
public class UserController extends BaseController{

    private final UserService userService;

    @GetMapping(ResourcePath.ROLE + "s/{userId}")
    public Map<String, List<BRole>> getRolesForUser(@PathVariable Long userId) throws NotFoundEntityException {
        return userService.getRolesForUser(userId);
    }

    @GetMapping(ResourcePath.ROLE + "s/{userId}/{entityId}")
    public List<BRole> getRolesForUserOverEntity(@PathVariable Long userId, @PathVariable Long entityId)
            throws NotFoundEntityException {
        return userService.getRolesForUserOverEntity(userId, entityId);
    }

    @PostMapping(ResourcePath.ROLE + "s/{userId}/{entityId}")
    public List<Long> addRolesToUser(@PathVariable Long userId, @PathVariable Long entityId,
                                     @RequestBody List<Long> rolesId) throws NotFoundEntityException {
        return userService.addRolesToUser(userId, entityId, rolesId);
    }

    @DeleteMapping(ResourcePath.ROLE + "s/{userId}/{entityId}")
    public List<Long> removeRolesFromUser(@PathVariable Long userId, @PathVariable Long entityId,
                                          @RequestBody  List<Long> rolesId) throws NotFoundEntityException {
        return userService.removeRolesFromUser(userId, entityId, rolesId);
    }
}
