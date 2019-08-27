package libgdx.implementations.skelgame.gameservice;

public class QuizStarsService {

    public static final int NR_OF_STARS_TO_DISPLAY = 2;

    public int getStarsWon(float percentCorrectAnswers) {
        if (percentCorrectAnswers == 100f) {
            return NR_OF_STARS_TO_DISPLAY;
        } else {
            return NR_OF_STARS_TO_DISPLAY - 1;
        }
    }

}
