package libgdx.implementations.countries;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.I18NBundle;

import libgdx.game.Game;
import libgdx.resources.SpecificResource;

public enum CountriesSpecificResource implements SpecificResource {


    // @formatter:off
    specific_labels("labels/labels", I18NBundle.class),

    moving_background("moving_background.png", Texture.class),
    title_clouds_background("title_clouds_background.png", Texture.class),

    campaign_level_0_0("campaign/l_0/level_0_0.png", Texture.class),
    campaign_level_0_1("campaign/l_0/level_0_1.png", Texture.class),
    campaign_level_0_2("campaign/l_0/level_0_2.png", Texture.class),
    campaign_level_0_3("campaign/l_0/level_0_3.png", Texture.class),
    campaign_level_0_4("campaign/l_0/level_0_4.png", Texture.class),
    campaign_level_0_5("campaign/l_0/level_0_5.png", Texture.class),

    all_found("all_found.png", Texture.class),
    notansw_background("notansw_background.png", Texture.class),
    correctansw_background("correctansw_background.png", Texture.class),

    play_down("buttons/play_down.png", Texture.class),
    play_up("buttons/play_up.png", Texture.class),
    ;

    // @formatter:on

    private String path;
    private Class<?> classType;

    CountriesSpecificResource(String path, Class<?> classType) {
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
