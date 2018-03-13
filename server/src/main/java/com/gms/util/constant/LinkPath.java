package com.gms.util.constant;

/**
 * LinkPath
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 * Mar 12, 2018
 */
public class LinkPath {

    private LinkPath() {}

    private static final String LINK = "_links";
    private static final String HREF = "href";
    private static final String SELF = "self";

    public static final String EMBEDDED = "_embedded.";

    public static String get(String what) {
        return String.format("%s.%s.%s", LINK, what, HREF);
    }

    public static String getSelf(String what) {
        return String.format("%s.%s.%s", LINK, SELF, what);
    }

    public static String get() {
        return get(SELF);
    }

}
