package com.gms.util.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public class MessageResolver {

    /**
     * Instance of {@link MessageSource}.
     */
    private final MessageSource messageSource;

    /**
     * Creates a new {@link MessageResolver} with the provided {@link MessageSource}.
     *
     * @param messageSource {@link MessageSource} to be used by this message provider.
     */
    public MessageResolver(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Gets the appropriate message according to the request locale given a code.
     *
     * @param code Message code in the resource bundle.
     * @param args Arguments to be interpolated in the resultant message.
     * @return A {@link String} which is the message associated to the specified {@code code}.
     */
    public String getMessage(final String code, final String... args) {
        String[] realArgs = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            realArgs[i] = getMessage(args[i]);
        }

        return messageSource.getMessage(code, realArgs, code, LocaleContextHolder.getLocale());
    }

    /**
     * Gets the appropriate message according to the request locale given a code.
     *
     * @param code Message code in the resource bundle.
     * @return A {@link String} which is the message associated to the specified {@code code}.
     */
    public String getMessage(final String code) {
        return getMessage(code, new String[0]);
    }

}
