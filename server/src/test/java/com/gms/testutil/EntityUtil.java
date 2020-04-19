package com.gms.testutil;

import com.gms.domain.security.ownedentity.EOwnedEntity;
import com.gms.domain.security.permission.BPermission;
import com.gms.domain.security.role.BRole;
import com.gms.domain.security.user.EUser;
import com.gms.util.GMSRandom;
import org.springframework.hateoas.EntityModel;

import static com.gms.testutil.StringUtil.EXAMPLE_DESCRIPTION;
import static com.gms.testutil.StringUtil.EXAMPLE_EMAIL;
import static com.gms.testutil.StringUtil.EXAMPLE_LABEL;
import static com.gms.testutil.StringUtil.EXAMPLE_LAST_NAME;
import static com.gms.testutil.StringUtil.EXAMPLE_NAME;
import static com.gms.testutil.StringUtil.EXAMPLE_PASSWORD;
import static com.gms.testutil.StringUtil.EXAMPLE_USERNAME;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public final class EntityUtil {

    /**
     * Privates constructor to make class un-instantiable.
     */
    private EntityUtil() {
    }

    /**
     * Creates a sample {@link EUser} with random values in its attributes.
     *
     * @return A {@link EUser}
     */
    public static EUser getSampleUser() {
        return getSampleUser(new GMSRandom().nextString());
    }

    /**
     * Creates a sample {@link EUser} with random values in its attributes.
     *
     * @param random Random generator for getting the random values to be used.
     * @return A {@link EUser}
     */
    public static EUser getSampleUser(final String random) {
        EUser u = new EUser(EXAMPLE_USERNAME + random, "a" + random + EXAMPLE_EMAIL, EXAMPLE_NAME + random
                + random, EXAMPLE_LAST_NAME + random, EXAMPLE_PASSWORD + random);
        u.setEnabled(true); // make user enabled for test which required

        return u;
    }

    /**
     * Creates a sample {@link EntityModel} with a {@link EUser} as content. The {@link EUser} is generated with random
     * values in its attributes.
     *
     * @return A {@link EntityModel}
     */
    public static EntityModel<EUser> getSampleUserResource() {
        return getSampleUserResource(new GMSRandom().nextString());
    }

    /**
     * Creates a sample {@link EntityModel} with a {@link EUser} as content. The {@link EUser} is generated with random
     * values in its attributes.
     *
     * @param random Random generator for getting the random values to be used.
     * @return A {@link EntityModel}
     */
    public static EntityModel<EUser> getSampleUserResource(final String random) {
        return new EntityModel<>(getSampleUser(random));
    }

    /**
     * Creates a sample {@link EOwnedEntity} with random values in its attributes.
     *
     * @return An {@link EOwnedEntity}
     */
    public static EOwnedEntity getSampleEntity() {
        return getSampleEntity(new GMSRandom().nextString());
    }

    /**
     * Creates a sample {@link EOwnedEntity} with random values in its attributes.
     *
     * @param random Random generator for getting the random values to be used.
     * @return An {@link EOwnedEntity}
     */
    public static EOwnedEntity getSampleEntity(final String random) {
        return new EOwnedEntity(EXAMPLE_NAME + random, EXAMPLE_USERNAME + random, EXAMPLE_DESCRIPTION + random);
    }

    /**
     * Creates a sample {@link EntityModel} with a {@link EOwnedEntity} as content. The {@link EOwnedEntity} is
     * generated with random values in its attributes.
     *
     * @return A {@link EntityModel}
     */
    public static EntityModel<EOwnedEntity> getSampleEntityResource() {
        return getSampleEntityResource(new GMSRandom().nextString());
    }

    /**
     * Creates a sample {@link EntityModel} with a {@link EOwnedEntity} as content. The {@link EOwnedEntity} is
     * generated with random values in its attributes.
     *
     * @param random Random generator for getting the random values to be used.
     * @return A {@link EntityModel}
     */
    public static EntityModel<EOwnedEntity> getSampleEntityResource(final String random) {
        return new EntityModel<>(getSampleEntity(random));
    }

    /**
     * Creates a sample {@link BRole} with random values in its attributes and its enabled attribute set
     * to {@code true}.
     *
     * @return A {@link BRole}
     */
    public static BRole getSampleRole() {
        return getSampleRole(new GMSRandom().nextString());
    }

    /**
     * Creates a sample {@link BRole} with random values in its attributes and its enabled attribute set
     * to {@code true}.
     *
     * @param random Random generator for getting the random values to be used.
     * @return A {@link BRole}
     */
    public static BRole getSampleRole(final String random) {
        BRole r = new BRole(EXAMPLE_LABEL + random);
        r.setEnabled(true);
        r.setDescription(EXAMPLE_DESCRIPTION + random);
        return r;
    }

    /**
     * Creates a sample {@link EntityModel} with a {@link BRole} as content. The {@link BRole} is generated with random
     * values in its attributes and its enabled attribute set to {@code true}.
     *
     * @return A {@link EntityModel}
     */
    public static EntityModel<BRole> getSampleRoleResource() {
        return getSampleRoleResource(new GMSRandom().nextString());
    }

    /**
     * Creates a sample {@link EntityModel} with a {@link BRole} as content. The {@link BRole} is generated with random
     * values in its attributes and its enabled attribute set to {@code true}.
     *
     * @param random Random generator for getting the random values to be used.
     * @return A {@link EntityModel}
     */
    public static EntityModel<BRole> getSampleRoleResource(final String random) {
        return new EntityModel<>(getSampleRole(random));
    }

    /**
     * Creates a sample {@link BPermission} with random values in its attributes.
     *
     * @return A {@link BPermission}
     */
    public static BPermission getSamplePermission() {
        return getSamplePermission(new GMSRandom().nextString());
    }

    /**
     * Creates a sample {@link BPermission} with random values in its attributes.
     *
     * @param random Random generator for getting the random values to be used.
     * @return A {@link BPermission}
     */
    public static BPermission getSamplePermission(final String random) {
        return new BPermission(EXAMPLE_NAME + random, EXAMPLE_LABEL + random);
    }

    /**
     * Creates a sample {@link EntityModel} with a {@link BPermission} as content. The {@link BPermission} is generated
     * with random values in its attributes.
     *
     * @return A {@link EntityModel}
     */
    public static EntityModel<BPermission> getSamplePermissionResource() {
        return getSamplePermissionResource(new GMSRandom().nextString());
    }

    /**
     * Creates a sample {@link EntityModel} with a {@link BPermission} as content. The {@link BPermission} is generated
     * with random values in its attributes.
     *
     * @param random Random generator for getting the random values to be used.
     * @return A {@link EntityModel}
     */
    public static EntityModel<BPermission> getSamplePermissionResource(final String random) {
        return new EntityModel<>(getSamplePermission(random));
    }

}
