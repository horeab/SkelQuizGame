package libgdx.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.apache.commons.lang3.tuple.Pair;

import libgdx.game.Game;


public class ScreenDimensionsManager {

    //HEIGHT divided WIDTH
    public static final float STANDARD_SCREEN_RATIO = 1.777083333333333f;
    private static Integer screenWidth;
    private static Integer screenHeight;
    private static boolean isPortrait = Game.getInstance().getAppInfoService().isPortraitMode();

    public static float getScreenHeight(float percent) {
        return Utils.getValueForPercent(getScreenHeight(), percent);
    }

    public static float getScreenWidth(float percent) {
        return Utils.getValueForPercent(getScreenWidth(), percent);
    }

    public static float getNewHeightForNewWidth(float newWidth, float originalWidth, float originalHeight) {
        return (originalHeight / originalWidth) * newWidth;
    }

    public static float getNewHeightForNewWidth(float newWidth, Image image) {
        return getNewHeightForNewWidth(newWidth, image.getWidth(), image.getHeight());
    }

    public static Pair<Float, Float> resize(float toMinMaxWidth, float toMinMaxHeight, Image image) {
        float newWidth;
        float newHeight;
        if (image.getHeight() > image.getWidth()) {
            newHeight = toMinMaxHeight;
            newWidth = getNewWidthForNewHeight(newHeight, image);
        } else {
            newWidth = toMinMaxWidth;
            newHeight = getNewHeightForNewWidth(newWidth, image);
        }
        return Pair.of(newWidth, newHeight);
    }

    public static float getNewWidthForNewHeight(float newHeight, float originalWidth, float originalHeight) {
        return (originalWidth / originalHeight) * newHeight;
    }

    public static float getNewWidthForNewHeight(float newHeight, Image image) {
        return getNewWidthForNewHeight(newHeight, image.getWidth(), image.getHeight());
    }

    public static int getScreenWidth() {
        if (screenWidth == null) {
            int width = getExternalDeviceWidth();
            //if FALSE, width is larger, so width must be adjusted
            if (!isGdxGraphicsRatioGreaterThanStandard()) {
                width = isPortrait ?
                        Math.round(getExternalDeviceHeight() / STANDARD_SCREEN_RATIO) :
                        Math.round(getExternalDeviceHeight() * STANDARD_SCREEN_RATIO);
            }
            screenWidth = width;
        }
        return screenWidth;
    }

    public static int getScreenHeight() {
        if (screenHeight == null) {
            int height = getExternalDeviceHeight();
            //if TRUE, width is smaller, so height must be adjusted
            if (isGdxGraphicsRatioGreaterThanStandard()) {
                height = isPortrait ?
                        Math.round(getExternalDeviceWidth() * STANDARD_SCREEN_RATIO) :
                        Math.round(getExternalDeviceWidth() / STANDARD_SCREEN_RATIO);
            }
            screenHeight = height;
        }
        return screenHeight;
    }

    //If return TRUE, means that the WIDTH is lower than the standard
    private static boolean isGdxGraphicsRatioGreaterThanStandard() {
        return getGdxScreenRatio() > STANDARD_SCREEN_RATIO;
    }

    private static float getGdxScreenRatio() {
        return isPortrait ?
                getExternalDeviceHeight() / Float.valueOf(getExternalDeviceWidth()) :
                getExternalDeviceWidth() / Float.valueOf(getExternalDeviceHeight());
    }

    public static float getExternalDeviceHeightValue(float percent) {
        return Utils.getValueForPercent(getExternalDeviceHeight(), percent);
    }

    public static int getExternalDeviceHeight() {
        return Gdx.graphics.getHeight();
    }

    public static int getExternalDeviceWidth() {
        return Gdx.graphics.getWidth();
    }

    public static void resizeViewPort(Viewport viewport, int width, int height) {
        viewport.update(width, ScreenDimensionsManager.getResizeMarginHeight(height), false);
        viewport.getCamera().position.x = width / 2;
        viewport.getCamera().position.y = height / 2;
    }

    private static int getResizeMarginHeight(int height) {
        int topMarginPercent = Math.round(Game.getInstance().getAppInfoService().gameScreenTopMargin() / ScreenDimensionsManager.getScreenHeight() * 100);
        return height - Math.round(topMarginPercent / 100f * height);
    }

    public static float getScreenOrientationVal(float isPortraitValue, float isLandscapeValue) {
        return isPortrait ? isPortraitValue : isLandscapeValue;
    }

}
