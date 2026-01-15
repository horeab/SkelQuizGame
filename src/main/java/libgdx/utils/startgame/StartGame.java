package libgdx.utils.startgame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import libgdx.game.Game;

public class StartGame {
    private static final Pair<Integer, Integer> q = new ImmutablePair<>(3436, 2125);
    private static final Pair<Integer, Integer> w = new ImmutablePair<>(1084, 610);
    private static final Pair<Integer, Integer> e = new ImmutablePair<>(789, 480);
    private static final Pair<Integer, Integer> r = new ImmutablePair<>(320, 240);
    private static final Pair<Integer, Integer> t = new ImmutablePair<>(853, 480);
    private static final Pair<Integer, Integer> s = new ImmutablePair<>(480, 853);
    private static final Pair<Integer, Integer> a = new ImmutablePair<>(853, 1480);
    private static final Pair<Integer, Integer> d = new ImmutablePair<>(1242, 2208);
    private static final Pair<Integer, Integer> x = new ImmutablePair<>(700, 300);
    private static final Pair<Integer, Integer> z = new ImmutablePair<>(1500, 1500);

    public static void main(Game game, String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        //landscape
//        Pair<Integer, Integer> V = s;
//        Pair<Integer, Integer> V = z;
        //portrait
        Pair<Integer, Integer> V = t;
        cfg.height = V.getLeft();
        cfg.width = V.getRight();
        new LwjglApplication(game, cfg);
    }
}
