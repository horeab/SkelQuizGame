package libgdx.resources.dimen;

import java.util.Arrays;

import libgdx.utils.ScreenDimensionsManager;

public class DimenUtils {

    public static float getDimen(Dimen dimen) {
        if (startWithXtoken(dimen)) {
            return ScreenDimensionsManager.getScreenWidth(dimen.getRawDimen());
        } else {
            return ScreenDimensionsManager.getScreenHeight(dimen.getRawDimen());
        }
    }

    public static int getIntegerValueOfDimen(Dimen dimen) {
        return Math.round(getDimen(dimen));
    }

    private static boolean startWithXtoken(Dimen dimen) {
        for (String xToken : Arrays.asList("width_", "left_", "right_", "horizontal_")) {
            if (dimen.name().startsWith(xToken)) {
                return true;
            }
        }
        return false;
    }

}
