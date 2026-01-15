package libgdx.controls.button.builders;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import org.apache.commons.lang3.StringUtils;

import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.ButtonSkin;
import libgdx.controls.button.MainButtonSize;
import libgdx.controls.button.MyButton;
import libgdx.controls.labelimage.LabelImage;
import libgdx.graphics.GraphicUtils;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;

public class ImageButtonBuilder extends ButtonBuilder {

    private boolean animateFadeInFadeOut;
    private boolean animateZoomInZoomOut;
    private float animateZoomInZoomOutAmount = 0.2f;
    private float padBetweenImageAndText = 1.6f;
    private Res textBackground = MainResource.popup_background;
    private AbstractScreen screen;
    private Float textButtonWidth;

    public ImageButtonBuilder(ButtonSkin buttonSkin, AbstractScreen screen) {
        setButtonSkin(buttonSkin);
        setFixedButtonSize(MainButtonSize.STANDARD_IMAGE);
        this.screen = screen;
    }

    public ImageButtonBuilder animateFadeInFadeOut() {
        this.animateFadeInFadeOut = true;
        return this;
    }

    public ImageButtonBuilder animateZoomInZoomOut() {
        animateZoomInZoomOut(true);
        return this;
    }

    public ImageButtonBuilder animateZoomInZoomOut(boolean animateZoomInZoomOut) {
        this.animateZoomInZoomOut = animateZoomInZoomOut;
        return this;
    }

    public ImageButtonBuilder padBetweenImageAndText(float padBetweenImageAndText) {
        this.padBetweenImageAndText = padBetweenImageAndText;
        return this;
    }

    public ImageButtonBuilder setAnimateZoomInZoomOutAmount(float animateZoomInZoomOutAmount) {
        this.animateZoomInZoomOutAmount = animateZoomInZoomOutAmount;
        return this;
    }

    public ImageButtonBuilder textBackground(Res textBackground) {
        this.textBackground = textBackground;
        return this;
    }

    public ImageButtonBuilder textButtonWidth(float textButtonWidth) {
        this.textButtonWidth = textButtonWidth;
        return this;
    }

    public ImageButtonBuilder setText(String text) {
        if (StringUtils.isNotBlank(text)) {
            float fontScale = this.fontScale != null ? this.fontScale : FontManager.calculateMultiplierStandardFontSize(0.7f);
            LabelImage textTable = createTextTable(text, textButtonWidth == null ? MainDimen.horizontal_general_margin.getDimen() * 15 : textButtonWidth, fontScale);
            if (textBackground != null) {
                textTable.setBackground(GraphicUtils.getNinePatch(textBackground));
            }
            addCenterTextImageColumn(textTable);
        }
        return this;
    }

    public ImageButtonBuilder addFadeOutOnClick() {
        addChangeListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                actor.setTouchable(Touchable.disabled);
                actor.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.hide()));
            }
        });
        return this;
    }

    @Override
    public MyButton build() {
        MyButton button = super.build();
        if (animateFadeInFadeOut) {
            new ActorAnimation(screen).animateFadeInFadeOut(button);
        }
        if (animateZoomInZoomOut) {
            button.setTransform(true);
            new ActorAnimation(screen).animateZoomInZoomOut(button, animateZoomInZoomOutAmount);
        }
        Table centerRow = button.getCenterRow();
        if (centerRow != null) {
            centerRow.padTop(button.getHeight() * padBetweenImageAndText);
        }
        return button;
    }
}
