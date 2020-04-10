package libgdx.implementations.periodictable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.I18NBundle;
import libgdx.game.Game;
import libgdx.resources.SpecificResource;

public enum PeriodicTableSpecificResource implements SpecificResource {


    // @formatter:off
    specific_labels("labels/labels", I18NBundle.class),

    eltype_0_background("backgrounds/eltype_0_background.png", Texture.class),
    eltype_1_background("backgrounds/eltype_1_background.png", Texture.class),
    eltype_2_background("backgrounds/eltype_2_background.png", Texture.class),
    eltype_3_background("backgrounds/eltype_3_background.png", Texture.class),
    eltype_4_background("backgrounds/eltype_4_background.png", Texture.class),
    eltype_5_background("backgrounds/eltype_5_background.png", Texture.class),
    eltype_6_background("backgrounds/eltype_6_background.png", Texture.class),
    eltype_7_background("backgrounds/eltype_7_background.png", Texture.class),
    eltype_8_background("backgrounds/eltype_8_background.png", Texture.class),
    notfound("backgrounds/notfound.png", Texture.class),
    all_found("backgrounds/all_found.png", Texture.class),
    allq_bakcground("backgrounds/allq_bakcground.png", Texture.class),
    success("backgrounds/success.png", Texture.class),

    play_down("buttons/play_down.png", Texture.class),
    play_up("buttons/play_up.png", Texture.class),
    pt_down("buttons/pt_down.png", Texture.class),
    pt_up("buttons/pt_up.png", Texture.class),
    ;

    // @formatter:on

    private String path;
    private Class<?> classType;

    PeriodicTableSpecificResource(String path, Class<?> classType) {
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
