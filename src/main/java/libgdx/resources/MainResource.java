package libgdx.resources;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;

import libgdx.game.Game;

public enum MainResource implements Res {

    skin("skin/skin.json", Skin.class),
    font_arial("fonts/font_arial.ttf", null),
    font_hi("fonts/font_hi.ttf", null),
    font_ko("fonts/font_ko.ttf", null),
    font_th("fonts/font_th.ttf", null),
    font_zh("fonts/font_zh.ttf", null),
    textureAtlas("skin/skin.atlas", TextureAtlas.class),

    inappurchase_background("backgrounds/inappurchase_background.png", Texture.class),
    popup_background("backgrounds/popup_background.png", Texture.class),
    red_background("backgrounds/red_background.png", Texture.class),
    green_background("backgrounds/green_background.png", Texture.class),
    transparent_background("backgrounds/transparent_background.png", Texture.class),
    white_full_background("backgrounds/white_full_background.png", Texture.class),

    background_texture("general/background_texture.png", Texture.class),
    splashtitle("general/splashtitle.png", Texture.class),
    remove("general/remove.png", Texture.class),
    magnify_glass("general/magnify_glass.png", Texture.class),
    error("general/error.png", Texture.class),
    play("general/play.png", Texture.class),
    skip("general/skip.png", Texture.class),

    btn_lowcolor_down("buttons/btn_lowcolor_down.png", Texture.class),
    btn_lowcolor_up("buttons/btn_lowcolor_up.png", Texture.class),
    btn_menu_down("buttons/btn_menu_down.png", Texture.class),
    btn_menu_up("buttons/btn_menu_up.png", Texture.class),
    btn_settings_up("buttons/btn_settings_up.png", Texture.class),
    btn_settings_down("buttons/btn_settings_down.png", Texture.class),
    btn_back_up("buttons/btn_back_up.png", Texture.class),
    btn_back_down("buttons/btn_back_down.png", Texture.class),
    btn_back_up_white("buttons/btn_back_up_white.png", Texture.class),
    btn_back_down_white("buttons/btn_back_down_white.png", Texture.class),
    refresh_down("buttons/refresh_down.png", Texture.class),
    refresh_up("buttons/refresh_up.png", Texture.class),

    crown("general/crown.png", Texture.class),
    mug("general/mug.png", Texture.class),
    mug_black_border("general/mug_black_border.png", Texture.class),
    mug_color("general/mug_color.png", Texture.class),
    press_finger("general/press_finger.png", Texture.class),
    heart_full("general/heart_full.png", Texture.class),
    lock("general/lock.png", Texture.class),
    question("general/question.png", Texture.class),
    unlock("general/unlock.png", Texture.class),
    sound_on("general/sound_on.png", Texture.class),
    sound_off("general/sound_off.png", Texture.class),
    music_on("general/music_on.png", Texture.class),
    music_off("general/music_off.png", Texture.class),
    circle_red("general/circle_red.png", Texture.class),
    square_red("general/square_red.png", Texture.class),
    triangle_red("general/triangle_red.png", Texture.class),

    main_labels_cs("labels/main_labels_cs", I18NBundle.class),
    main_labels_da("labels/main_labels_da", I18NBundle.class),
    main_labels_de("labels/main_labels_de", I18NBundle.class),
    main_labels_el("labels/main_labels_el", I18NBundle.class),
    main_labels_en("labels/main_labels_en", I18NBundle.class),
    main_labels_es("labels/main_labels_es", I18NBundle.class),
    main_labels_fi("labels/main_labels_fi", I18NBundle.class),
    main_labels_fr("labels/main_labels_fr", I18NBundle.class),
    main_labels_hi("labels/main_labels_hi", I18NBundle.class),
    main_labels_hr("labels/main_labels_hr", I18NBundle.class),
    main_labels_hu("labels/main_labels_hu", I18NBundle.class),
    main_labels_id("labels/main_labels_id", I18NBundle.class),
    main_labels_it("labels/main_labels_it", I18NBundle.class),
    main_labels_ja("labels/main_labels_ja", I18NBundle.class),
    main_labels_ko("labels/main_labels_ko", I18NBundle.class),
    main_labels_ms("labels/main_labels_ms", I18NBundle.class),
    main_labels_nl("labels/main_labels_nl", I18NBundle.class),
    main_labels_no("labels/main_labels_no", I18NBundle.class),
    main_labels_pl("labels/main_labels_pl", I18NBundle.class),
    main_labels_pt("labels/main_labels_pt", I18NBundle.class),
    main_labels_ro("labels/main_labels_ro", I18NBundle.class),
    main_labels_ru("labels/main_labels_ru", I18NBundle.class),
    main_labels_sk("labels/main_labels_sk", I18NBundle.class),
    main_labels_sv("labels/main_labels_sv", I18NBundle.class),
    main_labels_th("labels/main_labels_th", I18NBundle.class),
    main_labels_tr("labels/main_labels_tr", I18NBundle.class),
    main_labels_uk("labels/main_labels_uk", I18NBundle.class),
    main_labels_vi("labels/main_labels_vi", I18NBundle.class),
    main_labels_zh("labels/main_labels_zh", I18NBundle.class),;

    private String path;
    private Class<?> classType;

    MainResource(String path, Class<?> classType) {
        this.path = path;
        this.classType = classType;
    }

    @Override
    public Class<?> getClassType() {
        return classType;
    }

    @Override
    public String getPath() {
        return Game.getInstance().getAppInfoService().getMainResourcesFolder() + path;
    }
}
