package libgdx.screens;

import libgdx.campaign.CampaignLevel;
import libgdx.screen.AbstractScreen;

public class GameScreen extends AbstractScreen<QuizScreenManager> {

    private CampaignLevel campaignLevel;

    public GameScreen(CampaignLevel campaignLevel) {
        this.campaignLevel = campaignLevel;
    }

    @Override
    public void buildStage() {
    }

    @Override
    public void afterBuildStage() {
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showCampaignScreen();
    }

}
