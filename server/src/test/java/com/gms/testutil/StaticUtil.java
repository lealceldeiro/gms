package com.gms.testutil;

import org.junit.Assert;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public class StaticUtil {

    /**
     * Checks that the value of every static field in a given class is not repeated by any other static field. This is
     * intended to be used in a JUnit since it makes some assertions provided by the org.junit package
     * @param targetClass Class which the checks are going to be performed.
     */
    public static void testFieldsAreNorRepeated(Class<?> targetClass) {
        List<String> values = new LinkedList<>();
        final Field[] fields = targetClass.getFields();
        String value;
        Object field;
        for(Field f : fields) {
            field = ReflectionTestUtils.getField(targetClass, f.getName());
            value = field != null ? field.toString() : "";
            if (!values.contains(value)) {
                values.add(value);
            }
        }
        Assert.assertEquals(fields.length, values.size());
    }

}
