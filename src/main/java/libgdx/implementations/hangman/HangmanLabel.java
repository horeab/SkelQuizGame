package libgdx.implementations.hangman;


import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.utils.model.FontColor;

public class HangmanLabel extends Table {

    private MyWrappedLabel label;

    public HangmanLabel(String text) {
        super();
        label = new MyWrappedLabel(text);
        add(label);
    }

    public void setFontScale(float fontScale) {
        label.setFontScale(fontScale);
    }

    public void setRedColor() {
        setRedColor(FontColor.YELLOW);
    }

    public void setRedColor(FontColor fontColor) {
        label.setTextColor(fontColor);
    }


    public void setGreenColor() {
        label.setTextColor(FontColor.LIGHT_BLUE);
    }

}
