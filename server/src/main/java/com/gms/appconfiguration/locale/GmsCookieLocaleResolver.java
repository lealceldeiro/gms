package com.gms.appconfiguration.locale;

import com.gms.util.constant.DefaultConstant;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public class GmsCookieLocaleResolver extends CookieLocaleResolver {

    /**
     * This method is intended to be used by the Spring framework and should not be overridden. Doing so may produce
     * unexpected results.
     *
     * @param request Incoming {@link HttpServletRequest} used to determine the proper locale.
     * @return A {@link Locale} from the current request.
     */
    @Override
    public Locale resolveLocale(final HttpServletRequest request) {
        final String acceptLanguage = request.getHeader(DefaultConstant.DEFAULT_LANGUAGE_HEADER);
        if (acceptLanguage == null || acceptLanguage.trim().isEmpty()) {
            return super.resolveLocale(request);
        }

        return request.getLocale();
    }

}
