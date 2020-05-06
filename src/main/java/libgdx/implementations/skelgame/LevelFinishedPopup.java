package libgdx.implementations.skelgame;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import libgdx.constants.Contrast;
import libgdx.utils.model.FontColor;
import org.apache.commons.lang3.StringUtils;

import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignLevelEnumService;
import libgdx.campaign.CampaignStoreService;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.popup.MyPopup;
import libgdx.implementations.skelgame.CampaignScreenManager;
import libgdx.implementations.skelgame.SkelGameLabel;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.implementations.skelgame.gameservice.SinglePlayerLevelFinishedService;
import libgdx.screen.AbstractScreen;
import libgdx.screens.implementations.geoquiz.GeoQuizCampaignScreen;
import libgdx.screens.implementations.geoquiz.QuizScreenManager;

public class LevelFinishedPopup extends MyPopup<AbstractScreen, CampaignScreenManager> {

    private GameContext gameContext;
    private String gameOverSuccessText;
    private CampaignLevel currentCampaignLevel;

    public LevelFinishedPopup(AbstractScreen abstractScreen,
                              CampaignLevel currentCampaignLevel,
                              GameContext gameContext) {
        super(abstractScreen);
        this.gameContext = gameContext;
        this.currentCampaignLevel = currentCampaignLevel;
    }

    public LevelFinishedPopup(AbstractScreen screen, String gameOverSuccessText) {
        super(screen);
        this.gameOverSuccessText = gameOverSuccessText;
    }

    @Override
    public void addButtons() {
        MyButton playAgain = new ButtonBuilder()
                .setFontColor(FontColor.BLACK)
                .setContrast(Contrast.LIGHT)
                .setDefaultButton().setText(SkelGameLabel.play_again.getText()).build();
        playAgain.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (StringUtils.isNotBlank(gameOverSuccessText)) {
                    new CampaignStoreService().reset();
                    screenManager.showMainScreen();
                } else {
                    screenManager.showCampaignGameScreen(getGameContext(), currentCampaignLevel);
                }
            }
        });
        addButton(playAgain);

        if (StringUtils.isBlank(gameOverSuccessText)) {
            MyButton campaignScreenBtn = new ButtonBuilder().setDefaultButton()
                    .setContrast(Contrast.LIGHT)
                    .setFontColor(FontColor.BLACK)
                    .setText(SkelGameLabel.go_back.getText()).build();
            addButton(campaignScreenBtn);
            campaignScreenBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    screenManager.showCampaignScreen();
                }
            });
        }
    }

    @Override
    public void hide() {
        super.hide();
        screenManager.showCampaignScreen();
    }

    protected GameContext getGameContext() {
        return new GameContextService().createGameContext(
                gameContext.getQuestionConfig());
    }

    @Override
    public void onBackKeyPress() {
        if (StringUtils.isNotBlank(gameOverSuccessText)) {
            screenManager.showMainScreen();
        }
    }

    public CampaignLevel getCurrentCampaignLevel() {
        return currentCampaignLevel;
    }

    @Override
    protected String getLabelText() {
        String text;
        if (StringUtils.isNotBlank(gameOverSuccessText)) {
            text = gameOverSuccessText;
        } else {
            text = SkelGameLabel.level_failed.getText();
        }
        return text;
    }


}