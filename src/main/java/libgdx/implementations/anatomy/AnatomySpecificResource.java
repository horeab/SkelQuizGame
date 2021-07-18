package libgdx.implementations.anatomy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.I18NBundle;

import libgdx.game.Game;
import libgdx.resources.SpecificResource;

public enum AnatomySpecificResource implements SpecificResource {

    // @formatter:off

    specific_labels("labels/labels", I18NBundle.class),

    background_texture("background_texture.png", Texture.class),

    img_cat0_0("questions/images/0.png", Texture.class),
    img_cat1_1("questions/images/1.png", Texture.class),
    img_cat2_2("questions/images/2.png", Texture.class),
    img_cat3_3("questions/images/3.png", Texture.class),
    img_cat4_4("questions/images/4.png", Texture.class),
    img_cat5_5("questions/images/5.png", Texture.class),
    img_cat6_6("questions/images/6.png", Texture.class),
    img_cat7_7("questions/images/7.png", Texture.class),
    img_cat8_8("questions/images/8.png", Texture.class),
    img_cat9_9("questions/images/9.png", Texture.class),
    img_cat10_10("questions/images/10.png", Texture.class),
    img_cat11_11("questions/images/11.png", Texture.class),
    img_cat0_0s("questions/images/0s.png", Texture.class),
    img_cat1_1s("questions/images/1s.png", Texture.class),
    img_cat2_2s("questions/images/2s.png", Texture.class),
    img_cat3_3s("questions/images/3s.png", Texture.class),
    img_cat4_4s("questions/images/4s.png", Texture.class),
    img_cat5_5s("questions/images/5s.png", Texture.class),
    img_cat6_6s("questions/images/6s.png", Texture.class),
    img_cat7_7s("questions/images/7s.png", Texture.class),
    img_cat8_8s("questions/images/8s.png", Texture.class),
    img_cat9_9s("questions/images/9s.png", Texture.class),
    img_cat10_10s("questions/images/10s.png", Texture.class),
    img_cat11_11s("questions/images/11s.png", Texture.class),
    img_cat0_0t("questions/images/0t.png", Texture.class),
    img_cat1_1t("questions/images/1t.png", Texture.class),
    img_cat2_2t("questions/images/2t.png", Texture.class),
    img_cat3_3t("questions/images/3t.png", Texture.class),
    img_cat4_4t("questions/images/4t.png", Texture.class),
    img_cat5_5t("questions/images/5t.png", Texture.class),
    img_cat6_6t("questions/images/6t.png", Texture.class),
    img_cat7_7t("questions/images/7t.png", Texture.class),
    img_cat8_8t("questions/images/8t.png", Texture.class),
    img_cat9_9t("questions/images/9t.png", Texture.class),
    img_cat10_10t("questions/images/10t.png", Texture.class),
    img_cat11_11t("questions/images/11t.png", Texture.class),

    btn_generalk_down("buttons/btn_generalk_down.png", Texture.class),
    btn_generalk_up("buttons/btn_generalk_up.png", Texture.class),
    btn_identify_down("buttons/btn_identify_down.png", Texture.class),
    btn_identify_up("buttons/btn_identify_up.png", Texture.class),

    arrow_left("arrow_left.png", Texture.class),
    arrow_left_pointer("arrow_left_pointer.png", Texture.class),
    unlock("unlock.png", Texture.class),

    title_background("title_background.png", Texture.class),
    star("star.png", Texture.class),
    star_half("star_half.png", Texture.class),
    star_level("star_level.png", Texture.class),
    star_green("star_green.png", Texture.class),
    star_disabled("star_disabled.png", Texture.class),
    success("success.png", Texture.class),;
    // @formatter:on

    private String path;
    private Class<?> classType;

    AnatomySpecificResource(String path, Class<?> classType) {
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
