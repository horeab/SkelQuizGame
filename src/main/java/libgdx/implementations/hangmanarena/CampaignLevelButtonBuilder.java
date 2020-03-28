package libgdx.implementations.hangmanarena;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.campaign.*;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.labelimage.LabelImage;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.hangman.HangmanSpecificResource;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.implementations.skelgame.SkelGameButtonSize;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.resources.*;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.screens.implementations.hangman.HangmanGameScreen;
import libgdx.screens.implementations.hangmanarena.HangmanArenaGameScreen;
import libgdx.utils.EnumUtils;
import org.apache.commons.lang3.StringUtils;

public class CampaignLevelButtonBuilder extends ButtonBuilder {

    public static final int AVAILABLE_HINTS = 2;
    private AbstractScreen abstractScreen;
    private CampaignStoreLevel level;
    private CampaignLevel levelEnum;
    private float fontDimen = FontManager.calculateMultiplierStandardFontSize(0.8f);
    private boolean levelLocked;
    private CampaignLevelEnumService campaignLevelEnumService;

    public CampaignLevelButtonBuilder(AbstractScreen abstractScreen, CampaignLevel levelEnum, CampaignStoreLevel level) {
        this.abstractScreen = abstractScreen;
        this.level = level;
        this.levelEnum = levelEnum;
        this.campaignLevelEnumService = new CampaignLevelEnumService(levelEnum);
    }

    public CampaignLevelButtonBuilder setLevelLocked(boolean levelLocked) {
        this.levelLocked = levelLocked;
        return this;
    }

    @Override
    public MyButton build() {
        setFixedButtonSize(SkelGameButtonSize.CAMPAIGN_LEVEL_ROUND_IMAGE);
        setButtonSkin(campaignLevelEnumService.getButtonSkin(GameButtonSkin.class));
        addLevelInfo();
        CampaignLevelEnumService enumService = new CampaignLevelEnumService(levelEnum);
        QuestionConfig questionConfig = enumService.getQuestionConfig(HangmanArenaGameScreen.TOTAL_QUESTIONS, 2);
        addClickListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                HangmanArenaGame.getInstance().getScreenManager().showCampaignGameScreen(
                        new GameContextService().createGameContext(questionConfig), levelEnum
                );
            }
        });
        MyButton myButton = super.build();
        myButton.setDisabled(levelLocked);
        return myButton;
    }

    private void addLevelInfo() {
        Table table = new Table();
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        if (level != null && level.getStatus() == CampaignLevelStatusEnum.FINISHED.getStatus()) {
            Table starsBar = createStarsBar();
            table.add(starsBar).height(starsBar.getHeight()).width(starsBar.getWidth()).padBottom(verticalGeneralMarginDimen / 2).row();
        }
        Table iconTable = createIconTable();
        table.add(iconTable).row();
        if (!levelLocked && StringUtils.isNotBlank(campaignLevelEnumService.getLabelText())) {
            table.add(createTextTable()).padTop(verticalGeneralMarginDimen / 2).row();
        }
        addCenterTextImageColumn(table);
    }

    private LabelImage createTextTable() {
        LabelImage textTable = createTextTable(campaignLevelEnumService.getLabelText(), MainDimen.horizontal_general_margin.getDimen() * 17, fontDimen);
        textTable.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
        return textTable;
    }

    private Table createStarsBar() {
        Table table = new StarsBarCreator(level.getScore()).createHorizontalStarsBar(MainDimen.vertical_general_margin.getDimen() * 3f);
        table.setBackground(GraphicUtils.getNinePatch(Resource.stars_table_background));
        return table;
    }


    private float getIconDimen() {
        return MainDimen.horizontal_general_margin.getDimen() * 10;
    }

    private Table createIconTable() {
        Table table = new Table();
        Table iconTable = new Table();
        float iconDimen = getIconDimen();
        Res icon = getIcon();
        if (icon.getPath().equals(HangmanArenaSpecificResource.bomb.getPath())) {
            Stack bombWithFire = new Stack();
            bombWithFire.addActor(GraphicUtils.getImage(icon));
            Image image = GraphicUtils.getImage(HangmanArenaSpecificResource.fire);
            Table animTable = new Table();
            float animDimen = iconDimen / 2.3f;
            new ActorAnimation(image, abstractScreen).animateZoomInZoomOut(0.3f);
            animTable.add(image).padLeft(MainDimen.horizontal_general_margin.getDimen() * 4).padBottom(MainDimen.vertical_general_margin.getDimen() * 5).width(animDimen).height(animDimen);
            bombWithFire.addActor(animTable);
            iconTable.add(bombWithFire).width(iconDimen / 1.5f).height(iconDimen / 1.5f);
        } else {
            iconTable.add(GraphicUtils.getImage(icon)).width(iconDimen).height(iconDimen);
        }
        if (levelLocked) {
            Stack stack = new Stack();
            Image image = GraphicUtils.getImage(MainResource.lock);
            stack.addActor(image);
            table.add(stack).width(iconDimen).height(iconDimen);
        } else {
            table.add(iconTable);
        }
        return table;
    }

    private Res getIcon() {
        Res res = HangmanArenaSpecificResource.bomb;
        CampaignLevelEnumService enumService = new CampaignLevelEnumService(levelEnum);
        if (enumService.getCategory() != null) {
            res = (SpecificResource) EnumUtils.getEnumValue(HangmanArenaGame.getInstance().getSubGameDependencyManager().getSpecificResourceTypeEnum(), "campaign_level_" + enumService.getDifficulty() + "_" + enumService.getCategory());
        }
        return res;
    }
}
