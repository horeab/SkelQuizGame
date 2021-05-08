package libgdx.implementations.screens.implementations.astronomy;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignLevelEnumService;
import libgdx.constants.Contrast;
import libgdx.constants.Language;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.popup.MyPopup;
import libgdx.game.Game;
import libgdx.implementations.screens.implementations.geoquiz.GeoQuizCampaignScreen;
import libgdx.implementations.skelgame.CampaignScreenManager;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.implementations.skelgame.gameservice.SinglePlayerLevelFinishedService;
import libgdx.screen.AbstractScreen;
import libgdx.skelgameimpl.skelgame.SkelGameLabel;
import libgdx.utils.model.FontColor;

public class AstronomyCampaignLevelFinishedPopup<TScreenManager extends AstronomyScreenManager> extends MyPopup<AbstractScreen, TScreenManager> {

    private boolean gameOverSuccess;
    private GameContext gameContext;
    private CampaignLevel currentCampaignLevel;
    private AstronomyGameType astronomyGameType;
    private CampaignLevel nextCampaignLevel;

    public AstronomyCampaignLevelFinishedPopup(AbstractScreen abstractScreen,
                                               CampaignLevel currentCampaignLevel,
                                               AstronomyGameType astronomyGameType,
                                               GameContext gameContext) {
        super(abstractScreen);
        this.gameOverSuccess = new SinglePlayerLevelFinishedService().isGameWon(gameContext.getCurrentUserGameUser());
        this.gameContext = gameContext;
        this.astronomyGameType = astronomyGameType;
        this.nextCampaignLevel = CampaignLevelEnumService.getNextLevel(currentCampaignLevel);
        this.currentCampaignLevel = currentCampaignLevel;
    }

    @Override
    public void addButtons() {
        if (gameOverSuccess) {
            if (nextCampaignLevel != null) {
                MyButton nextLevel = new ButtonBuilder().setDefaultButton().setText(SkelGameLabel.next_level.getText()).build();
                nextLevel.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        hide();
                    }
                });
                nextLevel.addListener(GeoQuizCampaignScreen.getStartLevelListener(getScreen(), new Runnable() {
                    @Override
                    public void run() {
                        Game.getInstance().getInAppPurchaseManager().displayInAppPurchasesPopup(Language.en.name(), "Unlock extra categories and remove Ads");
                    }
                }, nextCampaignLevel));
                addButton(nextLevel);
            }
        } else {
            MyButton playAgain = new ButtonBuilder().setDefaultButton().setContrast(Contrast.LIGHT).setFontColor(FontColor.BLACK).setText(SkelGameLabel.play_again.getText()).build();
            playAgain.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    screenManager.showCampaignGameScreen(new GameContextService().createGameContext(gameContext.getQuestionConfig()), currentCampaignLevel, astronomyGameType);
                }
            });
            addButton(playAgain);
        }

        MyButton goBackBtn = new ButtonBuilder().setContrast(Contrast.LIGHT).setFontColor(FontColor.BLACK).setDefaultButton().setText(SkelGameLabel.go_back.getText()).build();
        addButton(goBackBtn);
        goBackBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (astronomyGameType == AstronomyGameType.FIND_PLANET) {
                    screenManager.showMainScreen();
                } else {
                    screenManager.showDetailedCampaignScreen(astronomyGameType);
                }
            }
        });
    }

    @Override
    protected String getLabelText() {
        String text = "";
        if (gameOverSuccess) {
            if (nextCampaignLevel != null) {
                text = SkelGameLabel.level_finished.getText();
            } else {
                text = SkelGameLabel.game_finished.getText();
            }
        } else {
            text = SkelGameLabel.level_failed.getText();
        }
        return text;
    }


}