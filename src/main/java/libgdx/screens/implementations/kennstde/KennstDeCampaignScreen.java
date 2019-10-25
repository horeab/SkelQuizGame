package libgdx.screens.implementations.kennstde;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import libgdx.campaign.*;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.geoquiz.QuizGameSpecificResource;
import libgdx.implementations.hangman.HangmanCampaignLevelEnum;
import libgdx.implementations.hangman.HangmanGame;
import libgdx.implementations.hangman.HangmanQuestionCategoryEnum;
import libgdx.implementations.hangman.HangmanSpecificResource;
import libgdx.implementations.kennstde.KennstDeCampaignLevelEnum;
import libgdx.implementations.kennstde.KennstDeQuestionCategoryEnum;
import libgdx.implementations.kennstde.KennstDeSpecificResource;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.implementations.skelgame.SkelGameLabel;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.implementations.skelgame.gameservice.QuizStarsService;
import libgdx.resources.FontManager;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.screen.AbstractScreen;
import libgdx.screens.implementations.hangman.HangmanGameScreen;
import libgdx.screens.implementations.hangman.HangmanLevelFinishedPopup;
import libgdx.screens.implementations.hangman.HangmanScreenManager;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

import java.util.ArrayList;
import java.util.List;

public class KennstDeCampaignScreen extends AbstractScreen<HangmanScreenManager> {

    private CampaignService campaignService = new CampaignService();
    private List<CampaignStoreLevel> allCampaignLevelStores;

    public KennstDeCampaignScreen() {
        allCampaignLevelStores = campaignService.getFinishedCampaignLevels();
    }

    @Override
    public void buildStage() {
        Table table = new Table();
        table.setFillParent(true);
        table.add(createAllTable()).width(ScreenDimensionsManager.getScreenWidth());
        table.setBackground(GraphicUtils.getNinePatch(KennstDeSpecificResource.campaign_background_texture));
        addActor(table);
        new BackButtonBuilder().addHoverBackButton(this);
    }

    private Table createAllTable() {
        Table table = new Table();
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        Table titleTable = new Table();
        MyWrappedLabel titleLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(
                FontColor.WHITE.getColor(),
                FontColor.BLACK.getColor(),
                FontConfig.FONT_SIZE * 2,
                4f)).setText(Game.getInstance().getAppInfoService().getAppName()).build());
        Stack stack = new Stack();
        Image titleBackr = GraphicUtils.getImage(KennstDeSpecificResource.title_background);
        stack.addActor(titleBackr);
        stack.addActor(titleLabel);
        table.add(stack)
                .padBottom(-verticalGeneralMarginDimen * 10)
                .width(ScreenDimensionsManager.getScreenWidthValue(90))
                .height(ScreenDimensionsManager.getNewHeightForNewWidth(ScreenDimensionsManager.getScreenWidthValue(90), titleBackr)).row();

        table.add(createCampaignButtons()).padTop(ScreenDimensionsManager.getScreenHeightValue(20));
        return table;
    }

    private Table createCampaignButtons() {
        Table table = new Table();
        int totalLevels = KennstDeCampaignLevelEnum.values().length;
        for (int i = 0; i < totalLevels; i++) {
            Integer category = CampaignLevelEnumService.getCategory(KennstDeCampaignLevelEnum.values()[i].name());
            float rowHeight = ScreenDimensionsManager.getScreenHeightValue(15);
            if (i % 2 == 0) {
                table.add(createCampaignBtn(category)).width(ScreenDimensionsManager.getScreenWidth() / 2).height(rowHeight);
                table.add().width(ScreenDimensionsManager.getScreenWidth() / 2);
            } else {
                table.add();
                table.add(createCampaignBtn(category)).height(rowHeight);
            }
            table.row();
        }
        return table;
    }

    private Table createCampaignBtn(int cat) {
        Table table = new Table();
        boolean btnUnlocked = false;
        for (CampaignStoreLevel campaignStoreLevel : allCampaignLevelStores) {
            if (cat == CampaignLevelEnumService.getCategory(campaignStoreLevel.getName())) {
                btnUnlocked = true;
                break;
            }
        }
        if (btnUnlocked) {
            MyButton btn = new ButtonBuilder().setText(new SpecificPropertiesUtils().getQuestionCategoryLabel(cat)).build();
            table.add(btn);
        }
        return table;
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

}
