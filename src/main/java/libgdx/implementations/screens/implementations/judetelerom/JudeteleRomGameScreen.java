package libgdx.implementations.screens.implementations.judetelerom;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import libgdx.campaign.CampaignStoreService;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.popup.notificationpopup.MyNotificationPopupConfigBuilder;
import libgdx.controls.popup.notificationpopup.MyNotificationPopupCreator;
import libgdx.dbapi.GameStatsDbApiService;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.hangman.HangmanGameCreatorDependencies;
import libgdx.implementations.judetelerom.JudeteleRomCampaignLevelEnum;
import libgdx.implementations.judetelerom.JudeteleRomCategoryEnum;
import libgdx.implementations.judetelerom.JudeteleRomSpecificResource;
import libgdx.implementations.skelgame.gameservice.*;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.implementations.skelgame.question.GameQuestionInfoStatus;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.implementations.skelgame.question.Question;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.implementations.screens.GameScreen;
import libgdx.utils.DateUtils;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.RGBColor;

public class JudeteleRomGameScreen extends GameScreen<JudeteleRomScreenManager> {

    private JudeteContainers judeteContainers = new JudeteContainers();
    private CampaignStoreService campaignStoreService = new CampaignStoreService();
    private Table allTable;

    public JudeteleRomGameScreen(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    public void buildStage() {
        createAllTable();
        new BackButtonBuilder().addHoverBackButton(this);
    }

    private void createAllTable() {
        if (Game.getInstance().getCurrentUser() != null) {
            new GameStatsDbApiService().incrementGameStatsQuestionsWon(Game.getInstance().getCurrentUser().getId(), Long.valueOf(DateUtils.getNowMillis()).toString());
        }

        allTable = new Table();
        float dimen = MainDimen.vertical_general_margin.getDimen();
        allTable.add(judeteContainers.createAllJudeteFound()).padTop(dimen * 2).row();
        String allQuestionsPlayed = campaignStoreService.getAllQuestionsPlayed();
        allTable.add(allQuestionsTable(allQuestionsPlayed.split(CampaignStoreService.TEXT_SPLIT).length - 1)).padBottom(dimen).padTop(dimen).growX().row();
        QuestionContainerCreatorService questionContainerCreatorService = gameContext.getCurrentUserCreatorDependencies().getQuestionContainerCreatorService(gameContext, this);
        Table questionTable = questionContainerCreatorService.createQuestionTable();
        Table answersTable = questionContainerCreatorService.createAnswerOptionsTable();
        allTable.add(questionTable).growY().row();
        float topPad = 0;
        if (gameContext.getCurrentUserCreatorDependencies() instanceof HangmanGameCreatorDependencies) {
            topPad = ScreenDimensionsManager.getScreenHeightValue(15);
        }
        allTable.add(answersTable).padTop(-topPad).growY();
        allTable.setFillParent(true);
        addActor(allTable);

        questionContainerCreatorService.processGameInfo(gameContext.getCurrentUserGameUser().getGameQuestionInfo());
    }

    private Table allQuestionsTable(int nrOfCorrectQuestions) {
        Table table = new Table();
        int totalNrOfQuestions = JudeteleRomCampaignLevelEnum.values().length * JudeteleRomCategoryEnum.values().length;
        float qTableWidth = 100 / Float.valueOf(totalNrOfQuestions);
        for (int i = 0; i < totalNrOfQuestions; i++) {
            Table qTable = new Table();
            if (i <= (nrOfCorrectQuestions - 1) && nrOfCorrectQuestions != 0) {
                qTable.setBackground(GraphicUtils.getNinePatch(JudeteleRomSpecificResource.allq_bakcground));
            }
            table.add(qTable).width(ScreenDimensionsManager.getScreenWidthValue(qTableWidth));
        }
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
                allTable.remove();
                createAllTable();
            }
        })));
    }

    private void processPlayedQuestions() {
        for (GameQuestionInfo gameQuestionInfo : gameContext.getCurrentUserGameUser().getAllQuestionInfos()) {
            if (gameQuestionInfo.getStatus() == GameQuestionInfoStatus.LOST) {
                gameContext.getCurrentUserGameUser().resetQuestion(gameQuestionInfo);
            } else {
                Question question = gameQuestionInfo.getQuestion();
                if (gameQuestionInfo.getStatus() == GameQuestionInfoStatus.WON && !campaignStoreService.isQuestionAlreadyPlayed(JudeteContainers.getQuestionId(question.getQuestionLineInQuestionFile(), question.getQuestionCategory(), question.getQuestionDifficultyLevel()))) {
                    campaignStoreService.putQuestionPlayed(JudeteContainers.getQuestionId(question.getQuestionLineInQuestionFile(), question.getQuestionCategory(), question.getQuestionDifficultyLevel()));
                    int questionLineInQuestionFile = question.getQuestionLineInQuestionFile();
                    if (JudeteContainers.isJudetFound(questionLineInQuestionFile)) {
                        new MyNotificationPopupCreator(new MyNotificationPopupConfigBuilder().setText(
                                SpecificPropertiesUtils.getText("ro_judetelerom_judet_found",
                                        new SpecificPropertiesUtils().getQuestionCampaignLabel
                                                (questionLineInQuestionFile))).build()).shortNotificationPopup().addToPopupManager();
                    }
                }
            }
        }
    }


    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
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

    public void executeLevelFinished() {
        GameUser gameUser = gameContext.getCurrentUserGameUser();
        if (levelFinishedService.isGameWon(gameUser)) {
            screenManager.showMainScreen();
        }
//        screenManager.showMainScreen();
    }
}
