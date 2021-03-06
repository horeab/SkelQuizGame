package libgdx.implementations.flags;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.I18NBundle;
import libgdx.game.Game;
import libgdx.resources.SpecificResource;

public enum FlagsSpecificResource implements SpecificResource {

    // @formatter:off

    specific_labels("labels/labels", I18NBundle.class),
    allq_bakcground("allq_bakcground.png", Texture.class),
    star("star.png", Texture.class),

    btn_campaign_0_down("buttons/btn_0_down.png", Texture.class),
    btn_campaign_0_up("buttons/btn_0_up.png", Texture.class),
    btn_campaign_1_down("buttons/btn_1_down.png", Texture.class),
    btn_campaign_1_up("buttons/btn_1_up.png", Texture.class),
    btn_campaign_2_down("buttons/btn_2_down.png", Texture.class),
    btn_campaign_2_up("buttons/btn_2_up.png", Texture.class),

    background_texture_0("background_texture_0.png", Texture.class),
    background_texture_1("background_texture_1.png", Texture.class),
    background_texture_2("background_texture_2.png", Texture.class),
    wall_texture("wall_texture.png", Texture.class),

    campaign_level_0_0("campaign/level_0_0.png", Texture.class),
    campaign_level_0_1("campaign/level_0_1.png", Texture.class),
    campaign_level_0_2("campaign/level_0_2.png", Texture.class),
    campaign_level_0_3("campaign/level_0_3.png", Texture.class),
    campaign_level_0_4("campaign/level_0_4.png", Texture.class),
    ;
    // @formatter:on

    private String path;
    private Class<?> classType;

    FlagsSpecificResource(String path, Class<?> classType) {
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
