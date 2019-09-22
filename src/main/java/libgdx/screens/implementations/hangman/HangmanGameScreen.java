package libgdx.screens.implementations.hangman;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignService;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.implementations.geoquiz.QuizGame;
import libgdx.implementations.skelgame.gameservice.*;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.resources.dimen.MainDimen;
import libgdx.screens.implementations.geoquiz.CampaignLevelFinishedPopup;
import libgdx.screens.GameScreen;
import libgdx.utils.ScreenDimensionsManager;

import java.util.ArrayList;
import java.util.List;

public class HangmanGameScreen extends GameScreen<HangmanScreenManager> {

    private CampaignLevel campaignLevel;
    private HangmanGameService hangmanGameService;
    private HangmanQuestionContainerCreatorService hangmanQuestionContainerCreatorService;

    public HangmanGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        super(gameContext);
        this.campaignLevel = campaignLevel;
        this.hangmanGameService = new HangmanGameService(gameContext.getQuestion());
        this.hangmanQuestionContainerCreatorService = new HangmanQuestionContainerCreatorService(gameContext, this);
    }

    @Override
    public void buildStage() {
        Table table = new Table();
        table.setFillParent(true);
        MyWrappedLabel word = new MyWrappedLabel(hangmanGameService.getCurrentWordState(hangmanGameService.getHangmanWord(gameContext.getQuestion().getQuestionString()), gameContext.getCurrentUserGameUser().getGameQuestionInfo().getAnswerIds()));
        table.add(word).row();
        table.add(new HangmanQuestionContainerCreatorService(gameContext, this).createSquareAnswerOptionsTable(new ArrayList<>(hangmanQuestionContainerCreatorService.getAllAnswerButtons().values())));
        addActor(table);
    }

    public void goToNextQuestionScreen() {
        screenManager.showCampaignGameScreen(gameContext, campaignLevel);
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

    public void executeLevelFinished() {
        SinglePlayerLevelFinishedService levelFinishedService = new SinglePlayerLevelFinishedService();
        GameUser gameUser = gameContext.getCurrentUserGameUser();
        if (levelFinishedService.isGameWon(gameUser)) {
            new CampaignService().levelFinished(QuizGame.getInstance().getDependencyManager().getStarsService().getStarsWon(LevelFinishedService.getPercentageOfWonQuestions(gameUser)), campaignLevel);
        }
        new CampaignLevelFinishedPopup(this, campaignLevel, gameContext).addToPopupManager();
    }
}
