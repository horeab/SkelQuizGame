package libgdx.controls.button.builders;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MainButtonSize;
import libgdx.controls.button.MainButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.preferences.SettingsService;

public class SoundIconButtonBuilder {

    private SettingsService settingsService = new SettingsService();

    public MyButton createSoundButton() {
        final MyButton button = new ButtonBuilder("")
                .setButtonSkin(getButtonSkin())
                .setFixedButtonSize(MainButtonSize.SOUND_BUTTON)
                .build();
        button.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settingsService.toggleSound();
                button.setButtonSkin(getButtonSkin());
            }
        });
        return button;
    }

    private MainButtonSkin getButtonSkin() {
        return settingsService.isSoundOn() ? MainButtonSkin.SOUND_ON : MainButtonSkin.SOUND_OFF;
    }
}
