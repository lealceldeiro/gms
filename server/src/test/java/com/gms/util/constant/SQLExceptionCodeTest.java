package com.gms.util.constant;

import com.gms.Application;
import com.gms.testutil.StaticUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SQLExceptionCodeTest {

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void constantsAreNotNull() {
        assertNotNull(SQLExceptionCode.NO_DATA);
        assertNotNull(SQLExceptionCode.NO_ADDITIONAL_DYNAMIC_RESULT_SETS_RETURNED);
        assertNotNull(SQLExceptionCode.CONNECTION_EXCEPTION);
        assertNotNull(SQLExceptionCode.CONNECTION_DOES_NOT_EXIST);
        assertNotNull(SQLExceptionCode.CONNECTION_FAILURE);
        assertNotNull(SQLExceptionCode.SQLCLIENT_UNABLE_TO_ESTABLISH_SQLCONNECTION);
        assertNotNull(SQLExceptionCode.SQLSERVER_REJECTED_ESTABLISHMENT_OF_SQLCONNECTION);
        assertNotNull(SQLExceptionCode.SQLSERVER_REJECTED_ESTABLISHMENT_OF_SQLCONNECTION);
        assertNotNull(SQLExceptionCode.TRANSACTION_RESOLUTION_UNKNOWN);
        assertNotNull(SQLExceptionCode.PROTOCOL_VIOLATION);
        assertNotNull(SQLExceptionCode.FEATURE_NOT_SUPPORTED);
        assertNotNull(SQLExceptionCode.INVALID_TRANSACTION_INITIATION);
        assertNotNull(SQLExceptionCode.LOCATOR_EXCEPTION);
        assertNotNull(SQLExceptionCode.INVALID_LOCATOR_SPECIFICATION);
        assertNotNull(SQLExceptionCode.INVALID_GRANTOR);
        assertNotNull(SQLExceptionCode.INVALID_GRANT_OPERATION);
        assertNotNull(SQLExceptionCode.INVALID_ROLE_SPECIFICATION);
        assertNotNull(SQLExceptionCode.DATA_EXCEPTION);
        assertNotNull(SQLExceptionCode.DATETIME_FIELD_OVERFLOW);
        assertNotNull(SQLExceptionCode.DIVISION_BY_ZERO);
        assertNotNull(SQLExceptionCode.ERROR_IN_ASSIGNMENT);
        assertNotNull(SQLExceptionCode.ESCAPE_CHARACTER_CONFLICT);
        assertNotNull(SQLExceptionCode.STRING_DATA_RIGHT_TRUNCATION);
        assertNotNull(SQLExceptionCode.INVALID_DATETIME_FORMAT);
        assertNotNull(SQLExceptionCode.INVALID_ESCAPE_CHARACTER);
        assertNotNull(SQLExceptionCode.INVALID_ESCAPE_SEQUENCE);
        assertNotNull(SQLExceptionCode.NONSTANDARD_USE_OF_ESCAPE_CHARACTER);
        assertNotNull(SQLExceptionCode.INVALID_PARAMETER_VALUE);
        assertNotNull(SQLExceptionCode.INVALID_ROW_COUNT_IN_LIMIT_CLAUSE);
        assertNotNull(SQLExceptionCode.INVALID_USE_OF_ESCAPE_CHARACTER);
        assertNotNull(SQLExceptionCode.NULL_VALUE_NOT_ALLOWED);
        assertNotNull(SQLExceptionCode.NUMERIC_VALUE_OUT_OF_RANGE);
        assertNotNull(SQLExceptionCode.STRING_DATA_LENGTH_MISMATCH);
        assertNotNull(SQLExceptionCode.SUBSTRING_ERROR);
        assertNotNull(SQLExceptionCode.ZERO_LENGTH_CHARACTER_STRING);
        assertNotNull(SQLExceptionCode.FLOATING_POINT_EXCEPTION);
        assertNotNull(SQLExceptionCode.INVALID_TEXT_REPRESENTATION);
        assertNotNull(SQLExceptionCode.INVALID_BINARY_REPRESENTATION);
        assertNotNull(SQLExceptionCode.BAD_COPY_FILE_FORMAT);
        assertNotNull(SQLExceptionCode.UNTRANSLATABLE_CHARACTER);
        assertNotNull(SQLExceptionCode.INTEGRITY_CONSTRAINT_VIOLATION);
        assertNotNull(SQLExceptionCode.RESTRICT_VIOLATION);
        assertNotNull(SQLExceptionCode.NOT_NULL_VIOLATION);
        assertNotNull(SQLExceptionCode.FOREIGN_KEY_VIOLATION);
        assertNotNull(SQLExceptionCode.UNIQUE_VIOLATION);
        assertNotNull(SQLExceptionCode.CHECK_VIOLATION);
        assertNotNull(SQLExceptionCode.EXCLUSION_VIOLATION);
        assertNotNull(SQLExceptionCode.EXTERNAL_ROUTINE_INVOCATION_EXCEPTION);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void fieldsAreNotRepeated() {
        StaticUtil.testFieldsAreNorRepeated(SQLExceptionCode.class);
    }

}
