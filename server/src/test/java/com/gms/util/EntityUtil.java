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

    /**
     * Creates a sample {@link EUser} with random values in its attributes.
     * @return A {@link EUser}
     */
    public static EUser getSampleUser() {
        return getSampleUser(new GMSRandom().nextString());
    }

    /**
     * Creates a sample {@link EUser} with random values in its attributes.
     * @param random Random generator for getting the random values to be used.
     * @return A {@link EUser}
     */
    public static EUser getSampleUser(String random) {
        EUser u = new EUser(EXAMPLE_USERNAME + random ,"a" + random + EXAMPLE_EMAIL, EXAMPLE_NAME + random +
                random, EXAMPLE_LAST_NAME + random, EXAMPLE_PASSWORD + random);
        u.setEnabled(true); // make user enabled for test which required
        return u;
    }

    /**
     * Creates a sample {@link Resource} with a {@link EUser} as content. The {@link EUser} is generated with random
     * values in its attributes.
     * @return A {@link Resource}
     */
    public static Resource<EUser> getSampleUserResource() {
        return getSampleUserResource(new GMSRandom().nextString());
    }

    /**
     * Creates a sample {@link Resource} with a {@link EUser} as content. The {@link EUser} is generated with random
     * values in its attributes.
     * @param random Random generator for getting the random values to be used.
     * @return A {@link Resource}
     */
    public static Resource<EUser> getSampleUserResource(String random) {
        return new Resource<>(getSampleUser(random));
    }

    /**
     * Creates a sample {@link EOwnedEntity} with random values in its attributes.
     * @return An {@link EOwnedEntity}
     */
    public static EOwnedEntity getSampleEntity() {
        return getSampleEntity(new GMSRandom().nextString());
    }

    /**
     * Creates a sample {@link EOwnedEntity} with random values in its attributes.
     * @param random Random generator for getting the random values to be used.
     * @return An {@link EOwnedEntity}
     */
    public static EOwnedEntity getSampleEntity(String random) {
        return new EOwnedEntity(EXAMPLE_NAME + random, EXAMPLE_USERNAME + random, EXAMPLE_DESCRIPTION + random);
    }

    /**
     * Creates a sample {@link Resource} with a {@link EOwnedEntity} as content. The {@link EOwnedEntity} is generated with
     * random values in its attributes.
     * @return A {@link Resource}
     */
    public static Resource<EOwnedEntity> getSampleEntityResource() {
        return getSampleEntityResource(new GMSRandom().nextString());
    }

    /**
     * Creates a sample {@link Resource} with a {@link EOwnedEntity} as content. The {@link EOwnedEntity} is generated with
     * random values in its attributes.
     * @param random Random generator for getting the random values to be used.
     * @return A {@link Resource}
     */
    public static Resource<EOwnedEntity> getSampleEntityResource(String random) {
        return new Resource<>(getSampleEntity(random));
    }

    /**
     * Creates a sample {@link BRole} with random values in its attributes and the {@link BRole#enabled} attributes set
     * to <code>true</code>.
     * @return A {@link BRole}
     */
    public static BRole getSampleRole() {
        return getSampleRole(new GMSRandom().nextString());
    }

    /**
     * Creates a sample {@link BRole} with random values in its attributes and the {@link BRole#enabled} attributes set
     * to <code>true</code>.
     * @param random Random generator for getting the random values to be used.
     * @return A {@link BRole}
     */
    public static BRole getSampleRole(String random) {
        BRole r = new BRole(EXAMPLE_LABEL + random);
        r.setEnabled(true);
        r.setDescription(EXAMPLE_DESCRIPTION + random);
        return r;
    }

    /**
     * Creates a sample {@link Resource} with a {@link BRole} as content. The {@link BRole} is generated with random
     * values in its attributes and the {@link BRole#enabled} attributes set to <code>true</code>.
     * @return A {@link Resource}
     */
    public static Resource<BRole> getSampleRoleResource() {
        return getSampleRoleResource(new GMSRandom().nextString());
    }

    /**
     * Creates a sample {@link Resource} with a {@link BRole} as content. The {@link BRole} is generated with random
     * values in its attributes and the {@link BRole#enabled} attributes set to <code>true</code>.
     * @param random Random generator for getting the random values to be used.
     * @return A {@link Resource}
     */
    public static Resource<BRole> getSampleRoleResource(String random) {
        return new Resource<>(getSampleRole(random));
    }

    /**
     * Creates a sample {@link BPermission} with random values in its attributes.
     * @return A {@link BPermission}
     */
    public static BPermission getSamplePermission() {
        return getSamplePermission(new GMSRandom().nextString());
    }

    /**
     * Creates a sample {@link BPermission} with random values in its attributes.
     * @param random Random generator for getting the random values to be used.
     * @return A {@link BPermission}
     */
    public static BPermission getSamplePermission(String random) {
        return new BPermission(EXAMPLE_NAME + random, EXAMPLE_LABEL + random);
    }

    /**
     * Creates a sample {@link Resource} with a {@link BPermission} as content. The {@link BPermission} is generated with random
     * values in its attributes.
     * @return A {@link Resource}
     */
    public static Resource<BPermission> getSamplePermissionResource() {
        return getSamplePermissionResource(new GMSRandom().nextString());
    }

    /**
     * Creates a sample {@link Resource} with a {@link BPermission} as content. The {@link BPermission} is generated with random
     * values in its attributes.
     * @param random Random generator for getting the random values to be used.
     * @return A {@link Resource}
     */
    public static Resource<BPermission> getSamplePermissionResource(String random) {
        return new Resource<>(getSamplePermission(random));
    }

}
