package cgeo.geocaching.wherigo;

/**
 * The "lite" flavor has no Wherigo player. See the "full" flavor's version of
 * this class.
 */
public final class WherigoSystemInfo {

    private WherigoSystemInfo() {
        // utility class
    }

    public static void append(final StringBuilder body) {
        body.append("\n")
            .append("\nWherigo: not available in this build");
    }
}
