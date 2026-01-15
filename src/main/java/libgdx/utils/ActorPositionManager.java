package libgdx.utils;

import com.badlogic.gdx.scenes.scene2d.Actor;


public class ActorPositionManager {

    public static void setActorCenterScreen(Actor view) {
        centerInRegion(view, ScreenDimensionsManager.getScreenWidth(), ScreenDimensionsManager.getScreenHeight());
    }

    public static void setActorCenterExternalScreen(Actor view) {
        centerInRegion(view, ScreenDimensionsManager.getExternalDeviceWidth(), ScreenDimensionsManager.getExternalDeviceHeight());
    }

    private static void centerInRegion(Actor view, int regionWidth, int regionHeight) {
        view.setPosition(regionWidth / 2 - view.getWidth() / 2, regionHeight / 2 - view.getHeight() / 2);
    }

    public static void setActorCenterHorizontalOnScreen(Actor view) {
        view.setPosition(ScreenDimensionsManager.getScreenWidth() / 2 - view.getWidth() / 2, view.getY());
    }

    public static void setActorCenterVerticalOnScreen(Actor view) {
        view.setPosition(view.getX(), ScreenDimensionsManager.getScreenHeight() / 2 - view.getHeight() / 2);
    }

    public static void setActorBelowAnchor(Actor view, Actor anchorView) {
        view.setPosition(view.getX(), anchorView.getY() - view.getHeight());
    }

    public static void setActorAboveAnchor(Actor view, Actor anchorView) {
        view.setPosition(view.getX(), anchorView.getY() + view.getHeight());
    }

    public static void marginTop(Actor view, float margin) {
        view.setPosition(view.getX(), view.getY() - margin);
    }
}
