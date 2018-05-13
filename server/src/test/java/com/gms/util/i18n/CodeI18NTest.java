package com.gms.util.i18n;

import com.gms.Application;
import com.gms.testutil.StaticUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class CodeI18NTest {


    @Test
    public void constantsAreNotNull() {
        Assert.assertNotNull(CodeI18N.FIELD_NOT_BLANK);
        Assert.assertNotNull(CodeI18N.FIELD_NOT_NULL);
        Assert.assertNotNull(CodeI18N.FIELD_SIZE);
        Assert.assertNotNull(CodeI18N.FIELD_NOT_WELL_FORMED);
        Assert.assertNotNull(CodeI18N.FIELD_PATTERN_INCORRECT_USERNAME);
    }

    @Test
    public void fieldsAreNotRepeated() {
        StaticUtil.testFieldsAreNorRepeated(CodeI18N.class);
    }
}