package com.gms.util.constant;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public final class LinkPath {

    /**
     * Privates constructor to make class un-instantiable.
     */
    private LinkPath() {
    }

    /**
     * "_links" path ins response bodies.
     */
    private static final String LINK = "_links";
    /**
     * "href" path ins response bodies.
     */
    private static final String HREF = "href";
    /**
     * "self" path ins response bodies.
     */
    private static final String SELF = "self";

    /**
     * "_embedded" path ins response bodies.
     */
    public static final String EMBEDDED = "_embedded.";
    /**
     * Meta documentation for the page size parameter.
     */
    public static final String PAGE_SIZE_PARAM_META = "Name of the URL query string parameter that indicates how "
            + "many results to return at once.";
    /**
     * Meta documentation for the page sort parameter.
     */
    public static final String PAGE_SORT_PARAM_META = "Properties that should be sorted by in the format property,"
            + "property(,ASC|DESC). Default sort direction is ascending. Use multiple sort parameters if you want to "
            + "switch directions, e.g. ?sort=property1&sort=property2,asc";
    /**
     * Meta documentation for the page number parameter.
     */
    public static final String PAGE_PAGE_PARAM_META = "Name of the URL query string parameter that indicates what "
            + "page to return.";

    /**
     * Returns a string with the format {@code "_links."} + {@code what} param + {@code ".href"}.
     *
     * @param what The desired String to be included in the resulting string.
     * @return A string concatenation of the three mentioned string
     */
    public static String get(final String what) {
        return String.format("%s.%s.%s", LINK, what, HREF);
    }

    /**
     * Returns a string with the format "_links.self." + {@code what} param .
     *
     * @param what The desired String to be included in the resulting string.
     * @return A string concatenation of the three mentioned string
     */
    public static String getSelf(final String what) {
        return String.format("%s.%s.%s", LINK, SELF, what);
    }

    /**
     * Returns a string with the format "_links.self." + {@code what} param . This is a shortcut for the
     * {@link LinkPath#get(String)} method providing {@link LinkPath#SELF} as argument.
     *
     * @return A string concatenation of the three mentioned string
     */
    public static String get() {
        return get(SELF);
    }

}
