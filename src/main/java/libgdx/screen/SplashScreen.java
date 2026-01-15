package libgdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import libgdx.game.Game;
import libgdx.resources.MainResource;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.RGBColor;

public class SplashScreen extends Stage implements com.badlogic.gdx.Screen {

    private Game game;

    public SplashScreen() {
        game = Game.getInstance();
        addSplashImage();
    }

    private void addSplashImage() {
        Texture texture = new Texture(MainResource.splashtitle.getPath());
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image image = new Image(new TextureRegion(texture));
        Table table = new Table();
        table.setFillParent(true);
        float factor = 1.5f;
        float sideSize = game.getAppInfoService().isPortraitMode() ? ScreenDimensionsManager.getScreenWidth() / factor : ScreenDimensionsManager.getScreenHeight() / factor;
        table.add(image).width(sideSize).height(sideSize);
        addActor(table);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void resize(int width, int height) {
        getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void render(float delta) {
        RGBColor backgroundColor = Game.getInstance().getSubGameDependencyManager().getScreenBackgroundColor();
        Gdx.gl20.glClearColor(backgroundColor.r / 255f, backgroundColor.g / 255f, backgroundColor.b / 255f, backgroundColor.a);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (game.getAssetManager().update(200)) {
            game.executeAfterAssetsLoaded();
        }
        act(delta);
        draw();
    }
}
