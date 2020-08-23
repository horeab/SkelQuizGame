package libgdx.implementations.screens.implementations.periodictable;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import libgdx.campaign.CampaignStoreService;
import libgdx.controls.button.MainButtonSize;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.popup.notificationpopup.MyNotificationPopupConfigBuilder;
import libgdx.controls.popup.notificationpopup.MyNotificationPopupCreator;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.periodictable.PeriodicTableCampaignLevelEnum;
import libgdx.implementations.periodictable.PeriodicTableCategoryEnum;
import libgdx.implementations.periodictable.PeriodicTableCreatorDependencies;
import libgdx.implementations.periodictable.PeriodicTableSpecificResource;
import libgdx.implementations.periodictable.spec.ChemicalElement;
import libgdx.implementations.periodictable.spec.ChemicalElementsUtil;
import libgdx.implementations.skelgame.gameservice.*;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.implementations.skelgame.question.GameQuestionInfoStatus;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.implementations.skelgame.question.Question;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.implementations.screens.GameScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.RGBColor;

import java.util.List;

public class PeriodicTableGameScreen extends GameScreen<PeriodicTableScreenManager> {

    public static int TOTAL_QUESTIONS = 9;
    private Table allTable;
    private Table hintButtonsTable;
    private List<ChemicalElement> chemicalElements;

    public PeriodicTableGameScreen(GameContext gameContext) {
        super(gameContext);
        chemicalElements = ((PeriodicTableCreatorDependencies) CreatorDependenciesContainer.getCreator(PeriodicTableCreatorDependencies.class)).getElements();
    }

    @Override
    public void buildStage() {
        createAllTable();
        new BackButtonBuilder().addHoverBackButton(this);
    }

