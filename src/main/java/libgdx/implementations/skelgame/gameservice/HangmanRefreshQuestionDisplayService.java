package libgdx.implementations.skelgame.gameservice;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.controls.ScreenRunnable;
import libgdx.controls.button.MyButton;
import libgdx.implementations.hangman.HangmanLabel;
import libgdx.implementations.skelgame.GameDimen;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.resources.FontManager;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ActorPositionManager;
import libgdx.utils.ScreenDimensionsManager;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HangmanRefreshQuestionDisplayService extends RefreshQuestionDisplayService<HangmanGameService> {

    public static final String ACTOR_NAME_HANGMAN_WORD_TABLE = "actor_name_hangman_word_table";

    public HangmanRefreshQuestionDisplayService(AbstractScreen screen, GameContext gameContext, Map<String, MyButton> allAnswerButtons) {
        super(screen, gameContext, allAnswerButtons);
    }

    @Override
    public void refreshQuestion(GameQuestionInfo gameQuestionInfo) {
        try {
            String hangmanWord = gameService.getHangmanWord(gameQuestionInfo.getQuestion().getQuestionString());
            createHangmanWord(hangmanWord, gameQuestionInfo.getAnswerIds(), new HashSet<String>());
        } catch (Exception e) {
            int i = 0;
        }
    }

    private void createHangmanWord(String hangmanWord, Set<String> pressedLetters, Set<String> redLetters) {
        String currentWordState = StringUtils.capitalize(gameService.getCurrentWordState(hangmanWord, pressedLetters));
        Table lettersTable = getLettersTableFromStage();
        lettersTable.reset();
        lettersTable.setName(ACTOR_NAME_HANGMAN_WORD_TABLE);
        float standardWidth = GameDimen.width_hangman_letter.getDimen();
        float calculatedWidth = calculateLetterLabelWidth(standardWidth, currentWordState);
        float fontScale = getFontScale(standardWidth, calculatedWidth);
        for (int i = 0; i < currentWordState.length(); i++) {
            String letter = Character.toString(currentWordState.charAt(i));
            HangmanLabel letterLabel = new HangmanLabel(letter);
            lettersTable.add(letterLabel).width(calculatedWidth);
            letterLabel.setFontScale(fontScale);
            if (redLetters.contains(letter.toLowerCase())) {
                letterLabel.setRedColor();
            }
        }
    }

    private float getFontScale(float standardWidth, float calculatedWidth) {
        float standardFontDim = 1.5f;
        return standardWidth != calculatedWidth ?
                FontManager.calculateMultiplierStandardFontSize(standardFontDim - (standardWidth / calculatedWidth * 0.2f)) : FontManager.calculateMultiplierStandardFontSize(standardFontDim);
    }

    @Override
    public void gameOverQuestion(final GameQuestionInfo gameQuestionInfo) {
        if (gameQuestionInfo != null) {
            new Thread(new ScreenRunnable(getAbstractGameScreen()) {
                @Override
                public void executeOperations() {
                    final String hangmanWord = gameService.getHangmanWord(gameQuestionInfo.getQuestion().getQuestionString());
                    final Set<String> normalizedWordLetters = gameService.getNormalizedWordLetters();
                    final List<String> unpressedCorrectAnswers = gameService.getUnpressedCorrectAnswers(gameQuestionInfo.getAnswerIds(), gameService.getWordLetters(hangmanWord));
                    Gdx.app.postRunnable(new ScreenRunnable(getAbstractGameScreen()) {
                        @Override
                        public void executeOperations() {
                            createHangmanWord(hangmanWord, normalizedWordLetters, new HashSet<>(unpressedCorrectAnswers));
                        }
                    });
                }
            }).start();
        }
    }

    private float calculateLetterLabelWidth(float standardWidth, String hangmanWord) {
        float dimen = standardWidth;
        while (dimen * hangmanWord.length() > ScreenDimensionsManager.getScreenWidth()) {
            dimen -= 0.1f;
        }
        return dimen;
    }

    private Table getLettersTableFromStage() {
        return abstractGameScreen.getRoot().findActor(ACTOR_NAME_HANGMAN_WORD_TABLE);
//        Table hangmanWordTable = abstractGameScreen.getRoot().findActor(ACTOR_NAME_HANGMAN_WORD_TABLE);
//        if (hangmanWordTable != null) {
//        }
//        hangmanWordTable = new Table();
//        float yPosition = GameDimen.height_hangman_button.getDimen() * (HangmanQuestionContainerCreatorService.nrOfAnswerRows() + 1) + MainDimen.vertical_general_margin.getDimen() * 2;
//        hangmanWordTable.setPosition(0, yPosition);
//        ActorPositionManager.setActorCenterHorizontalOnScreen(hangmanWordTable);
//        abstractGameScreen.addActor(hangmanWordTable);
//        return hangmanWordTable;
    }

}