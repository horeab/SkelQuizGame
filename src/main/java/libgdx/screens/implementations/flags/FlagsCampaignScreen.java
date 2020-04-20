package libgdx.screens.implementations.flags;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.campaign.CampaignService;
import libgdx.campaign.CampaignStoreLevel;
import libgdx.campaign.CampaignStoreService;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.flags.FlagsSpecificResource;
import libgdx.implementations.skelgame.SkelGameRatingService;
import libgdx.resources.MainResource;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import org.apache.commons.lang3.tuple.MutablePair;

import java.awt.*;
import java.util.List;

public class FlagsCampaignScreen extends AbstractScreen<FlagsScreenManager> {

    private FlagsContainers flagsContainers = new FlagsContainers();
    private CampaignService campaignService = new CampaignService();
    private CampaignStoreService campaignStoreService = new CampaignStoreService();
    private List<CampaignStoreLevel> allCampaignLevelStores;

    public FlagsCampaignScreen() {
        allCampaignLevelStores = campaignService.processAndGetAllLevels();
    }

    @Override
    public void buildStage() {
        if (Game.getInstance().isFirstTimeMainMenuDisplayed()) {
            new SkelGameRatingService(this).appLaunched();
        }
        Game.getInstance().setFirstTimeMainMenuDisplayed(false);
        Table table = new Table();
        table.setFillParent(true);
        table.add(createAllTable()).expand();
        addActor(table);

        float side = ScreenDimensionsManager.getScreenWidthValue(40);
        Texture texture = GraphicUtils.getTexture(FlagsSpecificResource.broccolismall, side);
        Image img = GraphicUtils.getImage(texture);
        img.setWidth(side);
        img.setHeight(side);
        if (!texture.getTextureData().isPrepared()) {
            texture.getTextureData().prepare();
        }
        img.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Pixmap pixmap = texture.getTextureData().consumePixmap();
                y = pixmap.getHeight() - y;
//                pixmap.setBlending(Pixmap.Blending.None);
//                Pixmap res = new FillColorService().floodFill(
//                        pixmap,
//                        new MutablePair<Integer, Integer>(Math.round(x), Math.round(y)),
//                        Color.RED.toIntBits()
//                );
//                res.setBlending(Pixmap.Blending.None);

                Pixmap res =  PixmapUtil.floodFill(pixmap, Math.round(x), Math.round(y), Color.BLACK.toIntBits());
                table.add(new Image(new Texture(res))).width(img.getWidth()).height(img.getHeight());
            }
        });
        table.add(img).width(img.getWidth()).height(img.getHeight());
    }

    private Table createAllTable() {
        Table table = new Table();
        table.setFillParent(true);
        return table;
    }

    @Override
    public void onBackKeyPress() {
        Gdx.app.exit();
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        Utils.createChangeLangPopup();
    }
}
