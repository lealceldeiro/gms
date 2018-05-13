package com.gms;

import com.gms.service.AppService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ApplicationTest {

    @Autowired AppService appService;
    private final String[] noArgs = {};

    @Test
    public void mainTest() {
        Runnable runnable = () -> Application.main(noArgs);
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Test
    public void configureTest() {
        Application app = new Application();
        assertNotNull(app.configure(new SpringApplicationBuilder(Application.class)));
    }

    @Test
    public void commandLineRunnerBeanCreationIsOK() {
        Application app = new Application();
        assertNotNull(app.commandLineRunner(appService));
    }

    @Test
    public void bCryptPasswordEncoderTest() {
        Application app = new Application();
        assertNotNull(app.bCryptPasswordEncoder());
    }
}