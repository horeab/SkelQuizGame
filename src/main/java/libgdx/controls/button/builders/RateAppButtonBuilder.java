package libgdx.controls.button.builders;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import libgdx.constants.Contrast;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.popup.RatingService;
import libgdx.game.Game;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.screen.AbstractScreen;
import libgdx.utils.model.FontColor;

public class RateAppButtonBuilder extends ButtonBuilder {

    private static final float BTN_WIDTH = MainDimen.width_default_btn.getDimen() / 1.1f;
    private AbstractScreen screen;

    public RateAppButtonBuilder(AbstractScreen screen) {
        this.screen = screen;
    }

    public RateAppButtonBuilder rateNowButton() {
        setFontColor(FontColor.BLACK);
        setWrappedText(MainGameLabel.rate_rate_now.getText(), BTN_WIDTH);
        setDefaultButton();
        setContrast(Contrast.LIGHT);
        addClickListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                RatingService ratingService = Game.getInstance().getMainDependencyManager().createRatingService(screen);
                ratingService.rateNow();
                Game.getInstance().getScreenManager().showMainScreen();
            }
        });
        return this;
    }

    public RateAppButtonBuilder rateLaterButton() {
        setFontColor(FontColor.BLACK);
        setWrappedText(MainGameLabel.rate_rate_later.getText(), BTN_WIDTH);
        setLowColorPopupButton();
        return this;
    }

}
