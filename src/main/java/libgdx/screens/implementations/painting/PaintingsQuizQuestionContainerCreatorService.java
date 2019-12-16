package libgdx.screens.implementations.painting;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.paintings.PaintingsSpecificResource;
import libgdx.implementations.skelgame.gameservice.DependentQuizQuestionContainerCreatorService;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameControlsCreatorService;
import libgdx.implementations.skelgame.gameservice.QuizQuestionContainerCreatorService;
import libgdx.resources.FontManager;
import libgdx.resources.Resource;
import libgdx.screens.GameScreen;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class PaintingsQuizQuestionContainerCreatorService extends DependentQuizQuestionContainerCreatorService {

    public PaintingsQuizQuestionContainerCreatorService(GameContext gameContext, GameScreen abstractGameScreen) {
        super(gameContext, abstractGameScreen);
    }

    @Override
    protected void setContainerBackground() {
        questionContainer.setBackground(GraphicUtils.getNinePatch(PaintingsSpecificResource.question_background));
    }

    @Override
    protected MyWrappedLabelConfigBuilder getMyWrappedLabelConfigBuilder(Image questionImage, String questionToBeDisplayed) {
        MyWrappedLabelConfigBuilder myWrappedLabelConfigBuilder = new MyWrappedLabelConfigBuilder().setText(questionToBeDisplayed);
        if (questionImage == null) {
            myWrappedLabelConfigBuilder.setFontScale(FontManager.calculateMultiplierStandardFontSize(1.2f));
        }
        float bigFontDim = FontManager.getBigFontDim();
        myWrappedLabelConfigBuilder.setFontScale(questionToBeDisplayed.length() > 43 ? bigFontDim / 1.2f : bigFontDim);
        myWrappedLabelConfigBuilder.setFontConfig(new FontConfig(
                FontColor.WHITE.getColor(),
                FontColor.BLACK.getColor(),
                2f));
        return myWrappedLabelConfigBuilder;
    }

}
