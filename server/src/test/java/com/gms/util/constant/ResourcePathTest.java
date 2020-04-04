package com.gms.util.constant;

import com.gms.testutil.StaticUtil;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.2
 */
public class ResourcePathTest {

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void constantsAreNotNull() {
        assertNotNull(ResourcePath.CONFIGURATION);
        assertNotNull(ResourcePath.PERMISSION);
        assertNotNull(ResourcePath.MULTI_LIKE);
        assertNotNull(ResourcePath.MULTI);
        assertNotNull(ResourcePath.NAME_LIKE);
        assertNotNull(ResourcePath.LABEL_LIKE);
        assertNotNull(ResourcePath.NAME);
        assertNotNull(ResourcePath.LABEL);
        assertNotNull(ResourcePath.OWNED_ENTITY);
        assertNotNull(ResourcePath.MULTI);
        assertNotNull(ResourcePath.NAME_LIKE);
        assertNotNull(ResourcePath.USERNAME_LIKE);
        assertNotNull(ResourcePath.NAME);
        assertNotNull(ResourcePath.USERNAME);
        assertNotNull(ResourcePath.ROLE);
        assertNotNull(ResourcePath.LABEL_LIKE);
        assertNotNull(ResourcePath.LABEL);
        assertNotNull(ResourcePath.USER);
        assertNotNull(ResourcePath.USERNAME);
        assertNotNull(ResourcePath.EMAIL);
        assertNotNull(ResourcePath.USERNAME_EMAIL);
        assertNotNull(ResourcePath.NAME);
        assertNotNull(ResourcePath.LASTNAME);
        assertNotNull(ResourcePath.USERNAME_LIKE);
        assertNotNull(ResourcePath.USER_SEARCH_EMAIL_LIKE);
        assertNotNull(ResourcePath.NAME_LIKE);
        assertNotNull(ResourcePath.USER_SEARCH_LASTNAME_LIKE);
        assertNotNull(ResourcePath.MULTI);
        assertNotNull(ResourcePath.AUTHORIZATION);
        assertNotNull(ResourcePath.QUERY_VALUE);
        assertNotNull(ResourcePath.USERNAME);
        assertNotNull(ResourcePath.EMAIL);
        assertNotNull(ResourcePath.NAME);
        assertNotNull(ResourcePath.LASTNAME);
        assertNotNull(ResourcePath.LABEL);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void fieldsAreNotRepeated() {
        StaticUtil.testFieldsAreNorRepeated(ResourcePath.class);
    }

}
