package libgdx.screens.implementations.flags;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Utility class with static methods working on {@link Pixmap}
 */
public final class PixmapUtil {

    private static int tx = 0;
    private static int ty = 0;
    private static Color c = Color.RED;

    private PixmapUtil() {
    }

    /**
     * Flood fill pixel map from a specific point, stops when get a specific
     * color and return resulting painted matrix
     * {@link https://en.wikipedia.org/wiki/Flood_fill} </br>
     * thanks to
     * {@link https://stackoverflow.com/questions/29914125/java-flood-fill-issue}
     *
     * @return boolean[][] where every true represent a pixel reached by flood
     * fill algorithm from starting position
     */
    public static Pixmap floodFill(Pixmap src, int x, int y, int blockColor) {
        if (src == null)
            throw new IllegalArgumentException("Pixmap should be not null");
        // set to true for fields that have been checked
        boolean[][] painted = new boolean[src.getWidth()][src.getHeight()];
        Queue<Point> queue = new LinkedList<Point>();
        queue.clear();
        queue.add(new Point(x, y));
        int temp_x = 0;
        int temp_y = 0;
        Point temp;

        Pixmap pixmap = new Pixmap(src.getWidth(), src.getHeight(), src.getFormat());
        for (int xc = 0; xc < src.getWidth(); xc++) {
            for (int yc = 0; yc < src.getHeight(); yc++) {
                pixmap.drawPixel(xc, yc, src.getPixel(xc, yc));
            }
        }
        // work until queue is empty
        while (!queue.isEmpty()) {
            System.out.println("x");
            temp = queue.remove();
            temp_x = (int) temp.getX();
            temp_y = (int) temp.getY();
            // only do stuff if point is within pixmap's bounds
            if (temp_x >= 0 && temp_x < pixmap.getWidth() && temp_y >= 0 && temp_y < pixmap.getHeight()) {
                // color of current point
                int pixel = pixmap.getPixel(temp_x, temp_y);
                if (!painted[temp_x][temp_y] && pixel != blockColor) {
                    painted[temp_x][temp_y] = true;
                    pixmap.drawPixel(temp_x, temp_y, c.toIntBits());
                    // add adjacent pixels on queue
                    queue.add(new Point(temp_x + 1, temp_y));
                    queue.add(new Point(temp_x - 1, temp_y));
                    queue.add(new Point(temp_x, temp_y + 1));
                    queue.add(new Point(temp_x, temp_y - 1));
                }else {
                    pixmap.drawPixel(temp_x, temp_y, Color.BLACK.toIntBits());
                }
            }
        }
        return pixmap;
    }

    private static Pixmap border(Pixmap pixmap, boolean[][] painted, int borderColor, int borderSize) {
        Pixmap outputPixmap = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), Format.RGBA8888);
        outputPixmap.setBlending(Pixmap.Blending.None);
        for (int x = 0; x < pixmap.getWidth(); x++) {
            for (int y = 0; y < pixmap.getHeight(); y++) {
                if (painted[x][y] && nearBorder(pixmap, x, y)) {
                    // draw colored pixel
                    outputPixmap.drawPixel(x, y, borderColor);
                    drawBorder(outputPixmap, borderColor, borderSize, x, y);
                } else {
                    // draw transparent
                    outputPixmap.drawPixel(x, y, Color.toIntBits(0, 0, 0, 0));
                }
            }
        }
        return outputPixmap;
    }

    private static void drawBorder(Pixmap outputPixmap, int borderColor, int borderSize, int x, int y) {
        for (int i = -borderSize; i < borderSize; i++) {
            for (int j = -borderSize; j < borderSize; j++) {
                outputPixmap.drawPixel(x + i, y + j, borderColor);
            }
        }
    }

    private static boolean nearBorder(Pixmap pixmap, int x, int y) {
        boolean result = false;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                tx = x + i;
                ty = y + j;
                if ((tx >= 0 && tx < pixmap.getWidth()) || (ty >= 0 && ty < pixmap.getHeight())) {
                    c = new Color(pixmap.getPixel(tx, ty));
                    if (c.equals(Color.BLACK)) {
                        return true;
                    }
                }
            }
        }
        return result;
    }



}