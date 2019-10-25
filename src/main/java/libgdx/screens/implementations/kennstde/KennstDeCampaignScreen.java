package libgdx.screens.implementations.kennstde;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import libgdx.campaign.CampaignLevelEnumService;
import libgdx.campaign.CampaignLevelStatusEnum;
import libgdx.campaign.CampaignService;
import libgdx.campaign.CampaignStoreLevel;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.kennstde.KennstDeCampaignLevelEnum;
import libgdx.implementations.kennstde.KennstDeSpecificResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.screen.AbstractScreen;
import libgdx.screens.implementations.hangman.HangmanScreenManager;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class KennstDeCampaignScreen extends AbstractScreen<HangmanScreenManager> {

    private CampaignService campaignService = new CampaignService();
    private List<CampaignStoreLevel> allCampaignLevelStores;

    public KennstDeCampaignScreen() {
        allCampaignLevelStores = campaignService.processAndGetAllLevels();
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
        boolean isBtnCurrent = false;
        for (CampaignStoreLevel campaignStoreLevel : allCampaignLevelStores) {
            if (cat == CampaignLevelEnumService.getCategory(campaignStoreLevel.getName())) {
                if (campaignStoreLevel.getStatus() == CampaignLevelStatusEnum.IN_PROGRESS.getStatus()) {
                    isBtnCurrent = true;
                }
                btnUnlocked = true;
                break;
            }
        }
        if (btnUnlocked) {
            MyButton btn = new ButtonBuilder().setText(new SpecificPropertiesUtils().getQuestionCategoryLabel(cat)).build();
            float btnDimen = MainDimen.horizontal_general_margin.getDimen() * 7.5f;
            btn.setHeight(btnDimen);
            btn.setWidth(btnDimen);
            btn.setOrigin(Align.center);
            if (isBtnCurrent) {
                btn.setTransform(true);
                new ActorAnimation(btn, this).animateZoomInZoomOut(0.5f);
            }
            table.add(btn).width(btn.getWidth()).height(btn.getHeight());
        }
        return table;
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

}
