package com.gms.appconfiguration.security.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.SecurityConst;
import com.gms.util.exception.ExceptionUtil;
import com.gms.util.exception.GmsSecurityException;
import com.gms.util.i18n.MessageResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RequiredArgsConstructor
public final class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {

    /**
     * Instance of {@link DefaultConst}.
     */
    private final DefaultConst dc;
    /**
     * Instance of {@link SecurityConst}.
     */
    private final SecurityConst sc;
    /**
     * Instance of {@link MessageResolver}.
     */
    private final MessageResolver msg;

    /**
     * Called when an authentication attempt fails. This method is intended to be used by the Spring framework and
     * should not be overridden. Doing so may produce unexpected results.
     *
     * @param request   the request during which the authentication attempt occurred.
     * @param response  the response.
     * @param exception the exception which was thrown to reject the authentication
     */
    @Override
    public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response,
                                        final AuthenticationException exception) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.append(getBody());
        }
    }

    private String getBody() throws JsonProcessingException {
        final GmsSecurityException ex = new GmsSecurityException(sc.getSignInUrl(),
                msg.getMessage("security.bad.credentials"));

        final ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(ExceptionUtil.getResponseBodyForGmsSecurityException(ex, msg, dc));
    }

}
