package com.gms.repositorydao;

import com.gms.Application;
import com.gms.service.db.QueryService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DAOProviderTest {

    /**
     * Instance of {@link QueryService}.
     */
    @Autowired
    private QueryService queryService;

    /**
     * Instance of {@link DAOProvider}.
     */
    private DAOProvider daoProvider;

    /**
     * "url" string.
     */
    private static final String URL = "url";
    /**
     * BD connection string.
     */
    private static final String URL_VALUE = "jdbc:postgresql://127.0.0.1/gms";
    /**
     * DB invalid connection string.
     */
    private static final String URL_VALUE_UNKNOWN = "jdbc:unknownValue";
    /**
     * Assert message.
     */
    private static final String URL_VALUE_UNKNOWN_MESSAGE = "DAOProvider did not throw NullPointerException with an "
            + "unknownValue db connection url";
    /**
     * Assert message.
     */
    private static final String MESSAGE_WRONG_CONSTRUCTOR_PARAM = "DAOProvider did not throw NullPointerException "
            + "with a null value as QueryService param in the constructor";

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void dAOProviderConstructorMustProvideQueryServiceInstance() {
        boolean threwException = false;
        try {
            new DAOProvider(null);
        } catch (NullPointerException e) {
            threwException = true;
        }
        if (!threwException) {
            Assert.fail(MESSAGE_WRONG_CONSTRUCTOR_PARAM);
        }
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getBAuthorizationDAOOK() {
        daoProvider = new DAOProvider(queryService);
        ReflectionTestUtils.setField(daoProvider, URL, URL_VALUE);
        Assert.assertNotNull(daoProvider.getBAuthorizationDAO());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getBPermissionDAOOK() {
        daoProvider = new DAOProvider(queryService);
        ReflectionTestUtils.setField(daoProvider, URL, URL_VALUE);
        Assert.assertNotNull(daoProvider.getBPermissionDAO());
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getBAuthorizationDAOThrowNullPointer() {
        daoProvider = new DAOProvider(queryService);
        ReflectionTestUtils.setField(daoProvider, URL, URL_VALUE_UNKNOWN);
        boolean threwException = false;
        try {
            Assert.assertNotNull(daoProvider.getBAuthorizationDAO());
        } catch (NullPointerException e) {
            threwException = true;
        }
        if (!threwException) {
            Assert.fail(URL_VALUE_UNKNOWN_MESSAGE);
        }
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getBPermissionDAOOKThrowNullPointer() {
        daoProvider = new DAOProvider(queryService);
        ReflectionTestUtils.setField(daoProvider, URL, URL_VALUE_UNKNOWN);
        boolean threwException = false;
        try {
            Assert.assertNotNull(daoProvider.getBPermissionDAO());
        } catch (NullPointerException e) {
            threwException = true;
        }
        if (!threwException) {
            Assert.fail(URL_VALUE_UNKNOWN_MESSAGE);
        }
    }

}
