package libgdx.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import libgdx.game.Game;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.utils.ScreenDimensionsManager;

public class GraphicUtils {

    public static Image getImage(Res resource) {
        return getImage(getTexture(resource));
    }

    public static Image getImage(Texture texture) {
        return new Image(new TextureRegionDrawable(new TextureRegion(texture)));
    }

    public static Drawable getColorBackground(Color color) {
        Sprite sprite = new Sprite(GraphicUtils.getTexture(MainResource.white_full_background));
        sprite.setColor(color);
        return new SpriteDrawable(sprite);
    }

    public static Texture getTexture(Res resource) {
        Res resToProcess = Game.getInstance().getMainDependencyManager().createResourceService().getOverridableRes(resource);
        Texture texture = getAssetManager(resToProcess).get(resToProcess.getPath(), Texture.class);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        return texture;
    }

    public static NinePatchDrawable getNinePatchIdentical(Res resource) {
        return new NinePatchDrawable(new NinePatch(getTexture(resource)));
    }

    private static AssetManager getAssetManager(Res resource) {
        AssetManager assetManager = Game.getInstance().getAssetManager();
        if (!assetManager.isLoaded(resource.getPath())) {
            assetManager.load(resource.getPath(), resource.getClassType());
            assetManager.finishLoading();
        }
        return assetManager;
    }

    private static Image getImage(Res resource, float width, float height) {
        Image image = getImage(resource);
        image.setWidth(width);
        image.setHeight(height);
        return image;
    }

    public static Image getImage(Res resource, float sideDim) {
        return getImage(resource, sideDim, sideDim);
    }

    public static NinePatchDrawable getNinePatch(Res resource) {
        Res resToProcess = Game.getInstance().getMainDependencyManager().createResourceService().getOverridableRes(resource);
        final Texture t = getAssetManager(resToProcess).get(resToProcess.getPath(), Texture.class);
        return new NinePatchDrawable(new NinePatch(new TextureRegion(t, 1, 1, t.getWidth() - 2, t.getHeight() - 2), 10, 10, 10, 10));
    }

    public static Image addTiledImage(Res image, float y, Texture.TextureWrap textureRepeatType) {
        return addTiledImage(image, y, textureRepeatType, ScreenDimensionsManager.getExternalDeviceHeightValue(30));
    }

    public static Image addTiledImage(Res image, float y, Texture.TextureWrap textureRepeatType, float sideDim) {
        Texture texture = getTexture(image, nextPwTwo(Math.round(sideDim)));
        texture.setWrap(textureRepeatType, textureRepeatType);
        TextureRegion imgTextureRegion = new TextureRegion(texture);
        imgTextureRegion.setRegion(0, 0, ScreenDimensionsManager.getExternalDeviceWidth(), ScreenDimensionsManager.getExternalDeviceHeight());

        TextureRegionDrawable imgTextureRegionDrawable = new TextureRegionDrawable(imgTextureRegion);
        Image img = new Image();
        img.setDrawable(imgTextureRegionDrawable);
        img.setSize(ScreenDimensionsManager.getExternalDeviceWidth(), ScreenDimensionsManager.getExternalDeviceHeight());
        img.setPosition(-ScreenDimensionsManager.getExternalDeviceWidth() / 2 + ScreenDimensionsManager.getScreenWidth() / 2, y);
        return img;
    }

    private static int nextPwTwo(int value) {
        int highestOneBit = Integer.highestOneBit(value);
        if (value == highestOneBit) {
            return value;
        }
        return highestOneBit << 1;
    }


    public static Texture getTexture(Res image, float sideDim) {
        Pixmap pixmapOrignal = new Pixmap(Gdx.files.internal(Game.getInstance().getMainDependencyManager().createResourceService().getOverridableRes(image).getPath()));
        pixmapOrignal.setBlending(Pixmap.Blending.None);
        Pixmap pixmapResized = new Pixmap(Math.round(sideDim), Math.round(sideDim), pixmapOrignal.getFormat());
        pixmapResized.setBlending(Pixmap.Blending.None);
        pixmapResized.drawPixmap(pixmapOrignal,
                0, 0, pixmapOrignal.getWidth(), pixmapOrignal.getHeight(),
                0, 0, pixmapResized.getWidth(), pixmapResized.getHeight()
        );
        Texture texture = new Texture(pixmapResized);
        pixmapOrignal.dispose();
        pixmapResized.dispose();
        return texture;
    }

}
