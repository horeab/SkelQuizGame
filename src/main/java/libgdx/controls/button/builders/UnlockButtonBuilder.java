package libgdx.controls.button.builders;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.MainButtonSize;
import libgdx.controls.button.MainButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.game.Game;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;

public class UnlockButtonBuilder {

    private MyButton createScreenUnlockButton(final AbstractScreen screen, String defaultLanguage, String defaultText, final Runnable afterBuy) {
        MyButton button = new ImageButtonBuilder(MainButtonSkin.UNLOCK_EXTRA_CONTENT, screen)
                .setFontColor(FontColor.BLACK)
                .setFixedButtonSize(MainButtonSize.UNLOCK_CONTENT_BUTTON)
                .build();
        button.setTransform(true);
        new ActorAnimation(Game.getInstance().getAbstractScreen()).animateZoomInZoomOut(button);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                displayInAppPurchasesPopup(defaultLanguage, defaultText, afterBuy);
            }
        });
        return button;
    }

    public MyButton addHoverUnlockButton(AbstractScreen screen, String defaultLanguage, String defaultText, Runnable afterBuy) {
        MyButton unlockBtn = createScreenUnlockButton(screen, defaultLanguage, defaultText, afterBuy);
        unlockBtn.setPosition(
                ScreenDimensionsManager.getScreenWidth() - MainDimen.horizontal_general_margin.getDimen() * 1.5f - MainButtonSize.UNLOCK_CONTENT_BUTTON.getWidth(),
                ScreenDimensionsManager.getScreenHeight() - MainButtonSize.UNLOCK_CONTENT_BUTTON.getHeight() - MainDimen.vertical_general_margin.getDimen());
        screen.addActor(unlockBtn);
        return unlockBtn;
    }

    private void displayInAppPurchasesPopup(String defaultLanguage, String defaultText, final Runnable afterBuy) {
        Game.getInstance().getInAppPurchaseManager().displayInAppPurchasesPopup(defaultLanguage, defaultText, new Runnable() {
            @Override
            public void run() {
                afterBuy.run();
            }
        });
    }
}
