package libgdx.implementations.screens.implementations.history;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import org.apache.commons.lang3.StringUtils;

import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.ButtonSkin;
import libgdx.controls.button.MainButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.history.HistorySpecificResource;
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.skelgame.GameButtonSize;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.ImageClickGameService;
import libgdx.implementations.skelgame.gameservice.QuestionContainerCreatorService;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;

public class HistoryQuestionContainerCreatorService extends QuestionContainerCreatorService<ImageClickGameService> {

    public HistoryQuestionContainerCreatorService(GameContext gameContext, GameScreen abstractGameScreen) {
        super(gameContext, abstractGameScreen);
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
        float optionHeight = ScreenDimensionsManager.getScreenHeightValue(16);
        float optionSideWidth = ScreenDimensionsManager.getScreenWidthValue(45);
        float timelineLineWidth = ScreenDimensionsManager.getScreenWidthValue(3);
        float dimen = MainDimen.vertical_general_margin.getDimen();
        int i = 0;
        float historyTimelineArrowWidth = GameButtonSize.HISTORY_TIMELINE_ARROW.getWidth();
        for (GameQuestionInfo q : gameContext.getCurrentUserGameUser().getAllQuestionInfos()) {
            Table timeLineTable = new Table();
            timeLineTable.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
            Table qTable = new Table();
            Table optionBtn = createOptionBtn();
            Table leftContainer = new Table();
            Cell leftCell = leftContainer.add().height(optionBtn.getHeight()).width(optionBtn.getWidth());
            Cell leftArrowCell = leftContainer.add().width(historyTimelineArrowWidth);
            if (i % 2 == 0) {
                leftCell.setActor(optionBtn);
                leftArrowCell.setActor(createTimelineArrow());
            } else {
                leftArrowCell.reset();
                leftCell.setActor(createAnswImg());
            }
            qTable.add(leftContainer).width(optionSideWidth);
            qTable.add(timeLineTable).height(optionHeight).width(timelineLineWidth);
            Table rightContainer = new Table();
            Cell rightArrowCell = rightContainer.add().width(historyTimelineArrowWidth);
            Cell rightCell = rightContainer.add().height(optionBtn.getHeight()).width(optionBtn.getWidth());
            if (i % 2 != 0) {
                rightArrowCell.setActor(createTimelineArrow());
                rightCell.setActor(optionBtn);
            } else {
                rightArrowCell.reset();
                rightCell.setActor(createAnswImg());
            }
            qTable.add(rightContainer).width(optionSideWidth);
            qTable.setBackground(GraphicUtils.getNinePatch(MainResource.btn_lowcolor_down));
            table.add(qTable).padBottom(-dimen / 2).width(optionSideWidth * 2).height(optionHeight).row();
            i++;
        }
        return table;
    }

    private Image createAnswImg() {
        Image image = GraphicUtils.getImage(HistorySpecificResource.arrow_left);
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

    private Table createOptionBtn() {
        Table table = new Table();
        MyButton btn = new ButtonBuilder()
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(1.2f))
                .setWrappedText("5000 BC", GameButtonSize.HISTORY_CLICK_ANSWER_OPTION.getWidth())
                .setFontColor(FontColor.BLACK)
                .setButtonSkin(MainButtonSkin.DEFAULT)
                .setFixedButtonSize(GameButtonSize.HISTORY_CLICK_ANSWER_OPTION)
                .build();
        table.add(btn).width(btn.getWidth()).height(btn.getHeight());
        return btn;
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