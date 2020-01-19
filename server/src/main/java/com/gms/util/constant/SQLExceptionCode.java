package com.gms.util.constant;

/**
 * <p>
 * According to https://www.postgresql.org/docs/9.2/static/errcodes-appendix.html .
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public final class SQLExceptionCode {

    /**
     * NO_DATA code.
     */
    public static final String NO_DATA = "02000";
    /**
     * NO_ADDITIONAL_DYNAMIC_RESULT_SETS_RETURNED code.
     */
    public static final String NO_ADDITIONAL_DYNAMIC_RESULT_SETS_RETURNED = "02001";
    /**
     * CONNECTION_EXCEPTION code.
     */
    public static final String CONNECTION_EXCEPTION = "08000";
    /**
     * CONNECTION_DOES_NOT_EXIST code.
     */
    public static final String CONNECTION_DOES_NOT_EXIST = "08003";
    /**
     * CONNECTION_FAILURE code.
     */
    public static final String CONNECTION_FAILURE = "08006";
    /**
     * SQLCLIENT_UNABLE_TO_ESTABLISH_SQLCONNECTION code.
     */
    public static final String SQLCLIENT_UNABLE_TO_ESTABLISH_SQLCONNECTION = "08001";
    /**
     * SQLSERVER_REJECTED_ESTABLISHMENT_OF_SQLCONNECTION code.
     */
    public static final String SQLSERVER_REJECTED_ESTABLISHMENT_OF_SQLCONNECTION = "08004";
    /**
     * TRANSACTION_RESOLUTION_UNKNOWN code.
     */
    public static final String TRANSACTION_RESOLUTION_UNKNOWN = "08007";
    /**
     * PROTOCOL_VIOLATION code.
     */
    public static final String PROTOCOL_VIOLATION = "08P01";
    /**
     * FEATURE_NOT_SUPPORTED code.
     */
    public static final String FEATURE_NOT_SUPPORTED = "0A000";
    /**
     * INVALID_TRANSACTION_INITIATION code.
     */
    public static final String INVALID_TRANSACTION_INITIATION = "0B000";
    /**
     * LOCATOR_EXCEPTION code.
     */
    public static final String LOCATOR_EXCEPTION = "0F000";
    /**
     * INVALID_LOCATOR_SPECIFICATION code.
     */
    public static final String INVALID_LOCATOR_SPECIFICATION = "0F001";
    /**
     * INVALID_GRANTOR code.
     */
    public static final String INVALID_GRANTOR = "0L000";
    /**
     * INVALID_GRANT_OPERATION code.
     */
    public static final String INVALID_GRANT_OPERATION = "0LP01";
    /**
     * INVALID_ROLE_SPECIFICATION code.
     */
    public static final String INVALID_ROLE_SPECIFICATION = "0P000";
    /**
     * DATA_EXCEPTION code.
     */
    public static final String DATA_EXCEPTION = "22000";
    /**
     * DATETIME_FIELD_OVERFLOW code.
     */
    public static final String DATETIME_FIELD_OVERFLOW = "22008";
    /**
     * DIVISION_BY_ZERO code.
     */
    public static final String DIVISION_BY_ZERO = "22012";
    /**
     * ERROR_IN_ASSIGNMENT code.
     */
    public static final String ERROR_IN_ASSIGNMENT = "22005";
    /**
     * ESCAPE_CHARACTER_CONFLICT code.
     */
    public static final String ESCAPE_CHARACTER_CONFLICT = "2200B";
    /**
     * STRING_DATA_RIGHT_TRUNCATION code.
     */
    public static final String STRING_DATA_RIGHT_TRUNCATION = "22001";
    /**
     * INVALID_DATETIME_FORMAT code.
     */
    public static final String INVALID_DATETIME_FORMAT = "22007";
    /**
     * INVALID_ESCAPE_CHARACTER code.
     */
    public static final String INVALID_ESCAPE_CHARACTER = "22019";
    /**
     * INVALID_ESCAPE_SEQUENCE code.
     */
    public static final String INVALID_ESCAPE_SEQUENCE = "22025";
    /**
     * NONSTANDARD_USE_OF_ESCAPE_CHARACTER code.
     */
    public static final String NONSTANDARD_USE_OF_ESCAPE_CHARACTER = "22P06";
    /**
     * INVALID_PARAMETER_VALUE code.
     */
    public static final String INVALID_PARAMETER_VALUE = "22023";
    /**
     * INVALID_ROW_COUNT_IN_LIMIT_CLAUSE code.
     */
    public static final String INVALID_ROW_COUNT_IN_LIMIT_CLAUSE = "2201W";
    /**
     * INVALID_USE_OF_ESCAPE_CHARACTER code.
     */
    public static final String INVALID_USE_OF_ESCAPE_CHARACTER = "2200C";
    /**
     * NULL_VALUE_NOT_ALLOWED code.
     */
    public static final String NULL_VALUE_NOT_ALLOWED = "22004";
    /**
     * NUMERIC_VALUE_OUT_OF_RANGE code.
     */
    public static final String NUMERIC_VALUE_OUT_OF_RANGE = "22003";
    /**
     * STRING_DATA_LENGTH_MISMATCH code.
     */
    public static final String STRING_DATA_LENGTH_MISMATCH = "22026";
    /**
     * SUBSTRING_ERROR code.
     */
    public static final String SUBSTRING_ERROR = "22011";
    /**
     * ZERO_LENGTH_CHARACTER_STRING code.
     */
    public static final String ZERO_LENGTH_CHARACTER_STRING = "2200F";
    /**
     * FLOATING_POINT_EXCEPTION code.
     */
    public static final String FLOATING_POINT_EXCEPTION = "22P01";
    /**
     * INVALID_TEXT_REPRESENTATION code.
     */
    public static final String INVALID_TEXT_REPRESENTATION = "22P02";
    /**
     * INVALID_BINARY_REPRESENTATION code.
     */
    public static final String INVALID_BINARY_REPRESENTATION = "22P03";
    /**
     * BAD_COPY_FILE_FORMAT code.
     */
    public static final String BAD_COPY_FILE_FORMAT = "22P04";
    /**
     * UNTRANSLATABLE_CHARACTER code.
     */
    public static final String UNTRANSLATABLE_CHARACTER = "22P05";
    /**
     * INTEGRITY_CONSTRAINT_VIOLATION code.
     */
    public static final String INTEGRITY_CONSTRAINT_VIOLATION = "23000";
    /**
     * RESTRICT_VIOLATION code.
     */
    public static final String RESTRICT_VIOLATION = "23001";
    /**
     * NOT_NULL_VIOLATION code.
     */
    public static final String NOT_NULL_VIOLATION = "23502";
    /**
     * FOREIGN_KEY_VIOLATION code.
     */
    public static final String FOREIGN_KEY_VIOLATION = "23503";
    /**
     * UNIQUE_VIOLATION code.
     */
    public static final String UNIQUE_VIOLATION = "23505";
    /**
     * CHECK_VIOLATION code.
     */
    public static final String CHECK_VIOLATION = "23514";
    /**
     * EXCLUSION_VIOLATION code.
     */
    public static final String EXCLUSION_VIOLATION = "23P01";
    /**
     * EXTERNAL_ROUTINE_INVOCATION_EXCEPTION code.
     */
    public static final String EXTERNAL_ROUTINE_INVOCATION_EXCEPTION = "39000";

    /**
     * Privates constructor to make class un-instantiable.
     */
    private SQLExceptionCode() {
    }

}
