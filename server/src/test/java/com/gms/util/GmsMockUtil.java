package com.gms.util;

import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

/**
 * MockUtil
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Mar 17, 2018
 */
public class GmsMockUtil {

    private GmsMockUtil() {}

    public static MockMvc getMvcMock(WebApplicationContext context, FilterChainProxy filterChainProxy) {
        return MockMvcBuilders.webAppContextSetup(context)
                .addFilter(filterChainProxy)
                .alwaysExpect(forwardedUrl(null))
                .build();
    }

    public static MockMvc getMvcMock(WebApplicationContext context, JUnitRestDocumentation restDocumentation,
                                     RestDocumentationResultHandler restDocumentationResultHandler,
                                     FilterChainProxy filterChainProxy) {
        return  MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(restDocumentationResultHandler)
                .addFilter(filterChainProxy)
                .alwaysExpect(forwardedUrl(null))
                .build();
    }

    public static MockMvc getMvcMockForwardUrlNotNull(WebApplicationContext context, JUnitRestDocumentation restDocumentation,
                                     RestDocumentationResultHandler restDocumentationResultHandler,
                                     FilterChainProxy filterChainProxy) {
        return  MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(restDocumentationResultHandler)
                .addFilter(filterChainProxy)
                .build();
    }

}