    private void createAllTable() {
        allTable = new Table();
        if (gameContext.getAmountAvailableHints() < 1) {
            gameContext.addHint();
        }
        CampaignStoreService campaignStoreService = new CampaignStoreService();
        boolean hintButtonEnabled = isHintButtonEnabled();
        PeriodicTableQuestionContainerCreatorService questionContainerCreatorService = new PeriodicTableQuestionContainerCreatorService(gameContext, this);
        HintButton hintButton = questionContainerCreatorService.getHintButtons().get(0);
        float dimen = MainDimen.vertical_general_margin.getDimen();
        hintButton.getMyButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                campaignStoreService.putHintPlayedAtQuestion();
            }
        });
        hintButton.getMyButton().setDisabled(!hintButtonEnabled);
        if (!hintButtonEnabled) {
            hintButton.getMyButton().getActions().clear();
        }
        allTable.add(new PeriodicTableContainers().createAllElementsFound()).padTop(dimen * 2).row();
        String allQuestionsPlayed = campaignStoreService.getAllQuestionsPlayed();
        allTable.add(allQuestionsTable(allQuestionsPlayed.split(CampaignStoreService.TEXT_SPLIT).length - 1)).padBottom(dimen).padTop(dimen).growX().row();
        Table questionTable = questionContainerCreatorService.createQuestionTable();
        Table answersTable = questionContainerCreatorService.createAnswerOptionsTable();
        allTable.add(questionTable).growY().row();
        allTable.add(answersTable).padBottom(dimen * 3).growY();
        allTable.setFillParent(true);
        hintButtonsTable = questionContainerCreatorService.createHintButtonsTable();
        hintButtonsTable.setX(ScreenDimensionsManager.getScreenWidth() - hintButtonsTable.getPrefWidth() / 2);
        hintButtonsTable.setY(ScreenDimensionsManager.getScreenHeight() - MainButtonSize.BACK_BUTTON.getHeight());
        addActor(hintButtonsTable);
        addActor(allTable);
        hintButtonsTable.toFront();

        questionContainerCreatorService.processGameInfo(gameContext.getCurrentUserGameUser().getGameQuestionInfo());
    }

    private boolean isHintButtonEnabled() {
        CampaignStoreService campaignStoreService = new CampaignStoreService();
        int questionDifferenceUntilNewHint = 4;
        int hintPlayedAtQuestion = campaignStoreService.getHintPlayedAtQuestion();
        int nrOfQuestionsPlayed = campaignStoreService.getNrOfQuestionsPlayed();
        boolean validHint = nrOfQuestionsPlayed - hintPlayedAtQuestion >= questionDifferenceUntilNewHint;
        return hintPlayedAtQuestion == -1 || validHint;
    }

    private Table allQuestionsTable(int nrOfCorrectQuestions) {
        Table table = new Table();
        int totalNrOfQuestions = PeriodicTableCampaignLevelEnum.values().length * PeriodicTableCategoryEnum.values().length;
        float qTableWidth = 100 / Float.valueOf(totalNrOfQuestions);
        for (int i = 0; i < totalNrOfQuestions; i++) {
            Table qTable = new Table();
            if (i <= (nrOfCorrectQuestions - 1) && nrOfCorrectQuestions != 0) {
                qTable.setBackground(GraphicUtils.getNinePatch(PeriodicTableSpecificResource.allq_bakcground));
            }
            table.add(qTable).height(ScreenDimensionsManager.getScreenHeightValue(5)).width(ScreenDimensionsManager.getScreenWidthValue(qTableWidth));
        }
        System.out.println(nrOfCorrectQuestions + "");
        return table;
    }

    @Override
    public void goToNextQuestionScreen() {
        processPlayedQuestions();
        if (levelFinishedService.isGameWon(gameContext.getCurrentUserGameUser())) {
        }
        Table hangmanWordTable = getRoot().findActor(HangmanRefreshQuestionDisplayService.ACTOR_NAME_HANGMAN_WORD_TABLE);
        if (hangmanWordTable != null) {
            hangmanWordTable.addAction(Actions.fadeOut(0.2f));
            hangmanWordTable.remove();
        }
        allTable.addAction(Actions.sequence(Actions.fadeOut(0.2f), Utils.createRunnableAction(new Runnable() {
            @Override
            public void run() {
                hintButtonsTable.remove();
                allTable.remove();
                createAllTable();
            }
        })));
    }


    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

    @Override
    protected int getQuestionsPlayedForPopupAd() {
        return 10;
    }

    @Override
    protected void setBackgroundColor(RGBColor backgroundColor) {
        if (levelFinishedService.isGameWon(gameContext.getCurrentUserGameUser())) {
            super.setBackgroundColor(RGBColor.RED);
        } else {
            super.setBackgroundColor(backgroundColor);
        }
    }

    @Override
    public void animateGameFinished() {
        super.animateGameFinished();
        processPlayedQuestions();
        if (LevelFinishedService.getPercentageOfWonQuestions(gameContext.getCurrentUserGameUser()) == 100f) {
//            ActorAnimation.animateImageCenterScreenFadeOut(AnatomySpecificResource.star, 0.3f);
        }
    }


    private void processPlayedQuestions() {
        CampaignStoreService campaignStoreService = new CampaignStoreService();
        for (GameQuestionInfo gameQuestionInfo : gameContext.getCurrentUserGameUser().getAllQuestionInfos()) {
            if (gameQuestionInfo.getStatus() == GameQuestionInfoStatus.LOST) {
                gameContext.getCurrentUserGameUser().resetQuestion(gameQuestionInfo);
            } else {
                Question question = gameQuestionInfo.getQuestion();
                if (gameQuestionInfo.getStatus() == GameQuestionInfoStatus.WON && !campaignStoreService.isQuestionAlreadyPlayed(PeriodicTableContainers.getQuestionId(question.getQuestionLineInQuestionFile(), question.getQuestionCategory(), question.getQuestionDifficultyLevel()))) {
                    campaignStoreService.putQuestionPlayed(PeriodicTableContainers.getQuestionId(question.getQuestionLineInQuestionFile(), question.getQuestionCategory(), question.getQuestionDifficultyLevel()));
                    int questionLineInQuestionFile = question.getQuestionLineInQuestionFile();
                    if (PeriodicTableContainers.isElementFound(questionLineInQuestionFile)) {
                        new MyNotificationPopupCreator(new MyNotificationPopupConfigBuilder().setTextColor(FontColor.BLACK).setText(
                                SpecificPropertiesUtils.getText("periodictable_element_discovered",
                                        ChemicalElementsUtil.getElementByNr(question.getQuestionLineInQuestionFile(), chemicalElements).getName()))
                                .build()).shortNotificationPopup().addToPopupManager();
                    }
                }
            }
        }
    }

    public void executeLevelFinished() {
        GameUser gameUser = gameContext.getCurrentUserGameUser();
        if (levelFinishedService.isGameWon(gameUser)) {
            screenManager.showMainScreen();
        }
    }
}
