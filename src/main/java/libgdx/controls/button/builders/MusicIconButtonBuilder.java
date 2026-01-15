package libgdx.controls.button.builders;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MainButtonSize;
import libgdx.controls.button.MainButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.preferences.SettingsService;
import libgdx.resources.Res;
import libgdx.utils.SoundUtils;

public class MusicIconButtonBuilder {

    private SettingsService settingsService = new SettingsService();

    public MyButton createMusicButton(Res music) {
        final MyButton button = new ButtonBuilder("")
                .setButtonSkin(getButtonSkin())
                .setFixedButtonSize(MainButtonSize.SOUND_BUTTON)
                .build();
        button.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settingsService.toggleMusic();
                button.setButtonSkin(getButtonSkin());
                SoundUtils.playMusic(music);
            }
        });
        return button;
    }

    private MainButtonSkin getButtonSkin() {
        return settingsService.isMusicOn() ? MainButtonSkin.MUSIC_ON : MainButtonSkin.MUSIC_OFF;
    }
}
