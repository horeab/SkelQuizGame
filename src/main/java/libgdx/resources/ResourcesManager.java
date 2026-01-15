package libgdx.resources;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import libgdx.game.Game;

public class ResourcesManager {

    private ResourcesManager() {
        super();
    }

    public static Skin getSkin() {
        return Game.getInstance().getAssetManager().get(MainResource.skin.getPath(), Skin.class);
    }

    public static String getPopupBackground() {
        return "dialog";
    }

    public static Drawable getTableBackgroundDefault() {
        return getSkin().getDrawable("grey_normal");
    }

}
