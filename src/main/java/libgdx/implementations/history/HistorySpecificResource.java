package libgdx.implementations.history;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.I18NBundle;

import libgdx.game.Game;
import libgdx.resources.SpecificResource;
import sun.net.www.content.image.png;

public enum HistorySpecificResource implements SpecificResource {


    // @formatter:off
    specific_labels("labels/labels", I18NBundle.class),

    btn_timeline("buttons/btn_timeline.png", Texture.class),
    btn_timeline_clicked("buttons/btn_timeline_clicked.png", Texture.class),
    btn_timeline_disabled_correct("buttons/btn_timeline_disabled_correct.png", Texture.class),
    btn_timeline_disabled_wrong("buttons/btn_timeline_disabled_wrong.png", Texture.class),

    btn_great("buttons/btn_great.png", Texture.class),
    btn_great_clicked("buttons/btn_great_clicked.png", Texture.class),
    btn_great_disabled_correct("buttons/btn_great_disabled_correct.png", Texture.class),
    btn_great_disabled_wrong("buttons/btn_great_disabled_wrong.png", Texture.class),
    btn_categ0_up("buttons/btn_categ0_up.png", Texture.class),
    btn_categ1_up("buttons/btn_categ1_up.png", Texture.class),
    btn_categ0_down("buttons/btn_categ0_down.png", Texture.class),
    btn_categ1_down("buttons/btn_categ1_down.png", Texture.class),
    hist_btn_hint("buttons/hist_btn_hint.png", Texture.class),

    arrow_left("arrow_left.png", Texture.class),
    arrow_right("arrow_right.png", Texture.class),
    timeline0_line_background("timeline0_line_background.png", Texture.class),
    timeline1_line_background("timeline1_line_background.png", Texture.class),
    timeline2_line_background("timeline2_line_background.png", Texture.class),
    timeline3_line_background("timeline3_line_background.png", Texture.class),
    timeline0_opt_background("timeline0_opt_background.png", Texture.class),
    timeline0_opt_background_odd("timeline3_opt_background_odd.png", Texture.class),
    timeline1_opt_background("timeline1_opt_background.png", Texture.class),
    timeline1_opt_background_odd("timeline3_opt_background_odd.png", Texture.class),
    timeline2_opt_background("timeline2_opt_background.png", Texture.class),
    timeline2_opt_background_odd("timeline3_opt_background_odd.png", Texture.class),
    timeline3_opt_background("timeline3_opt_background.png", Texture.class),
    timeline3_opt_background_odd("timeline3_opt_background_odd.png", Texture.class),
    timeline_opt_unknown("timeline_opt_unknown.png", Texture.class),
    hist_answ_wrong("hist_answ_wrong.png", Texture.class),
    timeline_opt_correct("timeline_opt_correct.png", Texture.class),
    timeline_opt_wrong("timeline_opt_wrong.png", Texture.class),
    score_icon("score_icon.png", Texture.class),
    title_clouds_background("title_clouds_background.png", Texture.class),

    level_fail("sound/level_fail.mp3", Sound.class),
    level_success("sound/level_success.mp3", Sound.class),

    i0("questions/images/i0.png", Texture.class),
    i1("questions/images/i1.png", Texture.class),
    i2("questions/images/i2.png", Texture.class),
    i3("questions/images/i3.jpg", Texture.class),
    i4("questions/images/i4.png", Texture.class),
    i5("questions/images/i5.png", Texture.class),
    i6("questions/images/i6.png", Texture.class),
    i7("questions/images/i7.png", Texture.class),
    i8("questions/images/i8.jpg", Texture.class),
    i9("questions/images/i9.jpg", Texture.class),
    i10("questions/images/i10.jpeg", Texture.class),
    i11("questions/images/i11.png", Texture.class),
    i12("questions/images/i12.jpg", Texture.class),
    i13("questions/images/i13.png", Texture.class),
    i14("questions/images/i14.jpg", Texture.class),
    i15("questions/images/i15.png", Texture.class),
    i16("questions/images/i16.jpg", Texture.class),
    i17("questions/images/i17.png", Texture.class),
    i18("questions/images/i18.jpg", Texture.class),
    i19("questions/images/i19.jpg", Texture.class),
    i20("questions/images/i20.jpg", Texture.class),
    i21("questions/images/i21.png", Texture.class),
    i22("questions/images/i22.png", Texture.class),
    i23("questions/images/i23.jpg", Texture.class),
    i24("questions/images/i24.png", Texture.class),
    i25("questions/images/i25.jpg", Texture.class),
    i26("questions/images/i26.jpg", Texture.class),
    i27("questions/images/i27.jpg", Texture.class),
    i28("questions/images/i28.jpg", Texture.class),
    i29("questions/images/i29.jpg", Texture.class),
    i30("questions/images/i30.jpg", Texture.class),
    i31("questions/images/i31.jpg", Texture.class),
    i32("questions/images/i32.jpg", Texture.class),
    i33("questions/images/i33.jpg", Texture.class),
    i34("questions/images/i34.png", Texture.class),
    i35("questions/images/i35.png", Texture.class),
    i36("questions/images/i36.jpeg", Texture.class),
    i37("questions/images/i37.jpg", Texture.class),
    i38("questions/images/i38.png", Texture.class),
    i39("questions/images/i39.png", Texture.class),
    i40("questions/images/i40.png", Texture.class),
    i41("questions/images/i41.jpg", Texture.class),
    i42("questions/images/i42.jpg", Texture.class),
    i43("questions/images/i43.jpg", Texture.class),
    i44("questions/images/i44.jpg", Texture.class),

    j0("questions/images/j0.png", Texture.class),
    j1("questions/images/j1.png", Texture.class),
    j2("questions/images/j2.png", Texture.class),
    j3("questions/images/j3.png", Texture.class),
    j4("questions/images/j4.png", Texture.class),
    j5("questions/images/j5.png", Texture.class),
    j6("questions/images/j6.png", Texture.class),
    j7("questions/images/j7.png", Texture.class),
    j8("questions/images/j8.png", Texture.class),
    j9("questions/images/j9.png", Texture.class),
    j10("questions/images/j10.png", Texture.class),
    j11("questions/images/j11.png", Texture.class),
    j12("questions/images/j12.png", Texture.class),
    j13("questions/images/j13.png", Texture.class),
    j14("questions/images/j14.png", Texture.class),
    j15("questions/images/j15.png", Texture.class),
    j16("questions/images/j16.png", Texture.class),
    j17("questions/images/j17.png", Texture.class),
    j18("questions/images/j18.png", Texture.class),
    j19("questions/images/j19.png", Texture.class),
    j20("questions/images/j20.png", Texture.class),
    j21("questions/images/j21.png", Texture.class),
    j22("questions/images/j22.png", Texture.class),
    j23("questions/images/j23.png", Texture.class),
    j24("questions/images/j24.png", Texture.class),
    j25("questions/images/j25.png", Texture.class),
    j26("questions/images/j26.png", Texture.class),
    j27("questions/images/j27.png", Texture.class),
    j28("questions/images/j28.png", Texture.class),
    j29("questions/images/j29.jpg", Texture.class),
    j30("questions/images/j30.png", Texture.class),
    j31("questions/images/j31.jpg", Texture.class),
    j32("questions/images/j32.jpg", Texture.class),
    j33("questions/images/j33.jpg", Texture.class),
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
