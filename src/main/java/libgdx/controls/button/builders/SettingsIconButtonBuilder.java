package libgdx.controls.button.builders;

import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MainButtonSize;
import libgdx.controls.button.MainButtonSkin;
import libgdx.controls.button.MyButton;

public class SettingsIconButtonBuilder {


    public MyButton createSettingsButton() {
        return new ButtonBuilder("")
                .setButtonSkin(MainButtonSkin.SETTINGS)
                .setFixedButtonSize(MainButtonSize.SETTINGS_BUTTON)
                .build();
    }

}
