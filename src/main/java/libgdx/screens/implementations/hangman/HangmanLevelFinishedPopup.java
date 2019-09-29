package libgdx.screens.implementations.hangman;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignLevelEnumService;
import libgdx.campaign.CampaignStoreService;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.popup.MyPopup;
import libgdx.implementations.skelgame.SkelGameLabel;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.implementations.skelgame.gameservice.SinglePlayerLevelFinishedService;
import libgdx.screen.AbstractScreen;
import libgdx.screens.implementations.geoquiz.GeoQuizCampaignScreen;
import libgdx.screens.implementations.geoquiz.QuizScreenManager;

public class HangmanLevelFinishedPopup extends MyPopup<AbstractScreen, HangmanScreenManager> {

    private GameContext gameContext;
    private boolean gameOverSuccess;
    private CampaignLevel currentCampaignLevel;

    public HangmanLevelFinishedPopup(AbstractScreen abstractScreen, CampaignLevel currentCampaignLevel, GameContext gameContext) {
        super(abstractScreen);
        this.gameContext = gameContext;
        this.currentCampaignLevel = currentCampaignLevel;
    }

    public HangmanLevelFinishedPopup(AbstractScreen screen, boolean gameOverSuccess) {
        super(screen);
        this.gameOverSuccess = gameOverSuccess;
    }

    @Override
    public void addButtons() {
        MyButton playAgain = new ButtonBuilder().setDefaultButton().setText(SkelGameLabel.play_again.getText()).build();
        playAgain.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (gameOverSuccess) {
                    new CampaignStoreService().reset();
                    screenManager.showMainScreen();
                } else {
                    screenManager.showCampaignGameScreen(new GameContextService().createGameContext(gameContext.getQuestionConfig()), currentCampaignLevel);
                }
            }
        });
        addButton(playAgain);

        if (!gameOverSuccess) {
            MyButton campaignScreenBtn = new ButtonBuilder().setDefaultButton().setText(SkelGameLabel.go_back.getText()).build();
            addButton(campaignScreenBtn);
            campaignScreenBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    screenManager.showMainScreen();
                }
            });
        }
    }

    @Override
    protected String getLabelText() {
        String text;
        if (gameOverSuccess) {
            text = SkelGameLabel.game_finished.getText();
        } else {
            text = SkelGameLabel.level_failed.getText();
        }
        return text;
    }


}