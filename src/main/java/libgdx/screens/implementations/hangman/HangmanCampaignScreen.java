package libgdx.screens.implementations.hangman;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.ArrayList;
import java.util.List;

import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignLevelEnumService;
import libgdx.campaign.CampaignService;
import libgdx.campaign.CampaignStoreLevel;
import libgdx.campaign.CampaignStoreService;
import libgdx.campaign.QuestionConfig;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.button.builders.UnlockButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.controls.labelimage.InAppPurchaseTable;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.hangman.HangmanCampaignLevelEnum;
import libgdx.implementations.hangman.HangmanGame;
import libgdx.implementations.hangman.HangmanQuestionCategoryEnum;
import libgdx.implementations.hangman.HangmanSpecificResource;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.implementations.skelgame.LevelFinishedPopup;
import libgdx.implementations.skelgame.SkelGameLabel;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.implementations.skelgame.gameservice.QuizStarsService;
import libgdx.resources.FontManager;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;

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
        if (!Utils.isValidExtraContent()) {
            new UnlockButtonBuilder().addHoverUnlockButton(getAbstractScreen(), new Runnable() {
                @Override
                public void run() {
                    screenManager.showCampaignScreen();
                }
            });
        }
    }

    private Table createHeaderTable() {
        Table table = new Table();
        table.add(new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setFontScale(FontManager.getBigFontDim())
                .setText(MainGameLabel.l_level.getText((allCampaignLevelStores.size() + 1))).build()))
                .padBottom(MainDimen.vertical_general_margin.getDimen())
                .padTop(MainDimen.vertical_general_margin.getDimen())
                .row();
        return table;
    }

    private Table createAllTable() {
        Table table = new Table();
        table.add(createHeaderTable()).colspan(2).row();
        int totalCat = HangmanQuestionCategoryEnum.values().length;
        long totalStarsWon = 0;
        float dimen = MainDimen.horizontal_general_margin.getDimen();
        InAppPurchaseTable inAppPurchaseTable = new InAppPurchaseTable();
        for (int i = 0; i < totalCat; i++) {
            if (i > 0 && i % 2 == 0) {
                table.row();
            }
            final int maxLevelFinished = allCampaignLevelStores.size() - 1;
            final int finalIndex = i;
            HangmanQuestionCategoryEnum categoryEnum = HangmanQuestionCategoryEnum.values()[i];
            long starsWon = -1;
            MyButton categBtn = new ButtonBuilder().setText(new SpecificPropertiesUtils().getQuestionCategoryLabel(categoryEnum.getIndex())).setButtonSkin(GameButtonSkin.HANGMAN_CATEG).build();
            for (CampaignStoreLevel level : allCampaignLevelStores) {
                if (CampaignLevelEnumService.getCategory(level.getName()) == categoryEnum.getIndex()) {
                    categBtn.setDisabled(true);
                    starsWon = level.getScore();
                    totalStarsWon = totalStarsWon + starsWon;
                }
            }
            if (starsWon == QuizStarsService.NR_OF_STARS_TO_DISPLAY) {
                categBtn.getCenterRow().row();
                categBtn.getCenterRow().add(GraphicUtils.getImage(HangmanSpecificResource.star)).width(dimen * 5).height(dimen * 5).padTop(dimen);
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
                        questionConfig = new QuestionConfig(questionConfig.getQuestionDifficultyStringList(), questionCategoryStringList, questionConfig.getAmount(), questionConfig.getAmountHints());
                    }
                    HangmanGame.getInstance().getScreenManager().showCampaignGameScreen(new GameContextService().createGameContext(questionConfig), campaignLevel);
                }
            });
            Table btnTable = new Table();
            btnTable.add(categBtn)
                    .height(ScreenDimensionsManager.getScreenHeightValue(27))
                    .width(ScreenDimensionsManager.getScreenWidthValue(45));
            if (!categBtn.isDisabled() && allCampaignLevelStores.size() > 2 && !Utils.isValidExtraContent()) {
                btnTable = inAppPurchaseTable.create(btnTable, new Runnable() {
                    @Override
                    public void run() {
                        screenManager.showCampaignScreen();
                    }
                }, MainDimen.horizontal_general_margin.getDimen() * 15);
                categBtn.setDisabled(true);
            }
            table.add(btnTable)
                    .pad(dimen)
                    .height(ScreenDimensionsManager.getScreenHeightValue(27))
                    .width(ScreenDimensionsManager.getScreenWidthValue(45));
        }
        if (allCampaignLevelStores.size() == totalCat) {
            CampaignStoreService campaignStoreService = new CampaignStoreService();
            String gameFinishedText = SkelGameLabel.game_finished.getText();
            if (campaignStoreService.getAllScoreWon() < totalStarsWon) {
                campaignStoreService.updateAllScoreWon(totalStarsWon);
                gameFinishedText = MainGameLabel.l_score_record.getText(totalStarsWon);
            }
            new LevelFinishedPopup(this, gameFinishedText).addToPopupManager();
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
