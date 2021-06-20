package libgdx.implementations.screens.implementations.astronomy;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignStoreService;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.game.Game;
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.skelgame.gameservice.*;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.implementations.skelgame.question.GameQuestionInfoStatus;
import libgdx.resources.MainResource;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.RGBColor;

import java.util.LinkedHashMap;
import java.util.Map;

public class AstronomyGameScreen extends GameScreen<AstronomyScreenManager> {

    public static int TOTAL_QUESTIONS = 6;
    private Table allTable;
    private AstronomyGameType astronomyGameType;

    public AstronomyGameScreen(GameContext gameContext, CampaignLevel campaignLevel, AstronomyGameType astronomyGameType) {
        super(gameContext);
        this.astronomyGameType = astronomyGameType;
    }

    @Override
    public void buildStage() {
        new ActorAnimation(getAbstractScreen()).createScrollingBackground(MainResource.background_texture);
        createAllTable();
        new BackButtonBuilder().addHoverBackButton(this);
    }

    private void createAllTable() {
        allTable = new Table();
        QuestionContainerCreatorService questionContainerCreatorService;
        if (gameContext.getQuestion().getQuestionCategory().getCreatorDependencies() == ImageClickGameCreatorDependencies.class) {
            questionContainerCreatorService = new AstronomyImageQuestionContainerCreatorService(gameContext, this);
        } else {
            questionContainerCreatorService = new AstronomyQuestionContainerCreatorService(gameContext, this);
        }
        Table questionTable = questionContainerCreatorService.createQuestionTable();
        Table answersTable = questionContainerCreatorService.createAnswerOptionsTable();
        Table headerTable = new HeaderCreator().createHeaderTable(createAllQuestionsMap());
        headerTable.setHeight(ScreenDimensionsManager.getScreenHeight(5));
        allTable.add(headerTable).height(headerTable.getHeight()).row();
        allTable.add(questionTable).growY().row();
        float topPad = 0;
        allTable.add(answersTable).padTop(-topPad).growY();
        allTable.setFillParent(true);
        addActor(allTable);

        questionContainerCreatorService.processGameInfo(gameContext.getCurrentUserGameUser().getGameQuestionInfo());
    }

    private Map<Integer, GameQuestionInfoStatus> createAllQuestionsMap() {
        Map<Integer, GameQuestionInfoStatus> map = new LinkedHashMap<>();
        for (GameQuestionInfo gameQuestionInfo : gameContext.getCurrentUserGameUser().getAllQuestionInfos()) {
            map.put(gameQuestionInfo.getQuestion().getQuestionLineInQuestionFile(), gameQuestionInfo.getStatus());
        }
        return map;
    }

    @Override
    public void goToNextQuestionScreen() {
        Table table = getRoot().findActor(HangmanRefreshQuestionDisplayService.ACTOR_NAME_HANGMAN_WORD_TABLE);
        if (table != null) {
            table.addAction(Actions.fadeOut(0.2f));
            table.remove();
        }
        allTable.addAction(Actions.sequence(Actions.delay(1.2f), Actions.fadeOut(0.2f), Utils.createRunnableAction(new Runnable() {
            @Override
            public void run() {
                allTable.remove();
                createAllTable();
            }
        })));
    }

    @Override
    public void onBackKeyPress() {
        if (astronomyGameType == AstronomyGameType.FIND_PLANET) {
            screenManager.showMainScreen();
        } else {
            screenManager.showDetailedCampaignScreen(astronomyGameType);
        }
    }

    @Override
    protected int getQuestionsPlayedForPopupAd() {
        return 7;
    }

    @Override
    public void showPopupAd(Runnable runnable) {
        int questionsPlayed = new CampaignStoreService().getNrOfQuestionsPlayed();
        if (questionsPlayed > 0 && questionsPlayed % getQuestionsPlayedForPopupAd() == 0) {
            Game.getInstance().getAppInfoService().showPopupAd(runnable);
        } else {
            runnable.run();
        }
    }

    @Override
    public void setBackgroundColor(RGBColor backgroundColor) {
        if (levelFinishedService.isGameWon(gameContext.getCurrentUserGameUser())) {
            super.setBackgroundColor(RGBColor.RED);
        } else {
            super.setBackgroundColor(backgroundColor);
        }
    }

    @Override
    public void executeLevelFinished() {
        Game.getInstance().getAppInfoService().showPopupAd(new Runnable() {
            @Override
            public void run() {
                onBackKeyPress();
            }
        });
    }
}
