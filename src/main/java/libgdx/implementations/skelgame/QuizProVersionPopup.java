package libgdx.implementations.skelgame;

import libgdx.controls.popup.ProVersionPopup;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.geoquiz.QuizGame;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.screen.AbstractScreen;

public class QuizProVersionPopup extends ProVersionPopup {

    public QuizProVersionPopup(AbstractScreen abstractScreen) {
        super(abstractScreen);
    }

    @Override
    protected String getLabelText() {
        return MainGameLabel.pro_version_info.getText(Game.getInstance().getAppInfoService().getAppName()) + "\n+" +
                MainGameLabel.pro_version_info_unlock.getText(Game.getInstance().getAppInfoService().getAppName());
    }

    @Override
    protected void addText() {
        float dimen = MainDimen.horizontal_general_margin.getDimen() * 15;
        getContentTable().add(GraphicUtils.getImage(MainResource.sound_off)).height(dimen).width(dimen);
        super.addText();
    }

    @Override
    public void hide() {
        super.hide();
        QuizGame.getInstance().getScreenManager().showMainScreen();
    }
}
