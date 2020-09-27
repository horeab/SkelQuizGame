package libgdx.implementations.screens.implementations.history;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import libgdx.implementations.skelgame.question.Question;
import libgdx.resources.gamelabel.MainGameLabel;

public class HistoryGreatPowersGameService extends HistoryGameService {

    public HistoryGreatPowersGameService(Question question) {
        super(question);
    }

    public Pair<String, String> getOptionText(String s) {
        Pair<Integer, Integer> nr = getOptionRawText(s);
        Pair<String, String> res = new MutablePair<String, String>();
        String val1 = String.valueOf(Math.abs(nr.getLeft()));
        String val2 = String.valueOf(Math.abs(nr.getRight()));
        val1 = nr.getLeft() < 0 ? MainGameLabel.l_bc_year.getText(val1) : val1;
        val2 = nr.getRight() < 0 ? MainGameLabel.l_bc_year.getText(val2) : val2;
        return new MutablePair<String, String>(val1, val2);
    }

    public Pair<Integer, Integer> getOptionRawText(String s) {
        String s1 = s.split(":")[1];
        String s2 = s1.split(",")[1];
        if (s2.equals("x")) {
            s2 = String.valueOf(DateUtils.toCalendar(new Date()).get(Calendar.YEAR));
        }
        return new MutablePair<Integer, Integer>(Integer.parseInt(s1.split(",")[0]), Integer.parseInt(s2));
    }

    @Override
    public int getSortYear(String s) {
        return getOptionRawText(s).getRight();
    }
}