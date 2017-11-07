package com.gmsboilerplatesbng.util.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class MessageResolver {

    private final MessageSource messageSource;

    public MessageResolver(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args,
                LocaleContextHolder.getLocale());
    }

    public String getMessage(String code) {
        return getMessage(code, new Object[]{});
    }
}
