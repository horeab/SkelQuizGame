package libgdx.implementations.countries.hangman;

import java.util.Map;

import libgdx.controls.button.MyButton;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.RefreshQuestionDisplayService;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.screen.AbstractScreen;

public class CountriesHangmanRefreshQuestionDisplayService extends RefreshQuestionDisplayService<CountriesHangmanGameService> {

    public static final String ACTOR_NAME_HANGMAN_WORD_TABLE = "actor_name_hangman_word_table";
    public static final String ACTOR_NAME_HANGMAN_IMAGE = "actorNameHangmanImage";

    public CountriesHangmanRefreshQuestionDisplayService(AbstractScreen screen, GameContext gameContext, Map<String, MyButton> allAnswerButtons) {
        super(screen, gameContext, allAnswerButtons);
    }

    @Override
    public void refreshQuestion(GameQuestionInfo gameQuestionInfo) {
    }

    @Override
    public void gameOverQuestion(final GameQuestionInfo gameQuestionInfo) {
    }

}
