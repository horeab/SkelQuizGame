package libgdx.campaign;

public class StarsService {

    static final int NR_OF_STARS_TO_DISPLAY = 1;

    public int getStarsWon(int crosswordLevel) {
        int nrOfStars;
        if (crosswordLevel <= 3) {
            nrOfStars = NR_OF_STARS_TO_DISPLAY;
        } else if (crosswordLevel <= 6) {
            nrOfStars = NR_OF_STARS_TO_DISPLAY - 1;
        } else {
            nrOfStars = NR_OF_STARS_TO_DISPLAY - 2;
        }
        return nrOfStars;
    }

}
