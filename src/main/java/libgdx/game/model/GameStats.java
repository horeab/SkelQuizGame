package libgdx.game.model;


public class GameStats {

    public enum AttrNames {
        TOURNAMENTS_STARTED,
        TOURNAMENTS_WON,
        QUESTIONS_STARTED,
        QUESTIONS_WON,;

    }

    private int userId;
    private int tournamentsStarted;
    private int tournamentsWon;
    private int questionsStarted;
    private int questionsWon;

    public int getAttrValue(AttrNames attrNames) {
        int result = 0;
        switch (attrNames) {
            case TOURNAMENTS_STARTED:
                result = getTournamentsStarted();
                break;
            case TOURNAMENTS_WON:
                result = getTournamentsWon();
                break;
            case QUESTIONS_STARTED:
                result = getQuestionsStarted();
                break;
            case QUESTIONS_WON:
                result = getQuestionsWon();
                break;
        }
        return result;
    }
    public int getUserId() {
        return userId;
    }

    public int getTournamentsStarted() {
        return tournamentsStarted;
    }

    public int getTournamentsWon() {
        return tournamentsWon;
    }

    public int getQuestionsStarted() {
        return questionsStarted;
    }

    public int getQuestionsWon() {
        return questionsWon;
    }

}
