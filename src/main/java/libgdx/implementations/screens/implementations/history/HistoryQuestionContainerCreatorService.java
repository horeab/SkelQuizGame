package libgdx.implementations.screens.implementations.history;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import org.apache.commons.lang3.StringUtils;

import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.ButtonSkin;
import libgdx.controls.button.MainButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.history.HistoryPreferencesService;
import libgdx.implementations.history.HistorySpecificResource;
import libgdx.implementations.skelgame.GameButtonSize;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.QuestionContainerCreatorService;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;

public class HistoryQuestionContainerCreatorService extends QuestionContainerCreatorService<HistoryGameService> {

    private HistoryGameScreen historyGameScreen;
    private HistoryPreferencesService historyPreferencesService = new HistoryPreferencesService();

    public HistoryQuestionContainerCreatorService(GameContext gameContext, HistoryGameScreen abstractGameScreen) {
        super(gameContext, abstractGameScreen);
        this.historyGameScreen = abstractGameScreen;
    }

    @Override
    public Table createAnswerOptionsTable() {
        return new Table();
    }

    @Override
    public Table createQuestionTable() {
        Table questionTable = createTimeline();
        questionTable.setBackground(GraphicUtils.getNinePatch(MainResource.btn_lowcolor_up));
        return questionTable;
    }

    @Override
    protected void setContainerBackground() {
    }

    private Table createTimeline() {
        Table table = new Table();
        float optionHeight = optionHeight();
        float optionSideWidth = ScreenDimensionsManager.getScreenWidthValue(45);
        float timelineLineWidth = ScreenDimensionsManager.getScreenWidthValue(3);
        int i = 0;
        float historyTimelineArrowWidth = GameButtonSize.HISTORY_TIMELINE_ARROW.getWidth();
        for (GameQuestionInfo q : gameContext.getCurrentUserGameUser().getAllQuestionInfos()) {
            String questionString = q.getQuestion().getQuestionString();
            String optionText = HistoryGameService.getOptionText(questionString);
            Table timeLineTable = new Table();
            Table qTable = new Table();
            Table optionBtn = createOptionBtn(optionText, i);
            Table leftContainer = new Table();
            Cell leftCell = leftContainer.add().height(optionBtn.getHeight()).width(optionBtn.getWidth());
            Cell leftArrowCell = leftContainer.add().width(historyTimelineArrowWidth);
            leftContainer.row();
            if (i % 2 == 0) {
                leftCell.setActor(optionBtn);
                leftArrowCell.setActor(createTimelineArrow());
            } else {
                leftArrowCell.reset();
                leftCell.setActor(createAnswImg(i));
            }
            qTable.add(leftContainer).width(optionSideWidth);
            qTable.add(timeLineTable).height(optionHeight).width(timelineLineWidth);
            timeLineTable.setBackground(historyGameScreen.getBackgroundForTimeline(questionString));
            timeLineTable.setName(getTimelineItemName(i));
            Table rightContainer = new Table();
            Cell rightArrowCell = rightContainer.add().width(historyTimelineArrowWidth);
            Cell rightCell = rightContainer.add().height(optionBtn.getHeight()).width(optionBtn.getWidth());
            rightContainer.row();
            if (i % 2 != 0) {
                rightArrowCell.setActor(createTimelineArrow());
                rightCell.setActor(optionBtn);
            } else {
                rightArrowCell.reset();
                rightCell.setActor(createAnswImg(i));
            }
            qTable.add(rightContainer).width(optionSideWidth);
            qTable.setBackground(getTimelineItemBackgr(questionString, i));
            table.add(qTable).width(optionSideWidth * 2).height(optionHeight).row();
            i++;
        }
        return table;
    }

    private String getTimelineItemName(int i) {
        return "timeLineTable" + i;
    }

    public static float optionHeight() {
        return ScreenDimensionsManager.getScreenHeightValue(12);
    }

    private Table createAnswImg(int index) {
        Table table = new Table();
        Image img = createOptImg(index);
        table.add(img);
        table.add(img).width(img.getWidth()).height(img.getHeight());
        table.setName(getOptionImageName(index));
        return table;
    }

    private Image createOptImg(int index) {
        Res res = HistorySpecificResource.timeline_opt_unknown;
        try {
            if (historyPreferencesService.getAllLevelsPlayed().contains(index)) {
                res = HistorySpecificResource.valueOf("i" + index);
            }
        } catch (Exception ignore) {
        }
        Image image = GraphicUtils.getImage(res);
        image.setWidth(GameButtonSize.HISTORY_ANSW_IMG.getWidth());
        image.setHeight(GameButtonSize.HISTORY_ANSW_IMG.getHeight());
        return image;
    }

