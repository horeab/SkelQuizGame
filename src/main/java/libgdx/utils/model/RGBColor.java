package libgdx.utils.model;


import com.badlogic.gdx.graphics.Color;

public class RGBColor {

    public static final RGBColor DARK_BLUE = new RGBColor(1, 0, 0, 102);
    public static final RGBColor BLUE = new RGBColor(1, 44, 157, 237);
    public static final RGBColor LIGHT_BLUE = new RGBColor(1, 179, 236, 255);
    public static final RGBColor LIGHT_MAUVE1 = new RGBColor(1, 191, 207, 240);
    public static final RGBColor LIGHT_MAUVE2 = new RGBColor(1, 219, 190, 234);
    public static final RGBColor LIGHT_FUCHSIA = new RGBColor(1, 233, 160, 210);
    public static final RGBColor LIGHT_RED1 = new RGBColor(1, 255, 235, 230);
    public static final RGBColor LIGHT_RED2 = new RGBColor(1, 247, 111, 128);
    public static final RGBColor RED = new RGBColor(1, 255, 0, 0);
    public static final RGBColor DARK_RED = new RGBColor(1, 150, 0, 0);
    public static final RGBColor BLACK = new RGBColor(1, 0, 0, 0);
    public static final RGBColor GREY = new RGBColor(1, 192, 192, 192);
    public static final RGBColor DARK_GREEN = new RGBColor(1, 20, 140, 20);
    public static final RGBColor GREEN = new RGBColor(1, 0, 153, 0);
    public static final RGBColor WHITE = new RGBColor(1, 255, 255, 255);
    public static final RGBColor LIGHT_GREEN = new RGBColor(1, 102, 255, 102);
    public static final RGBColor YELLOW = new RGBColor(1, 255, 255, 0);


    public float a;
    public float r;
    public float g;
    public float b;

    public RGBColor(String htmlColor) {
        this((Integer.decode(htmlColor) >> 16) & 0xFF, (Integer.decode(htmlColor) >> 8) & 0xFF, Integer.decode(htmlColor) & 0xFF);
    }

    public RGBColor(Color color) {
        this(color.a, color.r, color.g, color.b);
    }

    public RGBColor(float r, float g, float b) {
        this(1, r, g, b);
    }

    public RGBColor(float a, float r, float g, float b) {
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public void setValue(RGBColor color) {
        this.a = color.a;
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
    }

    public Color toColor255() {
        return new Color(r, g, b, a);
    }

    public Color toColor() {
        return new Color(r / 255f, g / 255f, b / 255f, a);
    }

    public Color toColor(float alpha) {
        return new Color(r / 255f, g / 255f, b / 255f, alpha);
    }

    public String toHexadecimal() {
        return String.format("#%02x%02x%02x", (int) r, (int) g, (int) b);
    }
}
