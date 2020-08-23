package libgdx.implementations.screens.implementations.countries;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;
import java.util.List;

import libgdx.campaign.CampaignStoreService;
import libgdx.game.Game;
import libgdx.screen.AbstractScreen;
import libgdx.skelgameimpl.skelgame.SkelGameRatingService;
import libgdx.utils.Utils;

public class CountriesCampaignScreen extends AbstractScreen<CountriesScreenManager> {

    private CampaignStoreService campaignStoreService = new CampaignStoreService();
    private Table allTable;


    @Override
    public void buildStage() {
        if (Game.getInstance().isFirstTimeMainMenuDisplayed()) {
            new SkelGameRatingService(this).appLaunched();
        }
        Game.getInstance().setFirstTimeMainMenuDisplayed(false);
        Table table = new Table();
        table.setFillParent(true);
        addActor(table);
    }


    @Override
    public void onBackKeyPress() {
        Gdx.app.exit();
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        Utils.createChangeLangPopup(new Runnable() {
            @Override
            public void run() {
                CountriesContainers.reset();
            }
        });
    }
}
