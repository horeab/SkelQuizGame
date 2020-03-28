package libgdx.implementations.hangmanarena;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.I18NBundle;
import libgdx.game.Game;
import libgdx.resources.SpecificResource;

public enum HangmanArenaSpecificResource implements SpecificResource {

    // @formatter:off

    specific_labels("labels/labels", I18NBundle.class),
    main_menu_screen_background("backgrounds/main_menu_screen_background.png", Texture.class),
    title_clouds_background("title_clouds_background.png", Texture.class),


    h0("hangmangame/h0.png", Texture.class),
    h1("hangmangame/h1.png", Texture.class),
    h2("hangmangame/h2.png", Texture.class),
    h3("hangmangame/h3.png", Texture.class),
    h4("hangmangame/h4.png", Texture.class),
    h5("hangmangame/h5.png", Texture.class),
    h6("hangmangame/h6.png", Texture.class),
    skyb0("hangmangame/skyb0.png", Texture.class),
    skyb1("hangmangame/skyb1.png", Texture.class),
    skyb2("hangmangame/skyb2.png", Texture.class),
    skyb3("hangmangame/skyb3.png", Texture.class),
    skyb4("hangmangame/skyb4.png", Texture.class),
    skyb5("hangmangame/skyb5.png", Texture.class),
    skyb6("hangmangame/skyb6.png", Texture.class),
    smoke("hangmangame/smoke.png", Texture.class),
    forest_texture("hangmangame/forest_texture.png", Texture.class),
    grass_texture("hangmangame/grass_texture.png", Texture.class),
    campaign_level_0_0("campaign/l_0/level_0_0.png", Texture.class),
    campaign_level_0_1("campaign/l_0/level_0_1.png", Texture.class),
    campaign_level_0_2("campaign/l_0/level_0_2.png", Texture.class),
    campaign_level_0_3("campaign/l_0/level_0_3.png", Texture.class),
    campaign_level_0_4("campaign/l_0/level_0_4.png", Texture.class),
    campaign_level_1_0("campaign/l_1/level_1_0.png", Texture.class),
    campaign_level_1_1("campaign/l_1/level_1_1.png", Texture.class),
    campaign_level_1_2("campaign/l_1/level_1_2.png", Texture.class),
    campaign_level_1_3("campaign/l_1/level_1_3.png", Texture.class),
    campaign_level_1_4("campaign/l_1/level_1_4.png", Texture.class),
    campaign_level_2_0("campaign/l_2/level_2_0.png", Texture.class),
    campaign_level_2_1("campaign/l_2/level_2_1.png", Texture.class),
    campaign_level_2_2("campaign/l_2/level_2_2.png", Texture.class),
    campaign_level_2_3("campaign/l_2/level_2_3.png", Texture.class),
    campaign_level_2_4("campaign/l_2/level_2_4.png", Texture.class),
    campaign_level_3_0("campaign/l_3/level_3_0.png", Texture.class),
    campaign_level_3_1("campaign/l_3/level_3_1.png", Texture.class),
    campaign_level_3_2("campaign/l_3/level_3_2.png", Texture.class),
    campaign_level_3_3("campaign/l_3/level_3_3.png", Texture.class),
    campaign_level_3_4("campaign/l_3/level_3_4.png", Texture.class),
    campaign_level_4_0("campaign/l_4/level_4_0.png", Texture.class),
    campaign_level_4_1("campaign/l_4/level_4_1.png", Texture.class),
    campaign_level_4_2("campaign/l_4/level_4_2.png", Texture.class),
    campaign_level_4_3("campaign/l_4/level_4_3.png", Texture.class),
    campaign_level_4_4("campaign/l_4/level_4_4.png", Texture.class),
    campaign_level_5_0("campaign/l_5/level_5_0.png", Texture.class),
    campaign_level_5_1("campaign/l_5/level_5_1.png", Texture.class),
    campaign_level_5_2("campaign/l_5/level_5_2.png", Texture.class),
    campaign_level_5_3("campaign/l_5/level_5_3.png", Texture.class),
    campaign_level_5_4("campaign/l_5/level_5_4.png", Texture.class),
    campaign_level_0_background("campaign/background/level_0_background.png", Texture.class),
    campaign_level_1_background("campaign/background/level_1_background.png", Texture.class),
    campaign_level_2_background("campaign/background/level_2_background.png", Texture.class),
    campaign_level_3_background("campaign/background/level_3_background.png", Texture.class),
    campaign_level_4_background("campaign/background/level_4_background.png", Texture.class),
    campaign_level_5_background("campaign/background/level_5_background.png", Texture.class),
    btn_campaign_disabled("campaign/buttons/btn_campaign_disabled.png", Texture.class),
    btn_campaign_wall_up("campaign/buttons/btn_campaign_wall_up.png", Texture.class),
    btn_campaign_wall_down("campaign/buttons/btn_campaign_wall_down.png", Texture.class),
    btn_campaign_0_down("campaign/buttons/btn_0_down.png", Texture.class),
    btn_campaign_0_up("campaign/buttons/btn_0_up.png", Texture.class),
    btn_campaign_1_down("campaign/buttons/btn_1_down.png", Texture.class),
    btn_campaign_1_up("campaign/buttons/btn_1_up.png", Texture.class),
    btn_campaign_2_down("campaign/buttons/btn_2_down.png", Texture.class),
    btn_campaign_2_up("campaign/buttons/btn_2_up.png", Texture.class),
    btn_campaign_3_down("campaign/buttons/btn_3_down.png", Texture.class),
    btn_campaign_3_up("campaign/buttons/btn_3_up.png", Texture.class),
    btn_campaign_4_down("campaign/buttons/btn_4_down.png", Texture.class),
    btn_campaign_4_up("campaign/buttons/btn_4_up.png", Texture.class),
    btn_campaign_5_down("campaign/buttons/btn_5_down.png", Texture.class),
    btn_campaign_5_up("campaign/buttons/btn_5_up.png", Texture.class),
    sun("sun.png", Texture.class),

