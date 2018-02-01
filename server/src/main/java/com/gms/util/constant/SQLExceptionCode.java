package com.gms.util.constant;

/**
 * SQLExceptionCode
 * <p>
 * According to https://www.postgresql.org/docs/9.2/static/errcodes-appendix.html
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Feb 01, 2018
 */
public class SQLExceptionCode {

    public static final String NO_DATA = "02000";
    public static final String NO_ADDITIONAL_DYNAMIC_RESULT_SETS_RETURNED = "02001";
    public static final String CONNECTION_EXCEPTION = "08000";
    public static final String CONNECTION_DOES_NOT_EXIST = "08003";
    public static final String CONNECTION_FAILURE = "08006";
    public static final String SQLCLIENT_UNABLE_TO_ESTABLISH_SQLCONNECTION = "08001";
    public static final String SQLSERVER_REJECTED_ESTABLISHMENT_OF_SQLCONNECTION = "08004";
    public static final String TRANSACTION_RESOLUTION_UNKNOWN = "08007";
    public static final String PROTOCOL_VIOLATION = "08P01";
    public static final String FEATURE_NOT_SUPPORTED = "0A000";
    public static final String INVALID_TRANSACTION_INITIATION = "0B000";
    public static final String LOCATOR_EXCEPTION = "0F000";
    public static final String INVALID_LOCATOR_SPECIFICATION = "0F001";
    public static final String INVALID_GRANTOR = "0L000";
    public static final String INVALID_GRANT_OPERATION = "0LP01";
    public static final String INVALID_ROLE_SPECIFICATION = "0P000";
    public static final String DATA_EXCEPTION = "22000";
    public static final String DATETIME_FIELD_OVERFLOW = "22008";
    public static final String DIVISION_BY_ZERO = "22012";
    public static final String ERROR_IN_ASSIGNMENT = "22005";
    public static final String ESCAPE_CHARACTER_CONFLICT = "2200B";
    public static final String STRING_DATA_RIGHT_TRUNCATION = "22001";
    public static final String INVALID_DATETIME_FORMAT = "22007";
    public static final String INVALID_ESCAPE_CHARACTER = "22019";
    public static final String INVALID_ESCAPE_SEQUENCE = "22025";
    public static final String NONSTANDARD_USE_OF_ESCAPE_CHARACTER = "22P06";
    public static final String INVALID_PARAMETER_VALUE = "22023";
    public static final String INVALID_ROW_COUNT_IN_LIMIT_CLAUSE = "2201W";
    public static final String INVALID_USE_OF_ESCAPE_CHARACTER = "2200C";
    public static final String NULL_VALUE_NOT_ALLOWED = "22004";
    public static final String NUMERIC_VALUE_OUT_OF_RANGE = "22003";
    public static final String STRING_DATA_LENGTH_MISMATCH = "22026";
    public static final String SUBSTRING_ERROR = "22011";
    public static final String ZERO_LENGTH_CHARACTER_STRING = "2200F";
    public static final String FLOATING_POINT_EXCEPTION = "22P01";
    public static final String INVALID_TEXT_REPRESENTATION = "22P02";
    public static final String INVALID_BINARY_REPRESENTATION = "22P03";
    public static final String BAD_COPY_FILE_FORMAT = "22P04";
    public static final String UNTRANSLATABLE_CHARACTER = "22P05";
    public static final String INTEGRITY_CONSTRAINT_VIOLATION = "23000";
    public static final String RESTRICT_VIOLATION = "23001";
    public static final String NOT_NULL_VIOLATION = "23502";
    public static final String FOREIGN_KEY_VIOLATION = "23503";
    public static final String UNIQUE_VIOLATION = "23505";
    public static final String CHECK_VIOLATION = "23514";
    public static final String EXCLUSION_VIOLATION = "23P01";
    public static final String EXTERNAL_ROUTINE_INVOCATION_EXCEPTION = "39000";

    private SQLExceptionCode() {
    }
}
