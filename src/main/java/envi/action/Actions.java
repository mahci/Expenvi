package envi.action;

public class Actions {

    public static final String ACT_CLICK        = "CLICK";
    public static final String ACT_PRESS_PRI    = "PRESS_PRI";
    public static final String ACT_PRESS_SEC    = "PRESS_SEC";
    public static final String ACT_RELEASE_PRI  = "RELEASE_PRI";
    public static final String ACT_RELEASE_SEC  = "RELEASE_SEC";
    public static final String ACT_CANCEL       = "CANCEL";

    // ===============================================================================

    // Actions done by a mouse
    public enum ACT {
        PRESS,
        RELEASE,
        CLICK,
        CANCEL
    }

    // Mouse buttons
    public enum MOUSE_BTN {
        PRIMARY,
        SECONDARY
    }

}
