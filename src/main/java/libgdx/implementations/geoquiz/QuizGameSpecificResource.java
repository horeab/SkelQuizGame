package libgdx.implementations.geoquiz;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.I18NBundle;
import libgdx.game.Game;
import libgdx.resources.SpecificResource;

public enum QuizGameSpecificResource implements SpecificResource {

    // @formatter:off

    btn_answer_opt_correct("buttons/btn_answer_opt_correct.png", Texture.class),
    btn_answer_opt_wrong("buttons/btn_answer_opt_wrong.png", Texture.class),
    btn_answer_opt_up("buttons/btn_answer_opt_up.png", Texture.class),
    btn_answer_opt_down("buttons/btn_answer_opt_down.png", Texture.class),
    btn_answer_opt_disabled("buttons/btn_answer_opt_disabled.png", Texture.class),
    btn_long_answer_opt_correct("buttons/btn_long_answer_opt_correct.png", Texture.class),
    btn_long_answer_opt_wrong("buttons/btn_long_answer_opt_wrong.png", Texture.class),
    btn_long_answer_opt_up("buttons/btn_long_answer_opt_up.png", Texture.class),
    btn_long_answer_opt_down("buttons/btn_long_answer_opt_down.png", Texture.class),
    btn_long_answer_opt_disabled("buttons/btn_long_answer_opt_disabled.png", Texture.class),
    btn_hint("buttons/btn_hint.png", Texture.class),
    btn_hint_disabled("buttons/btn_hint_disabled.png", Texture.class),
    green_backr("green_backr.png", Texture.class),
    title_backgr("title_backgr.png", Texture.class),
    red_backr("red_backgr.png", Texture.class),

    specific_labels("labels/labels", I18NBundle.class),


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

    ;
    // @formatter:on

    private String path;
    private Class<?> classType;

    QuizGameSpecificResource(String path, Class<?> classType) {
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
