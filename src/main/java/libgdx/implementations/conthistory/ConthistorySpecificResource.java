package libgdx.implementations.conthistory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.I18NBundle;

import libgdx.game.Game;
import libgdx.resources.SpecificResource;

public enum ConthistorySpecificResource implements SpecificResource {


    // @formatter:off
    specific_labels("labels/labels", I18NBundle.class),

    btn_categ0_up("buttons/btn_categ0_up.png", Texture.class),
    btn_categ1_up("buttons/btn_categ1_up.png", Texture.class),
    btn_categ2_up("buttons/btn_categ2_up.png", Texture.class),
    btn_categ3_up("buttons/btn_categ3_up.png", Texture.class),
    btn_categ4_up("buttons/btn_categ4_up.png", Texture.class),
    btn_categ0_down("buttons/btn_categ0_down.png", Texture.class),
    btn_categ1_down("buttons/btn_categ1_down.png", Texture.class),
    btn_categ2_down("buttons/btn_categ2_down.png", Texture.class),
    btn_categ3_down("buttons/btn_categ3_down.png", Texture.class),
    btn_categ4_down("buttons/btn_categ4_down.png", Texture.class),
    btn_categ0_disabled("buttons/btn_categ0_disabled.png", Texture.class),
    btn_categ1_disabled("buttons/btn_categ1_disabled.png", Texture.class),
    btn_categ2_disabled("buttons/btn_categ2_disabled.png", Texture.class),
    btn_categ3_disabled("buttons/btn_categ3_disabled.png", Texture.class),
    btn_categ4_disabled("buttons/btn_categ4_disabled.png", Texture.class),
    btn_categ_down("buttons/btn_categ_down.png", Texture.class),
    question_background("question_background.png", Texture.class),
    btn_categ_disabled("buttons/btn_categ_disabled.png", Texture.class),
    title_clouds_background("title_clouds_background.png", Texture.class),
    star("star.png", Texture.class),
    star_disabled("star_disabled.png", Texture.class),
    star_wrong("star_wrong.png", Texture.class),
    success("success.png", Texture.class),
    success_star("success_star.png", Texture.class),
    ;;

    // @formatter:on

    private String path;
    private Class<?> classType;

    ConthistorySpecificResource(String path, Class<?> classType) {
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
