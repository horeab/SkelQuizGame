package libgdx.screens.implementations.hangman;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignLevelEnumService;
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
    private CampaignLevel currentCampaignLevel;

    public HangmanLevelFinishedPopup(AbstractScreen abstractScreen, CampaignLevel currentCampaignLevel, GameContext gameContext) {
        super(abstractScreen);
        this.gameContext = gameContext;
        this.currentCampaignLevel = currentCampaignLevel;
    }

    @Override
    public void addButtons() {
        MyButton playAgain = new ButtonBuilder().setDefaultButton().setText(SkelGameLabel.play_again.getText()).build();
        playAgain.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screenManager.showCampaignGameScreen(new GameContextService().createGameContext(gameContext.getQuestionConfig()), currentCampaignLevel);
            }
        });
        addButton(playAgain);
    }

    @Override
    protected String getLabelText() {
        String text = "";
        text = SkelGameLabel.level_failed.getText();
        return text;
    }


}