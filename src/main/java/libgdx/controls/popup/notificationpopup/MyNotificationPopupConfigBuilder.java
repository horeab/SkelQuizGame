package libgdx.controls.popup.notificationpopup;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;


public class MyNotificationPopupConfigBuilder {

    private String text;
    private FontConfig fontConfig;
    private FontColor textColor;
    private Float fontScale;
    private Boolean transferBetweenScreens = true;
    private Res resource;
    private Float imageDimen = MainDimen.side_notification_popup_icon.getDimen();
    private Float popupWidth = ScreenDimensionsManager.getScreenWidth(70);
    private Table contentTable;


    public MyNotificationPopupConfigBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public MyNotificationPopupConfigBuilder setTransferBetweenScreens(Boolean transferBetweenScreens) {
        this.transferBetweenScreens = transferBetweenScreens;
        return this;
    }

    public MyNotificationPopupConfigBuilder setFontConfig(FontConfig fontConfig) {
        this.fontConfig = fontConfig;
        return this;
    }

    public MyNotificationPopupConfigBuilder setFontScale(float fontScale) {
        this.fontScale = fontScale;
        return this;
    }

    public MyNotificationPopupConfigBuilder setTextColor(FontColor textColor) {
        this.textColor = textColor;
        return this;
    }

    public MyNotificationPopupConfigBuilder setResource(Res resource) {
        this.resource = resource;
        return this;
    }


    public MyNotificationPopupConfigBuilder setImageDimen(Float imageDimen) {
        this.imageDimen = imageDimen;
        return this;
    }


    public MyNotificationPopupConfigBuilder setPopupWidth(Float popupWidth) {
        this.popupWidth = popupWidth;
        return this;
    }


    public MyNotificationPopupConfigBuilder setContentTable(Table contentTable) {
        this.contentTable = contentTable;
        return this;
    }

    public MyNotificationPopupConfig build() {
        MyNotificationPopupConfig config = new MyNotificationPopupConfig();
        config.setText(text);
        config.setTextColor(textColor);
        config.setFontScale(fontScale);
        config.setTransferBetweenScreens(transferBetweenScreens);
        config.setFontConfig(fontConfig);
        config.setResource(resource);
        config.setImageDimen(imageDimen);
        config.setPopupWidth(popupWidth);
        config.setContentTable(contentTable);
        return config;
    }
}
