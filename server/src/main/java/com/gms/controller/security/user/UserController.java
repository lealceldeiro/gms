package com.gms.controller.security.user;

import com.gms.domain.security.role.BRole;
import com.gms.service.security.user.UserService;
import com.gms.util.constant.ResourcePath;
import com.gms.util.exception.domain.NotFoundEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(ResourcePath.USER)
public class UserController {

    /**
     * Instance of {@link UserService}.
     */
    private final UserService userService;

    /**
     * Returns all roles assigned to a user. This method is intended to be used by the Spring framework and should not
     * be overridden. Doing so may produce unexpected results.
     *
     * @param userId {@link com.gms.domain.security.user.EUser}'s id from which the associated roles will be returned.
     * @return A Map of {@link List} or {@link BRole} with all roles associated to a user.
     * @throws NotFoundEntityException if there is not user registered with that id.
     */
    @GetMapping(ResourcePath.ROLE + "s/{userId}")
    public Map<String, List<BRole>> getRolesForUser(@PathVariable final Long userId) throws NotFoundEntityException {
        return userService.getRolesForUser(userId);
    }

    /**
     * Returns all roles assigned to a user for a specific owned entity. This method is intended to be used by the
     * Spring framework and should not be overridden. Doing so may produce unexpected results
     *
     * @param userId   {@link com.gms.domain.security.user.EUser}'s id from which the associated roles will be returned.
     * @param entityId {@link com.gms.domain.security.ownedentity.EOwnedEntity}'s id from which the roles are associated
     *                 to the user.
     * @return A {@link List} of {@link BRole} associated to the user over the entity.
     * @throws NotFoundEntityException If either the user or the entities are not found from the given id.
     */
    @GetMapping(ResourcePath.ROLE + "s/{userId}/{entityId}")
    public List<BRole> getRolesForUserOverEntity(@PathVariable final Long userId, @PathVariable final Long entityId)
            throws NotFoundEntityException {
        return userService.getRolesForUserOverEntity(userId, entityId);
    }

    /**
     * Adds some role(s) to a user over an entity. This method is intended to be used by the Spring framework and
     * should not be overridden. Doing so may produce unexpected results.
     *
     * @param userId   {@link com.gms.domain.security.user.EUser}'s id.
     * @param entityId {@link com.gms.domain.security.ownedentity.EOwnedEntity}'s id.
     * @param rolesId  {@link List} containing all {@link BRole} ids.
     * @return A {@link List} with the id of all roles added to the user.
     * @throws NotFoundEntityException If either the user, the entity or none of the roles was found by the list of ids.
     */
    @PostMapping(ResourcePath.ROLE + "s/{userId}/{entityId}")
    public List<Long> addRolesToUser(@PathVariable final Long userId, @PathVariable final Long entityId,
                                     @RequestBody final List<Long> rolesId) throws NotFoundEntityException {
        return userService.addRolesToUser(userId, entityId, rolesId);
    }

    /**
     * Adds some role(s) to a user from an entity. This method is intended to be used by the Spring framework and
     * should not be overridden. Doing so may produce unexpected results.
     *
     * @param userId   @link com.gms.domain.security.user.EUser}'s id.
     * @param entityId {@link com.gms.domain.security.ownedentity.EOwnedEntity}'s id.
     * @param rolesId  {@link List} containing all {@link BRole} ids.
     * @return A {@link List} with the id of all roles removed from the user.
     * @throws NotFoundEntityException If either the user, the entity or none of the roles was found by the list of ids.
     */
    @DeleteMapping(ResourcePath.ROLE + "s/{userId}/{entityId}")
    public List<Long> removeRolesFromUser(@PathVariable final Long userId, @PathVariable final Long entityId,
                                          @RequestBody final List<Long> rolesId) throws NotFoundEntityException {
        return userService.removeRolesFromUser(userId, entityId, rolesId);
    }

}
