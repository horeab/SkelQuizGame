package libgdx.implementations.skelgame.gameservice;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyLabel;
import libgdx.game.Game;
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
        processTouchableControls(FontColor.GRAY, Touchable.disabled);
    }

    public void enableTouchableAllControls() {
        processTouchableControls(FontColor.BLACK, Touchable.enabled);
    }

    private void processTouchableControls(FontColor fontColor, Touchable touchable) {
        List<MyButton> buttonsToProcess = new ArrayList<>(allAnswerButtons.values());
        for (HintButton hintButton : hintButtons) {
            buttonsToProcess.add(hintButton.getMyButton());
        }
        for (MyButton button : buttonsToProcess) {
            List<MyLabel> centerRowLabels = button.getCenterRowLabels();
            for (MyLabel label : centerRowLabels) {
                if (!button.isDisabled()) {
                    label.getStyle().font= Game.getInstance().getFontManager().getFont(fontColor);
                }
            }
            button.setTouchable(touchable);
        }
    }
}
