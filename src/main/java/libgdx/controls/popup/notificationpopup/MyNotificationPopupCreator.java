package libgdx.controls.popup.notificationpopup;

import org.apache.commons.lang3.StringUtils;

import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.ActorPositionManager;
import libgdx.utils.ScreenDimensionsManager;


public class MyNotificationPopupCreator {

    protected MyNotificationPopupConfig config;

    public MyNotificationPopupCreator(MyNotificationPopupConfig config) {
        this.config = config;
    }

    public ShortNotificationPopup shortNotificationPopup() {
        ShortNotificationPopup popup = new ShortNotificationPopup(config);
        setAttributes(popup, config);
        return popup;
    }

    public PositionNotificationPopup positionNotificationPopup(float x, float y) {
        PositionNotificationPopup popup = new PositionNotificationPopup(x, y, config);
        config.setPopupWidth(config.getPopupWidth() / 2);
        setAttributes(popup, config);
        return popup;
    }

    protected void setAttributes(MyNotificationPopup popup, MyNotificationPopupConfig config) {
        float popupWidth = config.getPopupWidth();
        if (config.getContentTable() != null) {
            popup.add(config.getContentTable());
            popupWidth = config.getContentTable().getPrefWidth();
        } else {
            if (config.getResource() != null) {
                popup.add(GraphicUtils.getImage(config.getResource())).width(config.getImageDimen()).height(config.getImageDimen());
            }
            if (StringUtils.isNotBlank(config.getText())) {
                MyWrappedLabelConfigBuilder myWrappedLabelConfigBuilder = new MyWrappedLabelConfigBuilder().setText(config.getText()).setWidth(config.getResource() != null ? popupWidth - config.getImageDimen() : popupWidth);
                if (config.getTextColor() != null) {
                    myWrappedLabelConfigBuilder.setFontColor(config.getTextColor());
                }
                if (config.getFontScale() != null) {
                    myWrappedLabelConfigBuilder.setFontScale(config.getFontScale());
                }
                if (config.getFontConfig() != null) {
                    myWrappedLabelConfigBuilder.setFontConfig(config.getFontConfig());
                }
                popup.add(new MyWrappedLabel(myWrappedLabelConfigBuilder.build()));
            }
        }
        popup.setWidth(popupWidth + MainDimen.horizontal_general_margin.getDimen() * 2);
        popup.setHeight(popup.getPrefHeight() + MainDimen.vertical_general_margin.getDimen());
        ActorPositionManager.setActorCenterScreen(popup);
        popup.setY(ScreenDimensionsManager.getScreenHeight(10));
    }
}