    bomb("campaign/bomb.png", Texture.class),
    fire("campaign/fire.png", Texture.class),
    campaign_wall_explosion("campaign/campaign_wall_explosion.png", Texture.class),
    table_left_right_dotted_line("campaign/connectinglines/table_left_right_dotted_line.png", Texture.class),
    table_right_left_dotted_line("campaign/connectinglines/table_right_left_dotted_line.png", Texture.class),
    table_down_center_left_dotted_line("campaign/connectinglines/table_down_center_left_dotted_line.png", Texture.class),
    table_up_center_left_dotted_line("campaign/connectinglines/table_up_center_left_dotted_line.png", Texture.class),
    table_center_right_dotted_line("campaign/connectinglines/table_center_right_dotted_line.png", Texture.class),

    btn_hangman_correct("buttons/btn_hangman_correct.png", Texture.class),
    btn_hangman_wrong("buttons/btn_hangman_wrong.png", Texture.class),
    btn_hangman_up("buttons/btn_hangman_up.png", Texture.class),
    btn_hangman_down("buttons/btn_hangman_down.png", Texture.class),
    btn_hint("buttons/btn_hint.png", Texture.class),
    btn_hint_disabled("buttons/btn_hint_disabled.png", Texture.class),
    correctq("correctq.png", Texture.class),
    unkownq("unkownq.png", Texture.class),
    wrongq("wrongq.png", Texture.class),

    btn_campaign("buttons/mainmenu/btn_campaign.png", Texture.class),
    btn_background_circle_up("buttons/background_circle/btn_background_circle_up.png", Texture.class),
    btn_background_circle_down("buttons/background_circle/btn_background_circle_down.png", Texture.class),

    ;
    // @formatter:on

    private String path;
    private Class<?> classType;

    HangmanArenaSpecificResource(String path, Class<?> classType) {
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
