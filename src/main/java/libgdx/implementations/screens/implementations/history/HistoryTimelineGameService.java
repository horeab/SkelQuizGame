package libgdx.implementations.screens.implementations.history;

import libgdx.implementations.skelgame.question.Question;
import libgdx.resources.gamelabel.MainGameLabel;

public class HistoryTimelineGameService extends HistoryGameService {

    public HistoryTimelineGameService(Question question) {
        super(question);
    }


    public String getOptionText(String s) {
        int nr = getOptionRawText(s);
        String val = Math.abs(nr) > 9999 ? formatNrToCurrency(Math.abs(nr)) : String.valueOf(Math.abs(nr));
        val = nr < 0 ? MainGameLabel.l_bc_year.getText(val) : val;
        return val;
    }

    public int getOptionRawText(String s) {
        return Integer.parseInt(s.split(":")[1]);
    }

    @Override
    public int getSortYear(String s) {
        return getOptionRawText(s);
    }
}