package com.gms.config.security.authentication.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.util.constant.DefaultConst;
import com.gms.util.exception.GmsSecurityException;
import com.gms.util.i18n.MessageResolver;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.gms.util.exception.ExceptionUtil.getResponseBodyForGmsSecurityException;

/**
 * Provides custom means to customize the response on authentication failure requests by allowing to be set an
 * {@link org.springframework.http.HttpStatus}.
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public final class GmsHttpStatusAndBodyEntryPoint implements GmsEntryPoint {
    /**
     * {@link HttpStatus} to be sent in the response.
     */
    private final HttpStatus httpStatus;

    /**
     * An instance of a {@link DefaultConst}.
     */
    private final DefaultConst dc;
    /**
     * An instance of a {@link MessageResolver}.
     */
    private final MessageResolver msg;

    /**
     * Creates a new {@link GmsHttpStatusAndBodyEntryPoint} which will provide the response with the given
     * {@code httpStatus} and a specific value as the body. How the body is shaped depends on the provided
     * {@code httpStatus}, that's it, the {@code httpStatus} determines which will the body be in the response.
     *
     * @param dc         A {@link DefaultConst} instance to help in creating the response body.
     * @param msg        A {@link MessageResolver} instance to help in creating the response body.
     * @param httpStatus Response {@link HttpStatus}.
     */
    public GmsHttpStatusAndBodyEntryPoint(final HttpStatus httpStatus, final DefaultConst dc,
                                          final MessageResolver msg) {
        Assert.notNull(httpStatus, "httpStatus cannot be null");
        Assert.notNull(dc, "dc cannot be null");
        Assert.notNull(msg, "msg cannot be null");
        this.httpStatus = httpStatus;
        this.dc = dc;
        this.msg = msg;
    }

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException {
        response.setStatus(httpStatus.value());
        try (PrintWriter writer = response.getWriter()) {
            writer.print(new ObjectMapper().writeValueAsString(responseBody(request.getRequestURL().toString())));
        }
    }

    private Object responseBody(final String url) {
        // update latter to return objects with a different information as different HttpStatus are handled
        return getResponseBodyForGmsSecurityException(new GmsSecurityException(url), msg, dc);
    }
}
