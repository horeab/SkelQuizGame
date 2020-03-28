package libgdx.screens.implementations.hangmanarena.spec;

import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.labelimage.LabelImage;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.hangmanarena.HangmanArenaSpecificResource;
import libgdx.implementations.skelgame.GameButtonSize;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.GameLabel;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.screen.AbstractScreen;

public class RoundButtonBuilder extends ButtonBuilder {

    private Res icon;
    private GameLabel text;
    private AbstractScreen screen;
    private boolean withSparkle;
    private float fontDimen = FontManager.getNormalFontDim();

    public RoundButtonBuilder(AbstractScreen screen) {
        this.screen = screen;
    }

    public ButtonBuilder setFontDimen(float fontDimen) {
        this.fontDimen = fontDimen;
        return this;
    }

    public RoundButtonBuilder setCampaignButton() {
        this.icon = HangmanArenaSpecificResource.sun;
        withSparkle = true;
        setup(MainGameLabel.l_play);
        return this;
    }


    private void setup(GameLabel text) {
        this.text = text;
        setButtonSkin(GameButtonSkin.HANGMANARENA_BACKGROUND_CIRCLE);
        setFixedButtonSize(GameButtonSize.BIG_MENU_ROUND_IMAGE);
    }

    @Override
    public MyButton build() {
        Table table = new Table();
        float horizontalGeneralMarginDimen = MainDimen.horizontal_general_margin.getDimen();
        float iconDimen = horizontalGeneralMarginDimen * 9;
        Stack iconWithSparkle = new Stack();
        iconWithSparkle.addActor(GraphicUtils.getImage(icon));
        Table iconTable = new Table();
        iconTable.add(iconWithSparkle)
                .width(iconDimen)
                .height(iconDimen);
        table.add(iconTable).row();
        LabelImage textTable = createTextTable(text.getText(), horizontalGeneralMarginDimen * 17, fontDimen);
        textTable.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
        table.add(textTable).padTop(MainDimen.vertical_general_margin.getDimen() / 2);
        addCenterTextImageColumn(table);
        return super.build();
    }

}
