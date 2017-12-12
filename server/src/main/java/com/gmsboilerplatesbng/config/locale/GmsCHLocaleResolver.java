package com.gmsboilerplatesbng.config.locale;

import com.gmsboilerplatesbng.util.constant.DefaultConst;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * GmsCHLocaleResolver
 *
 * @author Asiel Leal Celdeiro <lealceldeiro@gmail.com>
 *
 * @version 0.1
 * Dec 12, 2017
 */
@RequiredArgsConstructor
public class GmsCHLocaleResolver extends CookieLocaleResolver {

    private final DefaultConst dc;

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String acceptLanguage = request.getHeader(dc.DEFAULT_LANGUAGE_HEADER);
        if (acceptLanguage == null || acceptLanguage.trim().isEmpty()) {
            return super.determineDefaultLocale(request);
        }
        return request.getLocale();
    }

}