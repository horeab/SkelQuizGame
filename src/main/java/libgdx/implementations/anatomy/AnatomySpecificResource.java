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

    title_background("title_background.png", Texture.class),
    star("star.png", Texture.class),
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
