// Copyright (c) 1999-2004 Brian Wellington (bwelling@xbill.org)

package org.xbill.DNS;

/**
 * Constants relating to the credibility of cached data, which is based on
 * the data's source.  The constants NORMAL and ANY should be used by most
 * callers.
 *
 * @author Brian Wellington
 * @see Cache
 * @see Section
 */

public final class Credibility {

    /**
     * A hint or cache file on disk.
     */
    public static final int HINT = 0;
    /**
     * The additional section of a response.
     */
    public static final int ADDITIONAL = 1;
    /**
     * The additional section of a response.
     */
    public static final int GLUE = 2;
    /**
     * The authority section of a nonauthoritative response.
     */
    public static final int NONAUTH_AUTHORITY = 3;
    /**
     * The answer section of a nonauthoritative response.
     */
    public static final int NONAUTH_ANSWER = 3;
    /**
     * The authority section of an authoritative response.
     */
    public static final int AUTH_AUTHORITY = 4;
    /**
     * The answer section of a authoritative response.
     */
    public static final int AUTH_ANSWER = 4;
    /**
     * A zone.
     */
    public static final int ZONE = 5;
    /**
     * Credible data.
     */
    public static final int NORMAL = 3;
    /**
     * Data not required to be credible.
     */
    public static final int ANY = 1;

    private Credibility() {
    }

}
