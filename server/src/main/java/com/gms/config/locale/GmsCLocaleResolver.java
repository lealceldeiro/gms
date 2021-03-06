package com.gms.config.locale;

import com.gms.util.constant.DefaultConst;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public class GmsCLocaleResolver extends CookieLocaleResolver {

    /**
     * This method is intended to be used by the Spring framework and should not be overridden. Doing so may produce
     * unexpected results.
     *
     * @param request Incoming {@link HttpServletRequest} used to determine the proper locale.
     * @return A {@link Locale} from the current request.
     */
    @Override
    public Locale resolveLocale(final HttpServletRequest request) {
        String acceptLanguage = request.getHeader(DefaultConst.DEFAULT_LANGUAGE_HEADER);
        if (acceptLanguage == null || acceptLanguage.trim().isEmpty()) {
            return super.determineDefaultLocale(request);
        }

        return request.getLocale();
    }

}
