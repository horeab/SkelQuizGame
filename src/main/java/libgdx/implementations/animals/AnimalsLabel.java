package libgdx.implementations.animals;


import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.utils.model.FontColor;

public class AnimalsLabel extends Table {

    private MyWrappedLabel label;

    public AnimalsLabel(String text) {
        super();
        label = new MyWrappedLabel(text);
        add(label);
    }

    public void setFontScale(float fontScale) {
        label.setFontScale(fontScale);
    }

    public void setRedColor() {
        label.setTextColor(FontColor.RED);
    }

    public void setGreenColor() {
        label.setTextColor(FontColor.GREEN);
    }

}
