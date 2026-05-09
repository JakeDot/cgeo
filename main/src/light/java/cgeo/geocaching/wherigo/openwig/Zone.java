package cgeo.geocaching.wherigo.openwig;

/** Minimal Zone stub for light build — Wherigo game engine not available. */
public class Zone {

    public static final int INSIDE = 1;
    public static final int PROXIMITY = 2;
    public static final int DISTANT = 3;
    public static final int NOWHERE = 4;

    public String name = "";
    public int contain = NOWHERE;

    public boolean isActive() {
        return false;
    }

    public boolean isVisible() {
        return false;
    }
}
