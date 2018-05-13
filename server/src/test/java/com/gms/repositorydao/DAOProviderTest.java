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

    @Autowired private QueryService queryService;

    private DAOProvider daoProvider;
    private final static String URL = "url";
    private final static String URL_VALUE = "jdbc:postgresql://127.0.0.1/gms";
    private final static String URL_VALUE_UNKNOWN = "jdbc:unknownValue";
    private final static String URL_VALUE_UNKNOWN_MESSAGE = "DAOProvider did not throw NullPointerException with an" +
            "unknownValue db connection url";
    private final static String MESSAGE_WRONG_CONSTRUCTOR_PARAM = "DAOProvider did not throw NullPointerException with a" +
            "null value as QueryService param in the constructor";

    @Test
    public void DAOProviderConstructorMustProvideQueryServiceInstance() {
        boolean threwException = false;
        try {
            new DAOProvider(null);
        }
        catch (NullPointerException e) {
            threwException = true;
        }
        if (!threwException) {
            Assert.fail(MESSAGE_WRONG_CONSTRUCTOR_PARAM);
        }
    }

    @Test
    public void getBAuthorizationDAOOK() {
        daoProvider = new DAOProvider(queryService);
        ReflectionTestUtils.setField(daoProvider, URL, URL_VALUE);
        Assert.assertNotNull(daoProvider.getBAuthorizationDAO());
    }

    @Test
    public void getBPermissionDAOOK() {
        daoProvider = new DAOProvider(queryService);
        ReflectionTestUtils.setField(daoProvider, URL, URL_VALUE);
        Assert.assertNotNull(daoProvider.getBPermissionDAO());
    }

    @Test
    public void getBAuthorizationDAOThrowNullPointer() {
        daoProvider = new DAOProvider(queryService);
        ReflectionTestUtils.setField(daoProvider, URL, URL_VALUE_UNKNOWN);
        boolean threwException = false;
        try {
            Assert.assertNotNull(daoProvider.getBAuthorizationDAO());
        }
        catch (NullPointerException e) {
            threwException = true;
        }
        if (!threwException) {
            Assert.fail(URL_VALUE_UNKNOWN_MESSAGE);
        }
    }

    @Test
    public void getBPermissionDAOOKThrowNullPointer() {
        daoProvider = new DAOProvider(queryService);
        ReflectionTestUtils.setField(daoProvider, URL, URL_VALUE_UNKNOWN);
        boolean threwException = false;
        try {
            Assert.assertNotNull(daoProvider.getBPermissionDAO());
        }
        catch (NullPointerException e) {
            threwException = true;
        }
        if (!threwException) {
            Assert.fail(URL_VALUE_UNKNOWN_MESSAGE);
        }
    }
}