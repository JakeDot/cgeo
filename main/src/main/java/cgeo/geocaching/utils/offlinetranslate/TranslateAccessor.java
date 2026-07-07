package cgeo.geocaching.utils.offlinetranslate;

import cgeo.geocaching.utils.Log;

public class TranslateAccessor {

    private static final ITranslateAccessor INSTANCE;
    // Set to true to use the DevTranslateAccessor stub (simulates translation without real backend)
    private static final boolean DO_TEST = false;

    static {
        ITranslateAccessor instance = null;
        if (DO_TEST) {
            instance = new DevTranslateAccessor();
        } else {
            try {
                instance = new BergamotTranslateAccessor();
                Log.iForce("TranslateAccessor: Bergamot instance created");
            } catch (final Exception e) {
                Log.e("TranslateAccessor: Could not initialize Bergamot", e);
            }
        }
        INSTANCE = instance == null ? new NoopTranslateAccessor() : instance;
    }

    private TranslateAccessor() {
        //no instances
    }

    public static ITranslateAccessor get() {
        return INSTANCE;
    }

}
