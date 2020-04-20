package com.gms.appconfiguration.security.authentication.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.util.constant.DefaultConstant;
import com.gms.util.exception.GmsSecurityException;
import com.gms.util.i18n.MessageResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

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
@RequiredArgsConstructor
public final class GmsHttpStatusAndBodyAuthenticationEntryPoint implements GmsAuthenticationEntryPoint {
    /**
     * {@link HttpStatus} to be sent in the response.
     */
    private final HttpStatus httpStatus;

    /**
     * An instance of a {@link DefaultConstant}.
     */
    private final DefaultConstant defaultConstant;
    /**
     * An instance of a {@link MessageResolver}.
     */
    private final MessageResolver messageResolver;

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
        return getResponseBodyForGmsSecurityException(new GmsSecurityException(url), messageResolver, defaultConstant);
    }
}
