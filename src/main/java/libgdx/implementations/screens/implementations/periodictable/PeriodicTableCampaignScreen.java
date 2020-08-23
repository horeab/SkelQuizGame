package libgdx.implementations.screens.implementations.periodictable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.campaign.CampaignService;
import libgdx.campaign.CampaignStoreService;
import libgdx.constants.Language;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.periodictable.*;
import libgdx.implementations.periodictable.spec.ChemicalElement;
import libgdx.implementations.skelgame.*;
import libgdx.implementations.skelgame.gameservice.CreatorDependenciesContainer;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.implementations.skelgame.question.Question;
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

import java.util.*;

public class PeriodicTableCampaignScreen extends AbstractScreen<PeriodicTableScreenManager> {

    private CampaignService campaignService = new CampaignService();
    private CampaignStoreService campaignStoreService = new CampaignStoreService();
    private List<ChemicalElement> chemicalElements;
    private PeriodicTableCreatorDependencies creator = (PeriodicTableCreatorDependencies) CreatorDependenciesContainer.getCreator(PeriodicTableCreatorDependencies.class);

    public PeriodicTableCampaignScreen() {
        chemicalElements = creator.getElements();
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
        startGameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                List<Question> questions = new ArrayList<>(CreatorDependenciesContainer.getCreator(PeriodicTableCreatorDependencies.class).getQuestionCreator().getAllQuestions());
                List<Question> notPlayedQuestions = new ArrayList<>();
                for (Question question : questions) {
                    if (!campaignStoreService.isQuestionAlreadyPlayed(PeriodicTableContainers.getQuestionId(question.getQuestionLineInQuestionFile(), question.getQuestionCategory(), question.getQuestionDifficultyLevel()))) {
                        notPlayedQuestions.add(question);
                    }
                }
                Collections.shuffle(notPlayedQuestions);
                GameContext gameContext = new GameContextService().createGameContext(notPlayedQuestions.toArray(new Question[notPlayedQuestions.size()]));
                PeriodicTableGame.getInstance().getScreenManager().showCampaignGameScreen(gameContext, null);
            }
        });

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
                .setText(PeriodicTableContainers.getTotalNrOfElementsFound() + "/" + chemicalElements.size())
                .setWidth(extraWidth)
                .build()));
        table.add(controlsTable).padBottom(dimen).row();
        long totalStarsWon = 0;
        float btnSide = getElSideDimen();
        int i = 0;
        Table elementsTable = new Table();
        int elementsPerRow = (int) Math.floor(ScreenDimensionsManager.getScreenWidth() / btnSide);
        List<ChemicalElement> sortedAlphaElements = new ArrayList<>(chemicalElements);
        Collections.sort(sortedAlphaElements, new Comparator<ChemicalElement>() {
            @Override
            public int compare(ChemicalElement r1, ChemicalElement r2) {
                return creator.getNameText(r1.getAtomicNumber()).compareTo(creator.getNameText(r2.getAtomicNumber()));
            }
        });

        for (ChemicalElement e : sortedAlphaElements) {
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
        float elSideDimen = getElSideDimen();
        MyWrappedLabel nameLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(Color.BLACK, getNameFontSize()))
                .setText(creator.getNameText(e.getAtomicNumber()))
                .setWidth(elSideDimen)
                .build());
        table.add(nameLabel).padBottom(pad * 4);
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
        int correctAnswersForElement = PeriodicTableContainers.getTotalCorrectAnswersForElement(e.getAtomicNumber());
        if (correctAnswersForElement == PeriodicTableCategoryEnum.values().length) {
            table.setBackground(GraphicUtils.getNinePatch(PeriodicTableSpecificResource.all_found));
        } else {
            table.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
            for (int j = 0; j < PeriodicTableCategoryEnum.values().length; j++) {
                Table cat = new Table();
                cat.setWidth(sideDimen);
                cat.setHeight(sideDimen);
                if (j <= correctAnswersForElement - 1) {
                    cat.setBackground(GraphicUtils.getNinePatch(PeriodicTableSpecificResource.success));
                } else {
                    cat.setBackground(GraphicUtils.getNinePatch(PeriodicTableSpecificResource.notfound));
                }
                categsTable.add(cat).width(sideDimen).height(sideDimen).pad(pad);
            }
            table.add(categsTable).width(elSideDimen).height(sideDimen);
        }
        return table;
    }

    private float getNameFontSize() {
        float factor = Arrays.asList(Language.zh, Language.ko).contains(Language.valueOf(Game.getInstance().getAppInfoService().getLanguage()))
                ? 0.9f : 1.2f;
        factor = Arrays.asList(Language.ja).contains(Language.valueOf(Game.getInstance().getAppInfoService().getLanguage()))
                ? 1.6f : factor;
        return FontConfig.FONT_SIZE / factor;
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
