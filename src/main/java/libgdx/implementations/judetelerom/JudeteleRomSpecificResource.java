package libgdx.implementations.judetelerom;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.I18NBundle;
import libgdx.game.Game;
import libgdx.resources.SpecificResource;

public enum JudeteleRomSpecificResource implements SpecificResource {

    // @formatter:off

    specific_labels("labels/labels", I18NBundle.class),
    notfound("notfound.png", Texture.class),
    allfound("allfound.png", Texture.class),
    allq_bakcground("allq_bakcground.png", Texture.class),
    notansw_background("notansw_background.png", Texture.class),
    correctansw_background("correctansw_background.png", Texture.class),
    img_cat0_999("questions/images/cat0/999.png", Texture.class),
    ;
    // @formatter:on

    private String path;
    private Class<?> classType;

    JudeteleRomSpecificResource(String path, Class<?> classType) {
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
