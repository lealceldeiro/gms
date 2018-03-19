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
    public static final String PAGE_SIZE_ATTR_META = "Size of every pages which will be returned.";
    public static final String PAGE_SORT_ATTR_META = "Attributes to be used for sorting the results.";
    public static final String PAGE_NUMBER_ATTR_META = "Total pages according to the number of items to be returned per page and total number of items found.";

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
