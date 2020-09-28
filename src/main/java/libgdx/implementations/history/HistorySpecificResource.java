package libgdx.implementations.history;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.I18NBundle;

import libgdx.game.Game;
import libgdx.resources.SpecificResource;
import sun.net.www.content.image.png;

public enum HistorySpecificResource implements SpecificResource {


    // @formatter:off
    specific_labels("labels/labels", I18NBundle.class),

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

    table_left_right_dotted_line("campaign/connectinglines/table_left_right_dotted_line.png", Texture.class),
    table_right_left_dotted_line("campaign/connectinglines/table_right_left_dotted_line.png", Texture.class),
    table_down_center_left_dotted_line("campaign/connectinglines/table_down_center_left_dotted_line.png", Texture.class),
    table_up_center_left_dotted_line("campaign/connectinglines/table_up_center_left_dotted_line.png", Texture.class),
    table_center_right_dotted_line("campaign/connectinglines/table_center_right_dotted_line.png", Texture.class),

    campaign_level_0_background("campaign/background/level_0_background.png", Texture.class),
    campaign_level_1_background("campaign/background/level_1_background.png", Texture.class),
    campaign_level_2_background("campaign/background/level_2_background.png", Texture.class),
    campaign_level_3_background("campaign/background/level_3_background.png", Texture.class),
    campaign_level_4_background("campaign/background/level_4_background.png", Texture.class),
    campaign_level_5_background("campaign/background/level_5_background.png", Texture.class),

    arrow_left("arrow_left.png", Texture.class),
    arrow_right("arrow_right.png", Texture.class),
    timeline0_line_background("timeline0_line_background.png", Texture.class),
    timeline1_line_background("timeline1_line_background.png", Texture.class),
    timeline2_line_background("timeline2_line_background.png", Texture.class),
    timeline3_line_background("timeline3_line_background.png", Texture.class),
    timeline0_opt_background("timeline0_opt_background.png", Texture.class),
    timeline1_opt_background("timeline1_opt_background.png", Texture.class),
    timeline2_opt_background("timeline2_opt_background.png", Texture.class),
    timeline3_opt_background("timeline3_opt_background.png", Texture.class),
    timeline_opt_unknown("timeline_opt_unknown.png", Texture.class),
    timeline_opt_correct("timeline_opt_correct.png", Texture.class),
    timeline_opt_wrong("timeline_opt_wrong.png", Texture.class),
    score_icon("score_icon.png", Texture.class),
    title_clouds_background("title_clouds_background.png", Texture.class),

    i0("questions/images/i0.png", Texture.class),
    i1("questions/images/i1.png", Texture.class),
    ;

    // @formatter:on

    private String path;
    private Class<?> classType;

    HistorySpecificResource(String path, Class<?> classType) {
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
