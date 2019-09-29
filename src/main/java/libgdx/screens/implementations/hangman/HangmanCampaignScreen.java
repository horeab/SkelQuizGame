package libgdx.screens.implementations.hangman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import libgdx.campaign.*;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.ButtonSkin;
import libgdx.controls.button.MainButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.implementations.hangman.HangmanCampaignLevelEnum;
import libgdx.implementations.hangman.HangmanGame;
import libgdx.implementations.hangman.HangmanQuestionCategoryEnum;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.screen.AbstractScreen;
import libgdx.screens.implementations.geoquiz.GeoQuizCampaignScreen;
import libgdx.screens.implementations.geoquiz.QuizScreenManager;
import libgdx.utils.ScreenDimensionsManager;

import java.util.List;

public class HangmanCampaignScreen extends AbstractScreen<QuizScreenManager> {

    private CampaignService campaignService = new CampaignService();
    private List<CampaignStoreLevel> allCampaignLevelStores;

    public HangmanCampaignScreen() {
        allCampaignLevelStores = campaignService.getFinishedCampaignLevels();
    }

    @Override
    public void buildStage() {
        Table table = new Table();
        table.setFillParent(true);
        table.add(createAllTable());
        addActor(table);
    }

    private Table createAllTable() {
        Table table = new Table();
        int totalCat = HangmanQuestionCategoryEnum.values().length;
        for (int i = 0; i < totalCat; i++) {
            if (i > 0 && i % 2 == 0) {
                table.row();
            }
            final int maxLevelFinished = allCampaignLevelStores.size() - 1;
            final int finalIndex = i;
            HangmanQuestionCategoryEnum categoryEnum = HangmanQuestionCategoryEnum.values()[i];
            int starsWon = -1;
            MyButton categBtn = new ButtonBuilder().setText(new SpecificPropertiesUtils().getQuestionCategoryLabel(categoryEnum.getIndex())).setButtonSkin(getButtonSkin(maxLevelFinished)).build();
            for (CampaignStoreLevel level : allCampaignLevelStores) {
                if (CampaignLevelEnumService.getCategory(level.getName()) == categoryEnum.getIndex()) {
                    categBtn.setDisabled(true);
                    starsWon = level.getStarsWon();
                }
            }
            categBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    CampaignLevel campaignLevel = getHangmanCampaignLevelEnum(maxLevelFinished, finalIndex);
                    HangmanGame.getInstance().getScreenManager().showCampaignGameScreen(new GameContextService().createGameContext(new CampaignLevelEnumService(campaignLevel).getQuestionConfig(GeoQuizCampaignScreen.TOTAL_QUESTIONS)), campaignLevel);
                }
            });
            Table btnTable = new Table();
            if (starsWon != -1) {
                btnTable.add(new MyWrappedLabel(starsWon + "")).row();
            } else {
                btnTable.add().row();
            }
            btnTable.add(categBtn)
                    .height(ScreenDimensionsManager.getScreenHeightValue(30))
                    .width(ScreenDimensionsManager.getScreenWidthValue(45));
            table.add(btnTable)
                    .pad(MainDimen.horizontal_general_margin.getDimen())
                    .height(ScreenDimensionsManager.getScreenHeightValue(30))
                    .width(ScreenDimensionsManager.getScreenWidthValue(45));
        }
        if (allCampaignLevelStores.size() == totalCat) {
            new HangmanLevelFinishedPopup(this, true).addToPopupManager();
        }
        return table;
    }

    private HangmanCampaignLevelEnum getHangmanCampaignLevelEnum(int maxLevelFinished, int currentIndex) {
        int diff = maxLevelFinished + 1;
        return HangmanCampaignLevelEnum.valueOf("LEVEL_" + diff + "_" + currentIndex);
    }


    private ButtonSkin getButtonSkin(int maxLevelFinished) {
        if (maxLevelFinished == 2) {
            return MainButtonSkin.DEFAULT;
        } else if (maxLevelFinished == 3) {
            return MainButtonSkin.DEFAULT;
        } else if (maxLevelFinished == 4) {
            return MainButtonSkin.DEFAULT;
        } else if (maxLevelFinished == 5) {
            return MainButtonSkin.DEFAULT;
        } else {
            return MainButtonSkin.DEFAULT;
        }
    }

    @Override
    public void onBackKeyPress() {
        Gdx.app.exit();
    }

}
