package com.gms.util;

import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.domain.security.permission.BPermission;
import com.gms.domain.security.role.BRole;
import com.gms.domain.security.user.EUser;
import org.springframework.hateoas.Resource;

import static com.gms.util.StringUtil.*;

/**
 * EntityUtil
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Feb 14, 2018
 */
public class EntityUtil {

    private EntityUtil() {}

    public static EUser getSampleUser(String random) {
        return new EUser(EXAMPLE_USERNAME + random ,"a" + random + EXAMPLE_EMAIL, EXAMPLE_NAME + random +
                random, EXAMPLE_LAST_NAME + random, EXAMPLE_PASSWORD + random);
    }

    public static Resource<EUser> getSampleUserResource(String random) {
        return new Resource<>(getSampleUser(random));
    }

    public static EOwnedEntity getSampleEnitity(String random) {
        return new EOwnedEntity(EXAMPLE_NAME + random, EXAMPLE_USERNAME + random, EXAMPLE_DESCRIPTION + random);
    }

    public static Resource<EOwnedEntity> getSampleEnitityResource(String random) {
        return new Resource<>(getSampleEnitity(random));
    }

    public static BRole getSampleRole(String random) {
        BRole r = new BRole(EXAMPLE_LABEL + random);
        r.setEnabled(true);
        r.setDescription(EXAMPLE_DESCRIPTION + random);
        return r;
    }

    public static Resource<BRole> getSampleRoleResource(String random) {
        return new Resource<>(getSampleRole(random));
    }

    public static BPermission getSamplePermission(String random) {
        return new BPermission(EXAMPLE_NAME + random, EXAMPLE_LABEL + random);
    }

    public static Resource<BPermission> getSamplePermissionResource(String random) {
        return new Resource<>(getSamplePermission(random));
    }

}