    private Image createTimelineArrow() {
        Image image = GraphicUtils.getImage(HistorySpecificResource.arrow_left);
        image.setWidth(GameButtonSize.HISTORY_TIMELINE_ARROW.getWidth());
        image.setHeight(GameButtonSize.HISTORY_TIMELINE_ARROW.getHeight());
        return image;
    }

    private Table createOptionBtn(String optionText, int index) {
        Table table = new Table();
        MyButton btn = new ButtonBuilder()
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(1.2f))
                .setWrappedText(optionText, GameButtonSize.HISTORY_CLICK_ANSWER_OPTION.getWidth())
                .setFontColor(FontColor.BLACK)
                .setButtonSkin(MainButtonSkin.DEFAULT)
                .setDisabled(historyPreferencesService.getAllLevelsPlayed().contains(index))
                .setFixedButtonSize(GameButtonSize.HISTORY_CLICK_ANSWER_OPTION)
                .build();
        btn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String questionString = historyGameScreen.getCurrentGameQuestionInfo().getQuestion().getQuestionString();
                String currentQuestionCorrectAnswer = HistoryGameService.getOptionText(questionString);
                Integer currentQuestion = historyGameScreen.getCurrentQuestion();
                if (currentQuestionCorrectAnswer.equals(optionText)) {
                    Table imgTable = historyGameScreen.getRoot().findActor(getOptionImageName(currentQuestion));
                    Actor oldImg = imgTable.getChildren().get(0);
                    oldImg.addAction(Actions.sequence(Actions.fadeOut(0.2f), Utils.createRemoveActorAction(oldImg)));
                    Image img = createOptImg(currentQuestion);
                    imgTable.clear();
                    img.setVisible(false);
                    imgTable.add(img).width(img.getWidth()).height(img.getHeight());
                    new ActorAnimation(img, historyGameScreen).animateFastFadeIn();
                    historyPreferencesService.setLevelWon(currentQuestion);
                } else {
                    historyPreferencesService.setLevelLost(currentQuestion);
                }
                Table item = historyGameScreen.getRoot().findActor(getTimelineItemName(currentQuestion));
                item.setBackground(getTimelineItemBackgr(questionString, currentQuestion));
            }
        });
        table.add(btn).width(btn.getWidth()).height(btn.getHeight());
        return btn;
    }

    private String getOptionImageName(Integer currentQuestion) {
        return "optImage" + currentQuestion;
    }

    private Drawable getTimelineItemBackgr(String questionString, Integer questionNr) {
        Res res;
        if (historyPreferencesService.getLevelsLost().contains(questionNr)) {
            res = HistorySpecificResource.timeline_opt_wrong;
        } else if (historyPreferencesService.getLevelsWon().contains(questionNr)) {
            res = HistorySpecificResource.timeline_opt_correct;
        } else {
            res = HistorySpecificResource.valueOf("timeline" + historyGameScreen.getTimelineForYear(HistoryGameService.getOptionRawText(questionString)) + "_opt_background");
        }
        return GraphicUtils.getNinePatch(res);
    }


    @Override
    public ButtonSkin correctAnswerSkin() {
        return GameButtonSkin.ANSWER_IMAGE_CLICK_CORRECT;
    }

    @Override
    public ButtonSkin wrongAnswerSkin() {
        return GameButtonSkin.ANSWER_IMAGE_CLICK_WRONG;
    }

    @Override
    public int getNrOfAnswerRows() {
        return 2;
    }

    @Override
    public int getNrOfAnswersOnRow() {
        return 2;
    }

    @Override
    protected MyButton createAnswerButton(final String answer) {
        final MyButton button = new ImageButtonBuilder(GameButtonSkin.ANSWER_IMAGE_CLICK, Game.getInstance().getAbstractScreen())
                .setFontColor(FontColor.BLACK)
                .setText(StringUtils.capitalize(answer))
                .setFixedButtonSize(GameButtonSize.IMAGE_CLICK_ANSWER_OPTION)
                .build();
        button.getCenterRow().setVisible(false);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                button.toFront();
                button.getCenterRow().setVisible(true);
            }
        });
        return button;
    }

}