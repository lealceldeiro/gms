package com.gms.util.constant;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public class LinkPath {

    private LinkPath() {}

    private static final String LINK = "_links";
    private static final String HREF = "href";
    private static final String SELF = "self";

    public static final String EMBEDDED = "_embedded.";
    public static final String PAGE_SIZE_PARAM_META = "Name of the URL query string parameter that indicates how many results to return at once.";
    public static final String PAGE_SORT_PARAM_META = "Name of the URL query string parameter that indicates what direction to sort results.";
    public static final String PAGE_PAGE_PARAM_META = "Name of the URL query string parameter that indicates what page to return.";

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
