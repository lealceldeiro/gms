package com.gms.util.exception;

import com.gms.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class GmsErrorTest {

    /**
     * A sample String.
     */
    private static final String MSG = "test";
    /**
     * A sample String.
     */
    private static final String MSG_2 = "test2";
    /**
     * A sample String.
     */
    private static final String FIELD = "field";
    /**
     * A sample String.
     */
    private static final String FIELD_2 = "field2";
    /**
     * A sample String.
     */
    private static final String ERRORS_HOLDER = "errors";

    /**
     * Test to be executed by JUnit.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAllArgsConstructor() {
        Map<String, List<String>> errors = new HashMap<>();
        List<String> l = new LinkedList<>();
        List<String> l2 = new LinkedList<>();
        l.add("a");
        l.add("b");
        l2.add("c");
        l2.add("d");
        errors.put("k1", l);
        errors.put("k2", l2);

        GmsError e = new GmsError(errors);

        Map<String, List<String>> err = (Map<String, List<String>>) ReflectionTestUtils.getField(e, ERRORS_HOLDER);
        assertEquals(err, errors);
    }

    /**
     * Test to be executed by JUnit.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void addErrorFieldMessage() {
        GmsError e = new GmsError();
        e.addError(FIELD, MSG);
        e.addError(FIELD, MSG_2);
        e.addError(FIELD_2, MSG_2);
        Map<String, List<String>> errors = (Map<String, List<String>>) ReflectionTestUtils.getField(e, ERRORS_HOLDER);
        assertNotNull(errors);
        assertTrue(errors.get(FIELD).contains(MSG));
        assertTrue(errors.get(FIELD).contains(MSG_2));
        assertTrue(errors.get(FIELD_2).contains(MSG_2));

        // test also "removeErrors"
        e.removeErrors(FIELD);

        // test also "removeError"
        e.removeError(FIELD_2, MSG_2);

        errors = (Map<String, List<String>>) ReflectionTestUtils.getField(e, ERRORS_HOLDER);
        assertNotNull(errors);
        assertNull(errors.get(FIELD));
    }

    /**
     * Test to be executed by JUnit.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void addErrorMessage() {
        GmsError e = new GmsError();
        e.addError(MSG);
        Map<String, List<String>> errors = (Map<String, List<String>>) ReflectionTestUtils.getField(e, ERRORS_HOLDER);
        assertNotNull(errors);
        assertTrue(errors.get(GmsError.OTHERS).contains(MSG));

        // test also "removeError"
        e.removeError(MSG);

        errors = (Map<String, List<String>>) ReflectionTestUtils.getField(e, ERRORS_HOLDER);
        assertNotNull(errors);
        assertFalse(errors.get(GmsError.OTHERS).contains(MSG));
    }

    /**
     * Test to be executed by JUnit.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void setErrors() {
        Map<String, List<String>> errors = new HashMap<>();
        List<String> l = new LinkedList<>();
        List<String> l2 = new LinkedList<>();
        l.add("a");
        l.add("b");
        l2.add("c");
        l2.add("d");
        errors.put("k1", l);
        errors.put("k2", l2);

        GmsError e = new GmsError();
        e.addError(MSG);
        e.addError(FIELD, MSG_2);
        Map<String, List<String>> currentErrors =
                (Map<String, List<String>>) ReflectionTestUtils.getField(e, ERRORS_HOLDER);
        assertNotNull(currentErrors);
        assertTrue(currentErrors.get(GmsError.OTHERS).contains(MSG));
        assertTrue(currentErrors.get(FIELD).contains(MSG_2));

        e.setErrors(errors);
        currentErrors = (Map<String, List<String>>) ReflectionTestUtils.getField(e, ERRORS_HOLDER);

        if (currentErrors == null) {
            currentErrors = new HashMap<>();
        }

        assertEquals(currentErrors.get("k1"), l);
        assertEquals(currentErrors.get("k2"), l2);
    }

    /**
     * Test to be executed by JUnit.
     */
    @Test
    public void getErrors() {
        Map<String, List<String>> errors = new HashMap<>();
        List<String> l = new LinkedList<>();
        List<String> l2 = new LinkedList<>();
        l.add("a");
        l.add("b");
        l2.add("c");
        l2.add("d");
        errors.put("k1", l);
        errors.put("k2", l2);

        GmsError e = new GmsError();
        ReflectionTestUtils.setField(e, ERRORS_HOLDER, errors);
        Map<String, List<String>> currentErrors = e.getErrors();

        assertEquals(currentErrors.get("k1"), l);
        assertEquals(currentErrors.get("k2"), l2);
    }

}
