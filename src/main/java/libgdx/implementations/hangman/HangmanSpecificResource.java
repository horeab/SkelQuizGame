package libgdx.implementations.hangman;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.I18NBundle;
import libgdx.game.Game;
import libgdx.resources.SpecificResource;
import sun.net.www.content.image.png;

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
    main_menu_background_texture("main_menu_background_texture.png", Texture.class),

    btn_hangman_correct("buttons/btn_hangman_correct.png", Texture.class),
    btn_hangman_wrong("buttons/btn_hangman_wrong.png", Texture.class),
    btn_hangman_up("buttons/btn_hangman_up.png", Texture.class),
    btn_hangman_down("buttons/btn_hangman_down.png", Texture.class),

    btn_categ_up("buttons/btn_categ_up.png", Texture.class),
    btn_categ_down("buttons/btn_categ_down.png", Texture.class),
    btn_categ_disabled("buttons/btn_categ_disabled.png", Texture.class),
    btn_hint("buttons/btn_hint.png", Texture.class),
    btn_hint_disabled("buttons/btn_hint_disabled.png", Texture.class),

    title_background("title_background.png", Texture.class),

    btn_menu_up("buttons/btn_menu_up.png", Texture.class),
    btn_menu_down("buttons/btn_menu_down.png", Texture.class),

    star("star.png", Texture.class),

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
