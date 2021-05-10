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

    img_cat6_0("questions/images/cat6/0.jpg", Texture.class),
    img_cat6_1("questions/images/cat6/1.jpg", Texture.class),
    img_cat6_2("questions/images/cat6/2.jpg", Texture.class),
    img_cat6_3("questions/images/cat6/3.jpg", Texture.class),
    img_cat6_4("questions/images/cat6/4.jpg", Texture.class),
    img_cat6_5("questions/images/cat6/5.jpg", Texture.class),

    img_cat7_0("questions/images/cat7/0.jpg", Texture.class),
    img_cat7_1("questions/images/cat7/1.jpg", Texture.class),
    img_cat7_2("questions/images/cat7/2.jpg", Texture.class),
    img_cat7_3("questions/images/cat7/3.jpg", Texture.class),
    img_cat7_4("questions/images/cat7/4.jpg", Texture.class),
    img_cat7_5("questions/images/cat7/5.jpg", Texture.class),

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
    unlock("unlock.png", Texture.class),

    game_type_solar_system("campaign/game_type/solar_system.png", Texture.class),
    game_type_solar_system_pressed("campaign/game_type/solar_system_pressed.png", Texture.class),
    game_type_astronomy_quiz("campaign/game_type/astronomy_quiz.png", Texture.class),
    game_type_astronomy_quiz_pressed("campaign/game_type/astronomy_quiz_pressed.png", Texture.class),
    game_type_astronomy_images_quiz("campaign/game_type/astronomy_images_quiz.png", Texture.class),
    game_type_astronomy_images_quiz_pressed("campaign/game_type/astronomy_images_quiz_pressed.png", Texture.class),

    orbital_period("campaign/planets_game_type/orbital_period.png", Texture.class),
    orbital_period_pressed("campaign/planets_game_type/orbital_period_pressed.png", Texture.class),
    radius("campaign/planets_game_type/radius.png", Texture.class),
    radius_pressed("campaign/planets_game_type/radius_pressed.png", Texture.class),
    speed_light("campaign/planets_game_type/speed_light.png", Texture.class),
    distance_sun("campaign/planets_game_type/distance_sun.png", Texture.class),
    distance_sun_pressed("campaign/planets_game_type/distance_sun_pressed.png", Texture.class),
    temperature("campaign/planets_game_type/temperature.png", Texture.class),
    temperature_pressed("campaign/planets_game_type/temperature_pressed.png", Texture.class),
    mass("campaign/planets_game_type/mass.png", Texture.class),
    mass_pressed("campaign/planets_game_type/mass_pressed.png", Texture.class),
    weight("campaign/planets_game_type/weight.png", Texture.class),
    weight_pressed("campaign/planets_game_type/weight_pressed.png", Texture.class),

    star("star.png", Texture.class),
    star_green("star_green.png", Texture.class),
    star_disabled("star_disabled.png", Texture.class),
    star_wrong("star_wrong.png", Texture.class),


    background_music("sounds/background.wav", Music.class),
    level_fail("sounds/level_fail.mp3", Sound.class),
    level_success("sounds/level_success.mp3", Sound.class),

    planet_option_backgr("planet_option_backgr.png", Texture.class),

    specific_labels("labels/labels", I18NBundle.class),
    campaign_level_0_0("campaign/l_0/level_0_0.png", Texture.class),
    campaign_level_0_0_pressed("campaign/l_0/level_0_0_pressed.png", Texture.class),
    campaign_level_0_1("campaign/l_0/level_0_1.png", Texture.class),
    campaign_level_0_1_pressed("campaign/l_0/level_0_1_pressed.png", Texture.class),
    campaign_level_0_2("campaign/l_0/level_0_2.png", Texture.class),
    campaign_level_0_2_pressed("campaign/l_0/level_0_2_pressed.png", Texture.class),
    campaign_level_0_3("campaign/l_0/level_0_3.png", Texture.class),
    campaign_level_0_3_pressed("campaign/l_0/level_0_3_pressed.png", Texture.class),
    campaign_level_0_4("campaign/l_0/level_0_4.png", Texture.class),
    campaign_level_0_4_pressed("campaign/l_0/level_0_4_pressed.png", Texture.class),
    campaign_level_0_5("campaign/l_0/level_0_5.png", Texture.class),
    campaign_level_0_5_pressed("campaign/l_0/level_0_5_pressed.png", Texture.class),
    campaign_level_0_6("campaign/l_0/level_0_6.png", Texture.class),
    campaign_level_0_6_pressed("campaign/l_0/level_0_6_pressed.png", Texture.class),
    campaign_level_0_7("campaign/l_0/level_0_7.png", Texture.class),
    campaign_level_0_7_pressed("campaign/l_0/level_0_7_pressed.png", Texture.class),
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
