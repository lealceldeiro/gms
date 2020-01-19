package com.gms.testutil;

import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public final class GmsMockUtil {

    /**
     * Privates constructor to make class un-instantiable.
     */
    private GmsMockUtil() {
    }

    /**
     * Creates and returns a {@link MockMvc} which always expects null as the forwarded url.
     *
     * @param context                        {@link WebApplicationContext} with the configuration for a web application.
     * @param restDocumentation              An instance of a {@link RestDocumentationContextProvider} to provide access
     *                                       to the {@link org.springframework.restdocs.RestDocumentationContext}.
     * @param restDocumentationResultHandler An instance of a {@link ResultHandler} to perform and action on the result
     *                                       of an executed request.
     * @param filterChainProxy               The filter to add to the {@link MockMvc} object.
     * @return The initialized and created MockMvc.
     */
    public static MockMvc getMvcMock(final WebApplicationContext context,
                                     final RestDocumentationContextProvider restDocumentation,
                                     final ResultHandler restDocumentationResultHandler,
                                     final Filter filterChainProxy) {
        return MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(restDocumentationResultHandler)
                .addFilter(filterChainProxy)
                .alwaysExpect(forwardedUrl(null))
                .build();
    }

    /**
     * Creates and returns a {@link MockMvc} which always expects null as the forwarded url.
     *
     * @param context          {@link WebApplicationContext} with the configuration for a web application.
     * @param filterChainProxy The filter to add to the {@link MockMvc} object.
     * @return The initialized and created MockMvc.
     */
    public static MockMvc getMvcMock(final WebApplicationContext context, final Filter filterChainProxy) {
        return MockMvcBuilders.webAppContextSetup(context)
                .addFilter(filterChainProxy)
                .alwaysExpect(forwardedUrl(null))
                .build();
    }

    /**
     * Creates and returns a {@link MockMvc}.
     *
     * @param context                        {@link WebApplicationContext} with the configuration for a web application.
     * @param restDocumentation              An instance of a {@link RestDocumentationContextProvider} to provide access
     *                                       to the {@link org.springframework.restdocs.RestDocumentationContext}.
     * @param restDocumentationResultHandler An instance of a {@link ResultHandler} to perform and action on the result
     *                                       of an executed request.
     * @param filterChainProxy               The filter to add to the {@link MockMvc} object.
     * @return The initialized and created MockMvc.
     */
    public static MockMvc getMvcMockForwardUrlNotNull(final WebApplicationContext context,
                                                      final RestDocumentationContextProvider restDocumentation,
                                                      final ResultHandler restDocumentationResultHandler,
                                                      final Filter filterChainProxy) {
        return MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(restDocumentationResultHandler)
                .addFilter(filterChainProxy)
                .build();
    }

}
