package libgdx.controls;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import libgdx.game.Game;
import libgdx.resources.ResourcesManager;

public class MyTextField extends Table {

    TextField textField = new TextField("", ResourcesManager.getSkin());

    public MyTextField() {
        textField.getStyle().font= Game.getInstance().getFontManager().getFont();
        textField.setOnlyFontChars(true);
    }

    public TextField getTextField() {
        return textField;
    }

    public String getText(){
        return textField.getText();
    }
}