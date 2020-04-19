package com.gms.config.security.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.util.constant.DefaultConst;
import com.gms.util.exception.GmsSecurityException;
import com.gms.util.i18n.MessageResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.gms.util.exception.ExceptionUtil.getResponseBodyForGmsSecurityException;

/**
 * Handles access denied to a resource.
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@Component
@RequiredArgsConstructor
public final class GmsAccessDeniedHandler implements AccessDeniedHandler {
    /**
     * An instance of a {@link DefaultConst}.
     */
    private final DefaultConst dc;
    /**
     * An instance of a {@link MessageResolver}.
     */
    private final MessageResolver msg;

    @Override
    public void handle(final HttpServletRequest request, final HttpServletResponse response,
                       final AccessDeniedException accessDeniedException) throws IOException {
        final GmsSecurityException exception = new GmsSecurityException(request.getRequestURL().toString());
        final Object body = getResponseBodyForGmsSecurityException(exception, msg, dc);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        try (PrintWriter writer = response.getWriter()) {
            writer.print(new ObjectMapper().writeValueAsString(body));
        }
    }

}
