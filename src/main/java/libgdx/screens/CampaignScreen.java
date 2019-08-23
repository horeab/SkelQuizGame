package libgdx.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.campaign.*;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.skelgame.QuizGame;
import libgdx.resources.FontManager;
import libgdx.resources.Resource;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.EnumUtils;
import libgdx.utils.ScreenDimensionsManager;

import java.util.List;

public class CampaignScreen extends AbstractScreen<QuizScreenManager> {

    private CampaignService campaignService = new CampaignService();
    private int NR_OF_LEVELS = 4;

    @Override
    public void buildStage() {
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

}
