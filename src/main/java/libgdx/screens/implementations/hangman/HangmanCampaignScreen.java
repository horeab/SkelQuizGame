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
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfig;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.hangman.HangmanCampaignLevelEnum;
import libgdx.implementations.hangman.HangmanGame;
import libgdx.implementations.hangman.HangmanQuestionCategoryEnum;
import libgdx.implementations.hangman.HangmanSpecificResource;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.implementations.skelgame.SkelGameLabel;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.implementations.skelgame.gameservice.QuizStarsService;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.screen.AbstractScreen;
import libgdx.screens.implementations.geoquiz.GeoQuizCampaignScreen;
import libgdx.screens.implementations.geoquiz.QuizScreenManager;
import libgdx.utils.ScreenDimensionsManager;

import java.util.ArrayList;
import java.util.List;

public class HangmanCampaignScreen extends AbstractScreen<HangmanScreenManager> {

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
        new BackButtonBuilder().addHoverBackButton(this);
    }

    private Table createAllTable() {
        Table table = new Table();
        int totalCat = HangmanQuestionCategoryEnum.values().length;
        int totalStarsWon = 0;
        table.add(new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setFontScale(FontManager.getBigFontDim()).setText(MainGameLabel.l_level.getText((allCampaignLevelStores.size() + 1))).build())).pad(MainDimen.vertical_general_margin.getDimen()).colspan(2).row();
        for (int i = 0; i < totalCat; i++) {
            if (i > 0 && i % 2 == 0) {
                table.row();
            }
            final int maxLevelFinished = allCampaignLevelStores.size() - 1;
            final int finalIndex = i;
            HangmanQuestionCategoryEnum categoryEnum = HangmanQuestionCategoryEnum.values()[i];
            int starsWon = -1;
            MyButton categBtn = new ButtonBuilder().setText(new SpecificPropertiesUtils().getQuestionCategoryLabel(categoryEnum.getIndex())).setButtonSkin(GameButtonSkin.HANGMAN_CATEG).build();
            for (CampaignStoreLevel level : allCampaignLevelStores) {
                if (CampaignLevelEnumService.getCategory(level.getName()) == categoryEnum.getIndex()) {
                    categBtn.setDisabled(true);
                    starsWon = level.getStarsWon();
                    totalStarsWon = totalStarsWon + starsWon;
                }
            }
            float horizontalGeneralMarginDimen = MainDimen.horizontal_general_margin.getDimen();
            if (starsWon == QuizStarsService.NR_OF_STARS_TO_DISPLAY) {
                categBtn.getCenterRow().row();
                categBtn.getCenterRow().add(GraphicUtils.getImage(HangmanSpecificResource.star)).width(horizontalGeneralMarginDimen * 5).height(horizontalGeneralMarginDimen * 5).padTop(horizontalGeneralMarginDimen);
            }
            categBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    CampaignLevel campaignLevel = getHangmanCampaignLevelEnum(maxLevelFinished, finalIndex);
                    CampaignLevelEnumService enumService = new CampaignLevelEnumService(campaignLevel);
                    QuestionConfig questionConfig = enumService.getQuestionConfig(HangmanGameScreen.TOTAL_QUESTIONS);
                    //if this category, the questions should be mixed from all other categs
                    if (enumService.getCategory() == HangmanQuestionCategoryEnum.cat5.getIndex()) {
                        List<String> questionCategoryStringList = new ArrayList<>();
                        for (HangmanQuestionCategoryEnum categ : HangmanQuestionCategoryEnum.values()) {
                            if (categ != HangmanQuestionCategoryEnum.cat5) {
                                questionCategoryStringList.add(categ.name());
                            }
                        }
                        questionConfig = new QuestionConfig(questionConfig.getQuestionDifficultyStringList(), questionCategoryStringList, questionConfig.getAmount());
                    }
                    HangmanGame.getInstance().getScreenManager().showCampaignGameScreen(new GameContextService().createGameContext(questionConfig), campaignLevel);
                }
            });
            Table btnTable = new Table();
            btnTable.add(categBtn)
                    .height(ScreenDimensionsManager.getScreenHeightValue(27))
                    .width(ScreenDimensionsManager.getScreenWidthValue(45));
            table.add(btnTable)
                    .pad(horizontalGeneralMarginDimen)
                    .height(ScreenDimensionsManager.getScreenHeightValue(27))
                    .width(ScreenDimensionsManager.getScreenWidthValue(45));
        }
        if (allCampaignLevelStores.size() == totalCat) {
            CampaignStoreService campaignStoreService = new CampaignStoreService();
            String gameFinishedText = SkelGameLabel.game_finished.getText();
            if (campaignStoreService.getAllStarsWon() < totalStarsWon) {
                campaignStoreService.updateAllStarsWon(totalStarsWon);
                gameFinishedText = MainGameLabel.l_score_record.getText(totalStarsWon);
            }
            new HangmanLevelFinishedPopup(this, gameFinishedText).addToPopupManager();
        }
        return table;
    }

    private HangmanCampaignLevelEnum getHangmanCampaignLevelEnum(int maxLevelFinished, int currentIndex) {
        int diff = maxLevelFinished + 1;
        return HangmanCampaignLevelEnum.valueOf("LEVEL_" + diff + "_" + currentIndex);
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

}
