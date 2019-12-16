package libgdx.implementations.paintings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.I18NBundle;

import libgdx.game.Game;
import libgdx.resources.SpecificResource;

public enum PaintingsSpecificResource implements SpecificResource {

    // @formatter:off

    specific_labels("labels/labels", I18NBundle.class),


    title_clouds_background("title_clouds_background.png", Texture.class),
    star("star.png", Texture.class),
    question_background("question_background.png", Texture.class),
    background_texture("background_texture.png", Texture.class),

    btn_categ0_up("buttons/btn_categ0_up.png", Texture.class),
    btn_categ1_up("buttons/btn_categ1_up.png", Texture.class),
    btn_categ2_up("buttons/btn_categ2_up.png", Texture.class),
    btn_categ3_up("buttons/btn_categ3_up.png", Texture.class),
    btn_categ4_up("buttons/btn_categ4_up.png", Texture.class),
    btn_categ_star("buttons/btn_categ_star.png", Texture.class),
    btn_categ_finished("buttons/btn_categ_finished.png", Texture.class),
    btn_categ_down("buttons/btn_categ_down.png", Texture.class),
    btn_categ_disabled("buttons/btn_categ_disabled.png", Texture.class),;

    // @formatter:on

    private String path;
    private Class<?> classType;

    PaintingsSpecificResource(String path, Class<?> classType) {
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
