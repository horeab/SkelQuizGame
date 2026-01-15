package libgdx.utils;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import libgdx.controls.button.MainButtonSize;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.SettingsIconButtonBuilder;
import libgdx.controls.popup.MyPopup;
import libgdx.screen.AbstractScreen;

public class SettingsUtils {

    private Table soundMusicButtonsTable;

    public void setSoundMusicButtonsTable(Table soundMusicButtonsTable) {
        this.soundMusicButtonsTable = soundMusicButtonsTable;
    }

    public void addSettingsTable(final AbstractScreen screen) {
        Table table = new Table();
        MyButton settings = new SettingsIconButtonBuilder().createSettingsButton();
        table.add(settings).width(settings.getWidth()).height(settings.getHeight());
        float x = ScreenDimensionsManager.getScreenWidth() - MainButtonSize.SETTINGS_BUTTON.getWidth() / 1.5f;
        table.setPosition(x, MainButtonSize.SETTINGS_BUTTON.getHeight());
        settings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MyPopup settingsPopup = getSettingsPopup(screen).addToPopupManager();
                if (soundMusicButtonsTable != null) {
                    settingsPopup.addEmptyRowWithMargin(settingsPopup.getContentTable());
                    settingsPopup.getContentTable().add(soundMusicButtonsTable);
                    settingsPopup.addEmptyRowWithMargin(settingsPopup.getContentTable());
                }
            }
        });
        screen.addActor(table);
    }

    private MyPopup getSettingsPopup(AbstractScreen screen) {
        return new MyPopup(screen) {
            @Override
            protected String getLabelText() {
                return "";
            }

            @Override
            protected void addButtons() {
            }

            @Override
            public float getPrefWidth() {
                return ScreenDimensionsManager.getScreenWidth() / 2;
            }
        };
    }

}
