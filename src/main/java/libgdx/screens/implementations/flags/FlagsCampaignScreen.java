package libgdx.screens.implementations.flags;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.google.gson.Gson;
import libgdx.campaign.*;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.flags.FlagsCampaignLevelEnum;
import libgdx.implementations.flags.FlagsDifficultyLevel;
import libgdx.implementations.flags.FlagsGame;
import libgdx.implementations.flags.FlagsSpecificResource;
import libgdx.implementations.skelgame.*;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.implementations.skelgame.question.Question;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlagsCampaignScreen extends AbstractScreen<FlagsScreenManager> {

    private CampaignService campaignService = new CampaignService();
    private CampaignStoreService campaignStoreService = new CampaignStoreService();
    private List<CampaignStoreLevel> allCampaignLevelStores;
    private Table allTable;
    private FlagsSettings flagsSettings;

    public FlagsCampaignScreen() {
        initFlagsSettings();
        allCampaignLevelStores = campaignService.processAndGetAllLevels();
        FlagsContainers.init();
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

    private void initFlagsSettings() {
        String json = campaignStoreService.getJson();
        if (StringUtils.isNotBlank(json)) {
            flagsSettings = new Gson().fromJson(json, FlagsSettings.class);
        } else {
            flagsSettings = new FlagsSettings();
        }
    }

    private Table createAllTable() {
        if (allTable != null) {
            allTable.clearChildren();
        } else {
            allTable = new Table();
        }
        allTable.setFillParent(true);
        allTable.add(createCategButtons());
        boolean allLevelsFinished = true;
        for (FlagsCampaignLevelEnum flagsCampaignLevelEnum : FlagsCampaignLevelEnum.values()) {
            if (!campaignStoreService.isQuestionAlreadyPlayed(flagsCampaignLevelEnum.getName())) {
                allLevelsFinished = false;
                break;
            }
        }
        if (allLevelsFinished) {
            new LevelFinishedPopup(this, SkelGameLabel.game_finished.getText()).addToPopupManager();
        }
        return allTable;
    }


    private Table createCategButtons() {
        Table table = new Table();
        table.row();
        table.add(addDifficultyButtons()).padBottom(MainDimen.vertical_general_margin.getDimen() * 5);
        table.row();
        Table btnTable0 = new Table();
        addButtonToTable(btnTable0, getCampaignLevel(1));
        btnTable0.add().width(ScreenDimensionsManager.getScreenWidthValue(5));
        addButtonToTable(btnTable0, getCampaignLevel(0));
        btnTable0.add().width(ScreenDimensionsManager.getScreenWidthValue(5));
        addButtonToTable(btnTable0, getCampaignLevel(2));
        table.add(btnTable0).padBottom(MainDimen.vertical_general_margin.getDimen() * 3);
        table.row();
        Table btnTable1 = new Table();
        btnTable1.add().width(ScreenDimensionsManager.getScreenWidthValue(10));
        addButtonToTable(btnTable1, getCampaignLevel(3));
        btnTable1.add().width(ScreenDimensionsManager.getScreenWidthValue(10));
        addButtonToTable(btnTable1, getCampaignLevel(4));
        btnTable1.add().width(ScreenDimensionsManager.getScreenWidthValue(30));
        table.add(btnTable1);
        return table;
    }

    private FlagsCampaignLevelEnum getCampaignLevel(int categNr) {
        return FlagsCampaignLevelEnum.valueOf("LEVEL_" + flagsSettings.getFlagsDifficultyLevel().getIndex() + "_" + categNr);
    }

    private Table addDifficultyButtons() {
        Table table = new Table();
        for (int diff = 0; diff < 3; diff++) {
            MyButton difficultyButton = createDifficultyButton(diff);
            table.add(difficultyButton).width(difficultyButton.getWidth()).height(difficultyButton.getHeight());
        }
        return table;
    }

    private MyButton createDifficultyButton(final int diff) {
        String labelText = diff + " ";
        MyButton btn = new ImageButtonBuilder(GameButtonSkin.valueOf("FLAGS_DIFF_LEVEL_" + diff), getAbstractScreen())
                .setFontScale(FontManager.getSmallFontDim())
                .setFontColor(flagsSettings.getFlagsDifficultyLevel().getIndex() == diff ? FontColor.RED : FontColor.BLACK)
                .setFixedButtonSize(GameButtonSize.FLAGS_MENU_BUTTON)
                .setWrappedText(labelText, ScreenDimensionsManager.getScreenWidthValue(GameButtonSize.FLAGS_MENU_BUTTON.getWidth()))
                .build();
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                flagsSettings.setFlagsDifficultyLevel(FlagsDifficultyLevel.valueOf("_" + diff));
                campaignStoreService.putJson(new Gson().toJson(flagsSettings));
                createAllTable();
            }
        });
        return btn;
    }

    private void addButtonToTable(Table table, FlagsCampaignLevelEnum campaignLevel) {
        MyButton categButton = createCategButton(campaignLevel);

        Table containerTable = new Table();
        containerTable.add(categButton).width(categButton.getWidth()).height(categButton.getHeight()).row();

        int leftCountriesToPlay = FlagsContainers.getAllQuestions(campaignLevel).size();
        if (campaignStoreService.isQuestionAlreadyPlayed(campaignLevel.getName())) {
            containerTable.add(GraphicUtils.getImage(FlagsSpecificResource.star))
                    .padTop(MainDimen.vertical_general_margin.getDimen() * 3)
                    .width(categButton.getHeight() / 2).height(categButton.getHeight() / 2);
        } else {
            containerTable.add(FlagsContainers.createFlagsCounter(leftCountriesToPlay, categButton.getWidth(), MainResource.popup_background))
                    .padTop(MainDimen.vertical_general_margin.getDimen() * 3)
                    .width(categButton.getWidth()).height(categButton.getHeight() / 5);
        }


        table.add(containerTable).padBottom(MainDimen.vertical_general_margin.getDimen() * 5f)
                .width(categButton.getWidth()).height(categButton.getHeight());
    }

    private MyButton createCategButton(final CampaignLevel campaignLevel) {
        CampaignLevelEnumService enumService = new CampaignLevelEnumService(campaignLevel);
        String labelText = enumService.getLabelText();
        MyButton categBtn = new ImageButtonBuilder(GameButtonSkin.valueOf("FLAGS_CATEG" + enumService.getCategory()), getAbstractScreen())
                .padBetweenImageAndText(1.4f)
                .textBackground(null)
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(0.9f))
                .setText(labelText)
                .setFontColor(FontColor.BLACK)
                .setFixedButtonSize(GameButtonSize.FLAGS_MENU_BUTTON)
                .build();
        categBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                List<Question> allQuestions = FlagsContainers.getAllQuestions(campaignLevel);
                Collections.shuffle(allQuestions);
                GameContext gameContext = new GameContextService().createGameContext(allQuestions);
                FlagsGame.getInstance().getScreenManager().showCampaignGameScreen(gameContext, campaignLevel);
            }
        });
        return categBtn;
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
