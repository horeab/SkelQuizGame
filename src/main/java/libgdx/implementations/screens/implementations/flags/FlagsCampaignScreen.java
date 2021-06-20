package libgdx.implementations.screens.implementations.flags;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.google.gson.Gson;
import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignLevelEnumService;
import libgdx.campaign.CampaignStoreService;
import libgdx.constants.Language;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
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
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.screen.AbstractScreen;
import libgdx.skelgameimpl.skelgame.SkelGameLabel;
import libgdx.skelgameimpl.skelgame.SkelGameRatingService;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

public class FlagsCampaignScreen extends AbstractScreen<FlagsScreenManager> {

    private CampaignStoreService campaignStoreService = new CampaignStoreService();
    private Table allTable;
    private FlagsSettings flagsSettings;

    public FlagsCampaignScreen() {
        initFlagsSettings();
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
        FlagsContainers.setBackgroundDiff(flagsSettings, getBackgroundStage());
    }


    private void initFlagsSettings() {
        if (flagsSettings == null) {
            String json = new CampaignStoreService().getJson();
            if (StringUtils.isNotBlank(json)) {
                flagsSettings = new Gson().fromJson(json, FlagsSettings.class);
            } else {
                flagsSettings = new FlagsSettings();
            }
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

        MyWrappedLabel titleLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(
                        FontColor.WHITE.getColor(),
                        FontColor.BLACK.getColor(),
                        FontConfig.FONT_SIZE * (Game.getInstance().getAppInfoService().getLanguage().equals(Language.sv.name()) ?
                                2.0f : 2.3f),
                        5f))
                .setText(Game.getInstance().getAppInfoService().getAppName()).build());

        float marginDimen = MainDimen.vertical_general_margin.getDimen();
        table.add(titleLabel).padTop(marginDimen * 2).padBottom(marginDimen * 2).colspan(1).row();

        table.row();
        table.add(addDifficultyButtons()).padBottom(marginDimen * 7);
        table.row();
        Table btnTable0 = new Table();
        addButtonToTable(btnTable0, getCampaignLevel(0));
        btnTable0.add().width(ScreenDimensionsManager.getScreenWidth(5));
        addButtonToTable(btnTable0, getCampaignLevel(2));
        btnTable0.add().width(ScreenDimensionsManager.getScreenWidth(5));
        addButtonToTable(btnTable0, getCampaignLevel(1));
        table.add(btnTable0).padBottom(marginDimen * 2);
        table.row();
        Table btnTable1 = new Table();
        btnTable1.add().width(ScreenDimensionsManager.getScreenWidth(20));
        addButtonToTable(btnTable1, getCampaignLevel(4));
        btnTable1.add().width(ScreenDimensionsManager.getScreenWidth(15));
        addButtonToTable(btnTable1, getCampaignLevel(3));
        btnTable1.add().width(ScreenDimensionsManager.getScreenWidth(20));
        table.add(btnTable1);
        return table;
    }

    private FlagsCampaignLevelEnum getCampaignLevel(int categNr) {
        return FlagsCampaignLevelEnum.valueOf("LEVEL_" + flagsSettings.getFlagsDifficultyLevel().getIndex() + "_" + categNr);
    }

    private Table addDifficultyButtons() {
        Table table = new Table();
        float marginDimen = MainDimen.horizontal_general_margin.getDimen();
        for (int diff = 0; diff < 3; diff++) {
            MyButton difficultyButton = createDifficultyButton(diff);
            table.add(difficultyButton).width(difficultyButton.getWidth()).pad(marginDimen).height(difficultyButton.getHeight());
        }
        return table;
    }

    private MyButton createDifficultyButton(final int diff) {
        String labelText = diff + "";
        FontColor fontColor = FontColor.WHITE;
        if (diff == 0) {
            labelText = MainGameLabel.l_easy.getText();
            fontColor = FontColor.LIGHT_GREEN;
        } else if (diff == 1) {
            labelText = MainGameLabel.l_normal.getText();
            fontColor = FontColor.LIGHT_BLUE;
        } else if (diff == 2) {
            labelText = MainGameLabel.l_difficult.getText();
            fontColor = FontColor.RED;
        }
        MyButton btn = new ImageButtonBuilder(GameButtonSkin.valueOf("FLAGS_DIFF_LEVEL_" + diff), getAbstractScreen())
                .setFontConfig(new FontConfig(
                        flagsSettings.getFlagsDifficultyLevel().getIndex() == diff ? fontColor.getColor() : FontColor.WHITE.getColor(),
                        FontColor.BLACK.getColor(),
                        FontConfig.FONT_SIZE * 1f,
                        3f))
                .setFixedButtonSize(GameButtonSize.FLAGS_MENU_BUTTON)
                .setWrappedText(labelText, ScreenDimensionsManager.getScreenWidth(GameButtonSize.FLAGS_MENU_BUTTON.getWidth()))
                .build();
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                flagsSettings.setFlagsDifficultyLevel(FlagsDifficultyLevel.valueOf("_" + diff));
                campaignStoreService.putJson(new Gson().toJson(flagsSettings));
                FlagsContainers.setBackgroundDiff(flagsSettings, getBackgroundStage());
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
            float labelWidth = categButton.getWidth() * 1.15f;
            containerTable
                    .add(FlagsContainers.createFlagsCounter(leftCountriesToPlay, labelWidth, MainResource.popup_background))
                    .padTop(MainDimen.vertical_general_margin.getDimen() * 3)
                    .width(labelWidth).height(categButton.getHeight() / 3);
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
        Utils.createChangeLangPopup(new Runnable() {
            @Override
            public void run() {
                FlagsContainers.reset();
            }
        });
    }
}
