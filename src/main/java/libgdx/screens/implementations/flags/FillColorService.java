package libgdx.screens.implementations.flags;


import com.badlogic.gdx.graphics.Pixmap;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.LinkedList;
import java.util.Queue;

public class FillColorService {

    public Pixmap floodFill(Pixmap src,
                            MutablePair<Integer, Integer> pressedPoint,
                            int replacementColor) {
        pressedPoint.setRight(src.getHeight() - pressedPoint.getRight());
        Queue<MutablePair<Integer, Integer>> q = new LinkedList<MutablePair<Integer, Integer>>();
        q.add(pressedPoint);
        Pixmap target = new Pixmap(src.getWidth(), src.getHeight(), src.getFormat());

        for (int x = 0; x < src.getWidth(); x++) {
            for (int y = 0; y < src.getHeight(); y++) {
                target.drawPixel(x, y, src.getPixel(x, y));
            }
        }

        int targetColor = target.getPixel(pressedPoint.left, pressedPoint.right);
        while (q.size() > 0) {
            MutablePair<Integer, Integer> n = q.poll();
            if (target.getPixel(n.left, n.right) != targetColor)
                continue;
            MutablePair<Integer, Integer> w = n, e = new MutablePair<Integer, Integer>(n.left + 1, n.right);
            while ((w.left > 0) && (target.getPixel(w.left, w.right) == targetColor)) {
                target.drawPixel(w.left, w.right, replacementColor);
                if ((w.right > 0) && (target.getPixel(w.left, w.right - 1) == targetColor))
                    q.add(new MutablePair<Integer, Integer>(w.left, w.right - 1));
                if ((w.right < target.getHeight() - 1)
                        && (target.getPixel(w.left, w.right + 1) == targetColor))
                    q.add(new MutablePair<Integer, Integer>(w.left, w.right + 1));
                w.left--;
                if (targetColor != target.getPixel(w.left, w.right - 1)) {
                    System.out.println(w.left + " " + w.right);
                }
            }
            while ((e.left < target.getWidth() - 1)
                    && (target.getPixel(e.left, e.right) == targetColor)) {
                target.drawPixel(e.left, e.right, replacementColor);

                if ((e.right > 0) && (target.getPixel(e.left, e.right - 1) == targetColor))
                    q.add(new MutablePair<Integer, Integer>(e.left, e.right - 1));
                if ((e.right < target.getHeight() - 1)
                        && (target.getPixel(e.left, e.right + 1) == targetColor))
                    q.add(new MutablePair<Integer, Integer>(e.left, e.right + 1));
                e.left++;
            }
        }
        return target;
    }
}
