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

import static org.junit.Assert.*;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class GmsErrorTest {

    private static final String msg = "test";
    private static final String msg2 = "test2";
    private static final String field = "field";
    private static final String field2 = "field2";
    private static final String errorsHolder = "errors";

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

        Map<String, List<String>> err = (Map<String, List<String>>) ReflectionTestUtils.getField(e, errorsHolder);
        assertEquals(err, errors);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void addErrorFieldMessage() {
        GmsError e = new GmsError();
        e.addError(field, msg);
        e.addError(field, msg2);
        e.addError(field2, msg2);
        Map<String, List<String>> errors = (Map<String, List<String>>) ReflectionTestUtils.getField(e, errorsHolder);
        assertNotNull(errors);
        assertTrue(errors.get(field).contains(msg));
        assertTrue(errors.get(field).contains(msg2));
        assertTrue(errors.get(field2).contains(msg2));

        // test also "removeErrors"
        e.removeErrors(field);

        // test also "removeError"
        e.removeError(field2, msg2);

        errors = (Map<String, List<String>>) ReflectionTestUtils.getField(e, errorsHolder);
        assertNotNull(errors);
        assertNull(errors.get(field));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void addErrorMessage() {
        GmsError e = new GmsError();
        e.addError(msg);
        Map<String, List<String>> errors = (Map<String, List<String>>) ReflectionTestUtils.getField(e, errorsHolder);
        assertNotNull(errors);
        assertTrue(errors.get(GmsError.OTHERS).contains(msg));

        // test also "removeError"
        e.removeError(msg);

        errors = (Map<String, List<String>>) ReflectionTestUtils.getField(e, errorsHolder);
        assertNotNull(errors);
        assertFalse(errors.get(GmsError.OTHERS).contains(msg));
    }

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
        e.addError(msg);
        e.addError(field, msg2);
        Map<String, List<String>> currentErrors = (Map<String, List<String>>) ReflectionTestUtils.getField(e, errorsHolder);
        assertNotNull(currentErrors);
        assertTrue(currentErrors.get(GmsError.OTHERS).contains(msg));
        assertTrue(currentErrors.get(field).contains(msg2));

        e.setErrors(errors);
        currentErrors = (Map<String, List<String>>) ReflectionTestUtils.getField(e, errorsHolder);
        assertEquals(currentErrors.get("k1"), l);
        assertEquals(currentErrors.get("k2"), l2);
    }

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
        ReflectionTestUtils.setField(e, errorsHolder, errors);
        Map<String, List<String>> currentErrors = e.getErrors();

        assertEquals(currentErrors.get("k1"), l);
        assertEquals(currentErrors.get("k2"), l2);
    }
}