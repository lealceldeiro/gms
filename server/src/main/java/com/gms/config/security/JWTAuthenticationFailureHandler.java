package com.gms.config.security;

import com.gms.util.constant.DefaultConst;
import com.gms.util.constant.SecurityConst;
import com.gms.util.exception.ExceptionUtil;
import com.gms.util.exception.GmsSecurityException;
import com.gms.util.i18n.MessageResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
@RequiredArgsConstructor
public class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final DefaultConst dc;
    private final SecurityConst sc;
    private final MessageResolver msg;

    /**
     * Called when an authentication attempt fails.
     *
     * @param request   the request during which the authentication attempt occurred.
     * @param response  the response.
     * @param exception the exception which was thrown to reject the authentication
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().append(getBody());
    }

    private String getBody() {
        GmsSecurityException ex = new GmsSecurityException(sc.getSignInUrl(),
                msg.getMessage("security.bad.credentials"));
        return new JSONObject(ExceptionUtil.getResponseBodyForGmsSecurityException(ex, msg, dc)).toString();
    }
}
