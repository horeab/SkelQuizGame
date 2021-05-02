package libgdx.implementations.astronomy;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.I18NBundle;
import libgdx.game.Game;
import libgdx.resources.SpecificResource;

public enum AstronomySpecificResource implements SpecificResource {


    // @formatter:off
    img_cat0_0("questions/images/cat0/0.png", Texture.class),

    img_cat4_0("questions/images/cat4/0.jpg", Texture.class),
    img_cat4_1("questions/images/cat4/1.jpg", Texture.class),
    img_cat4_2("questions/images/cat4/2.jpg", Texture.class),
    img_cat4_3("questions/images/cat4/3.jpg", Texture.class),
    img_cat4_4("questions/images/cat4/4.jpg", Texture.class),
    img_cat4_5("questions/images/cat4/5.jpg", Texture.class),
    img_cat4_6("questions/images/cat4/6.jpg", Texture.class),

    planet_0("planets/0.png", Texture.class),
    planet_1("planets/1.png", Texture.class),
    planet_2("planets/2.png", Texture.class),
    planet_3("planets/3.png", Texture.class),
    planet_4("planets/4.png", Texture.class),
    planet_5("planets/5.png", Texture.class),
    planet_6("planets/6.png", Texture.class),
    planet_7("planets/7.png", Texture.class),
    planet_8("planets/8.png", Texture.class),
    planet_9("planets/9.png", Texture.class),
    planet_10("planets/10.png", Texture.class),
    earth_stroke("planets/earth_stroke.png", Texture.class),

    correct_drag("correct_drag.png", Texture.class),
    wrong_drag("wrong_drag.png", Texture.class),

    orbital_period("campaign/planets_game_type/orbital_period.png", Texture.class),
    radius("campaign/planets_game_type/radius.png", Texture.class),
    speed_light("campaign/planets_game_type/speed_light.png", Texture.class),
    distance_sun("campaign/planets_game_type/distance_sun.png", Texture.class),
    temperature("campaign/planets_game_type/temperature.png", Texture.class),
    mass("campaign/planets_game_type/mass.png", Texture.class),
    weight("campaign/planets_game_type/weight.png", Texture.class),


    background_music("sounds/background.mp3", Music.class),
    level_fail("sounds/level_fail.mp3", Sound.class),
    level_success("sounds/level_success.mp3", Sound.class),

    planet_option_backgr("planet_option_backgr.png", Texture.class),

    specific_labels("labels/labels", I18NBundle.class),
    campaign_level_0_0("campaign/l_0/level_0_0.png", Texture.class),
    campaign_level_0_1("campaign/l_0/level_0_1.png", Texture.class),
    campaign_level_0_1d("campaign/l_0/level_0_1d.png", Texture.class),
    campaign_level_0_2("campaign/l_0/level_0_2.png", Texture.class),
    campaign_level_0_2d("campaign/l_0/level_0_2d.png", Texture.class),
    campaign_level_0_3("campaign/l_0/level_0_3.png", Texture.class),
    campaign_level_0_3d("campaign/l_0/level_0_3d.png", Texture.class),
    campaign_level_0_4("campaign/l_0/level_0_4.png", Texture.class),
    campaign_level_0_4d("campaign/l_0/level_0_4d.png", Texture.class),
    campaign_level_0_5("campaign/l_0/level_0_5.png", Texture.class),
    campaign_level_0_5d("campaign/l_0/level_0_5d.png", Texture.class),
    title_clouds_background("title_clouds_background.png", Texture.class),
    ;

    // @formatter:on

    private String path;
    private Class<?> classType;

    AstronomySpecificResource(String path, Class<?> classType) {
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
