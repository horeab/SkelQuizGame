package libgdx.controls.popup;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

import java.util.Random;

import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.screen.AbstractScreen;
import libgdx.screen.AbstractScreenManager;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;

public class ParentalGatePopup extends MyPopup<AbstractScreen, AbstractScreenManager> {

    private final static String IMG_NAME = "ParentalGateImgDragName";
    private static final int NR_OF_OPTIONS = 3;

    private Runnable executeAfterTaskFinishedSuccessfully;
    int optionIndex;
    int responseIndex;

    public ParentalGatePopup(AbstractScreen abstractScreen, Runnable executeAfterTaskFinishedSuccessfully) {
        super(abstractScreen);
        this.executeAfterTaskFinishedSuccessfully = executeAfterTaskFinishedSuccessfully;
        optionIndex = new Random().nextInt(3);
        responseIndex = new Random().nextInt(NR_OF_OPTIONS);
        while (responseIndex == optionIndex) {
            responseIndex = new Random().nextInt(3);
        }
    }

    @Override
    protected void addButtons() {
        float imgDimen = getImgDimen();
        float margin = ScreenDimensionsManager.getScreenWidth(10);
        Table imgTable = new Table();
        imgTable.add(createImg(MainResource.square_red, 0)).height(imgDimen).width(imgDimen);
        imgTable.add(createImg(MainResource.circle_red, 1)).padLeft(margin).padRight(margin).height(imgDimen).width(imgDimen);
        imgTable.add(createImg(MainResource.triangle_red, 2)).height(imgDimen).width(imgDimen);
        getContentTable().add(imgTable).padTop(margin / 2).padBottom(margin / 2);
    }

    @Override
    protected void addText() {
        super.addText();
        addEmptyRowWithMargin(getContentTable());
        MyWrappedLabel instr = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontScale(FontManager.getNormalBigFontDim())
                .setText(getInstrText()).setWidth(getPrefWidth() - getPrefWidth() / 10).setFontColor(FontColor.BLACK).build());
        getContentTable().add(instr).width(instr.getPrefWidth()).row();
    }

    private String getInstrText() {
        return MainGameLabel.valueOf("l_parental_gate_" + optionIndex + "_" + responseIndex).getText();
    }

    protected MyWrappedLabelConfigBuilder getInfoLabelConfigBuilder() {
        MyWrappedLabelConfigBuilder builder = super.getInfoLabelConfigBuilder();
        builder.setFontScale(FontManager.calculateMultiplierStandardFontSize(3f));
        return builder;
    }

    private float getImgDimen() {
        return ScreenDimensionsManager.getScreenWidth(15);
    }

    private Image createImg(Res res, int index) {
        Image img = GraphicUtils.getImage(res);
        img.setName(getImgName(index));
        img.addListener(new DragListener() {

            @Override
            public void dragStart(InputEvent event, float x, float y, int pointer) {
            }

            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                img.toFront();
                img.moveBy(x - img.getWidth() / 2, y - img.getHeight() / 2);
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                getContentTable().findActor(getImgName(0)).setTouchable(Touchable.disabled);
                getContentTable().findActor(getImgName(1)).setTouchable(Touchable.disabled);
                getContentTable().findActor(getImgName(2)).setTouchable(Touchable.disabled);
                float imgDimen = getImgDimen();
                float acceptedDist = imgDimen / 3;
                Image correctImage = getContentTable().findActor(getImgName(responseIndex));
                float correctImageX = correctImage.getX();
                float correctImageY = correctImage.getY();
                if (index == optionIndex &&
                        (correctImageX - acceptedDist < img.getX() && correctImageX + imgDimen + acceptedDist > (img.getX() + imgDimen))
                        &&
                        (correctImageY - acceptedDist < img.getY() && correctImageY + imgDimen + acceptedDist > (img.getY() + imgDimen))) {
                    img.addAction(Actions.moveTo(correctImageX, correctImageY, 0.3f));
                    getScreen().addAction(Actions.delay(1f, Utils.createRunnableAction(executeAfterTaskFinishedSuccessfully)));
                } else {
                    mainLabel.setText(MainGameLabel.l_verification_failed.getText());
                    getScreen().addAction(Actions.delay(1f, Utils.createRunnableAction(new Runnable() {
                        @Override
                        public void run() {
                            hide();
                        }
                    })));
                }
            }
        });
        return img;
    }

    private String getImgName(int index) {
        return IMG_NAME + index;
    }

    @Override
    public float getPrefWidth() {
        return ScreenDimensionsManager.getScreenWidth(90);
    }

    @Override
    public float getPrefHeight() {
        return ScreenDimensionsManager.getScreenHeight(90);
    }

    @Override
    protected String getLabelText() {
        return MainGameLabel.l_ask_parents.getText();
    }
}
