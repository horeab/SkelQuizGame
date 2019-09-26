package libgdx.implementations.hangman;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.I18NBundle;
import libgdx.game.Game;
import libgdx.resources.SpecificResource;

public enum HangmanSpecificResource implements SpecificResource {

    // @formatter:off

    specific_labels("labels/labels", I18NBundle.class),
    h0("h0.png", Texture.class),
    h1("h1.png", Texture.class),
    h2("h2.png", Texture.class),
    h3("h3.png", Texture.class),
    h4("h4.png", Texture.class),
    h5("h5.png", Texture.class),
    h6("h6.png", Texture.class),

    ;
    // @formatter:on

    private String path;
    private Class<?> classType;

    HangmanSpecificResource(String path, Class<?> classType) {
        this.path = path;
        this.classType = classType;
    }

    @Override
    public Class<?> getClassType() {
        return classType;
    }

    @Override
    public String getPath() {
        return Game.getInstance().getAppInfoService().getImplementationGameResourcesFolder() + path;
    }

}
