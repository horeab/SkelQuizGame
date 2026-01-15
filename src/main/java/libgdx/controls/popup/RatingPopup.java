package libgdx.controls.popup;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.RateAppButtonBuilder;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.screen.AbstractScreen;
import libgdx.screen.AbstractScreenManager;

public abstract class RatingPopup extends MyPopup<AbstractScreen, AbstractScreenManager> {

    public RatingPopup(AbstractScreen abstractScreen) {
        super(abstractScreen);
    }

    @Override
    public void addButtons() {
        MyButton rateLaterBtn = new RateAppButtonBuilder(getScreen()).rateLaterButton().build();
        rateLaterBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });
        addExtraButtons();
        addButton(new RateAppButtonBuilder(getScreen()).rateNowButton().build());
        addButton(rateLaterBtn);
    }

    protected abstract void addExtraButtons();

    @Override
    protected String getLabelText() {
        return MainGameLabel.rate_message.getText();
    }


}
