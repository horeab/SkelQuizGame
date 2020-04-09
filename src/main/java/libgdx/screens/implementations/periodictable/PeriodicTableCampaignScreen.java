package libgdx.screens.implementations.periodictable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.campaign.*;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MainButtonSize;
import libgdx.controls.button.MainButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.astronomy.AstronomyGame;
import libgdx.implementations.periodictable.PeriodicTableCampaignLevelEnum;
import libgdx.implementations.periodictable.PeriodicTableCategoryEnum;
import libgdx.implementations.periodictable.PeriodicTableGame;
import libgdx.implementations.periodictable.PeriodicTableSpecificResource;
import libgdx.implementations.periodictable.spec.ChemicalElement;
import libgdx.implementations.periodictable.spec.ChemicalElementsUtil;
import libgdx.implementations.skelgame.*;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.screen.AbstractScreen;
import libgdx.screens.implementations.anatomy.AnatomyGameScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

import java.util.List;

public class PeriodicTableCampaignScreen extends AbstractScreen<PeriodicTableScreenManager> {

    private CampaignService campaignService = new CampaignService();
    private List<CampaignStoreLevel> allCampaignLevelStores;
    private List<ChemicalElement> chemicalElements;

    public PeriodicTableCampaignScreen() {
        allCampaignLevelStores = campaignService.processAndGetAllLevels();
        chemicalElements = ChemicalElementsUtil.processTextForChemicalElements();
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
    }

    protected Stack createTitleStack(MyWrappedLabel titleLabel) {
        Stack stack = new Stack();
        stack.addActor(titleLabel);
        return stack;
    }

    private Table createAllTable() {
        Table table = new Table();
        MyWrappedLabel titleLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(
                        FontColor.LIGHT_GREEN.getColor(),
                        FontColor.LIGHT_GREEN.getColor(),
                        FontConfig.FONT_SIZE * 1.6f,
                        1f))
                .setText(Game.getInstance().getAppInfoService().getAppName()).build());

        float dimen = MainDimen.vertical_general_margin.getDimen();
        table.add(createTitleStack(titleLabel)).pad(dimen).row();
        Table controlsTable = new Table();

        MyButton startGameBtn = new ImageButtonBuilder(GameButtonSkin.PERIODICTABLE_STARTGAME, getAbstractScreen())
                .animateZoomInZoomOut()
                .setFixedButtonSize(GameButtonSize.PERIODICTABLE_MENU_BUTTON)
                .build();

        MyButton periodicTableBtn = new ImageButtonBuilder(GameButtonSkin.PERIODICTABLE_PT, getAbstractScreen())
                .setFixedButtonSize(GameButtonSize.PERIODICTABLE_MENU_BUTTON)
                .build();
        periodicTableBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PeriodicTableGame.getInstance().getScreenManager().showPeriodicTableScreen();
            }
        });

        float pad = MainDimen.horizontal_general_margin.getDimen() / 7;
        float extraWidth = ScreenDimensionsManager.getScreenWidthValue(15);
        controlsTable.add().width(extraWidth + GameButtonSize.PERIODICTABLE_MENU_BUTTON.getWidth() * 2 - dimen * 4);
        controlsTable.add(startGameBtn).width(startGameBtn.getWidth()).height(startGameBtn.getHeight()).padRight(dimen * 4);
        controlsTable.add(periodicTableBtn).width(periodicTableBtn.getWidth()).height(periodicTableBtn.getHeight()).padRight(dimen);
        controlsTable.add(new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(Color.LIGHT_GRAY, FontConfig.FONT_SIZE * 1.2f))
                .setText("0/" + chemicalElements.size())
                .setWidth(extraWidth)
                .build()));
        table.add(controlsTable).padBottom(dimen).row();
        long totalStarsWon = 0;
        float btnSide = getElSideDimen();
        int i = 0;
        Table elementsTable = new Table();
        int elementsPerRow = (int) Math.floor(ScreenDimensionsManager.getScreenWidth() / btnSide);
        for (ChemicalElement e : chemicalElements) {
            if (i % elementsPerRow == 0) {
                elementsTable.row();
            }
            elementsTable.add(createElementInfoTable(e)).pad(pad)
                    .width(btnSide).height(btnSide / 2);
            i++;
        }
        ScrollPane scrollPane = new ScrollPane(elementsTable);
        scrollPane.setScrollingDisabled(true, false);
        table.add(scrollPane).pad(pad);

        if (campaignService.getFinishedCampaignLevels().size() == PeriodicTableCampaignLevelEnum.values().length) {
            CampaignStoreService campaignStoreService = new CampaignStoreService();
            String gameFinishedText = SkelGameLabel.game_finished.getText();
            if (campaignStoreService.getAllScoreWon() < totalStarsWon) {
                campaignStoreService.updateAllScoreWon(totalStarsWon);
                gameFinishedText = MainGameLabel.l_score_record.getText(totalStarsWon);
            }
            new LevelFinishedPopup(this, gameFinishedText).addToPopupManager();
        }
        return table;
    }

    private float getElSideDimen() {
        return ScreenDimensionsManager.getScreenWidthValue(18);
    }

    private Table createElementInfoTable(ChemicalElement e) {
        Table table = new Table();
        float pad = MainDimen.horizontal_general_margin.getDimen() / 7;
        table.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
        table.add(new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(Color.BLACK, FontConfig.FONT_SIZE / 1.4f))
                .setText(e.getName())
                .build())).padBottom(pad * 4);
        table.setTouchable(Touchable.enabled);
        table.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new ChemicalElementInfoPopup(getAbstractScreen(), e).addToPopupManager();
            }
        });
        table.row();
        Table categsTable = new Table();
        float sideDimen = pad * 9;
        for (PeriodicTableCategoryEnum categoryEnum : PeriodicTableCategoryEnum.values()) {
            Table cat = new Table();
            cat.add(GraphicUtils.getImage(PeriodicTableSpecificResource.notfound));
            categsTable.add(cat).width(sideDimen).height(sideDimen).pad(pad);
        }
        table.add(categsTable);
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
