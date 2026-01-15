package libgdx.controls;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.List;

import libgdx.controls.label.MyLabel;

public abstract class TextTable extends Table {

    public abstract String getText();

    public abstract List<MyLabel> getLabels();
}
