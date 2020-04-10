package libgdx.implementations.skelgame.gameservice;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.resources.FontManager;
import libgdx.utils.model.FontColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameControlsService {

    private Map<String, MyButton> allAnswerButtons;
    private List<HintButton> hintButtons;

    public GameControlsService(Map<String, MyButton> allAnswerButtons, List<HintButton> hintButtons) {
        this.allAnswerButtons = allAnswerButtons;
        this.hintButtons = hintButtons;
    }

    public void disableButton(MyButton btn) {
        if(btn==null){
            int i=0;
        }
        if (!btn.isDisabled()) {
            btn.setDisabled(true);
        }
    }

    public void disableAllControls() {
        for (MyButton button : allAnswerButtons.values()) {
            disableButton(button);
        }
        disableAllHintButtons();
    }

    public void disableAllHintButtons() {
        for (HintButton hintButton : hintButtons) {
            //stop animation
            hintButton.getMyButton().setTransform(false);
            hintButton.getMyButton().setDisabled(true);
        }
    }

    public void disableTouchableAllControls() {
        processTouchableControls(Touchable.disabled);
    }

    public void enableTouchableAllControls() {
        processTouchableControls(Touchable.enabled);
    }

    private void processTouchableControls(Touchable touchable) {
        List<MyButton> buttonsToProcess = new ArrayList<>(allAnswerButtons.values());
        for (HintButton hintButton : hintButtons) {
            buttonsToProcess.add(hintButton.getMyButton());
        }
        FontColor fontColor = FontManager.getBaseColorForContrast();
        for (MyButton button : buttonsToProcess) {
            List<MyLabel> centerRowLabels = button.getCenterRowLabels();
            for (MyLabel label : centerRowLabels) {
                if (touchable == Touchable.disabled) {
                    fontColor = button.getButtonSkin().getButtonDisabledFontColor() != null ? button.getButtonSkin().getButtonDisabledFontColor() : FontManager.getBaseColorForContrast();
                }
                label.getStyle().font = Game.getInstance().getFontManager().getFont(fontColor);
            }
            button.setTouchable(touchable);
        }
    }
}
