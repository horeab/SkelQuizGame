package libgdx.utils.model;


import com.badlogic.gdx.graphics.Color;

import java.util.Objects;

import libgdx.resources.FontManager;

public class FontConfig {

    public static final int FONT_SIZE = 32;
    public static final float STANDARD_BORDER_WIDTH = 0.4f;

    private Color color;
    private Color borderColor;
    private float fontSize;
    private float borderWidth;
    private int shadowOffsetX;
    private int shadowOffsetY;
    private Color shadowColor;

    public FontConfig(Color color, Color borderColor, float fontSize, float borderWidth) {
        this(color, borderColor, Math.round(fontSize), borderWidth);
    }

    public FontConfig(Color color, float fontSize, int shadowOffsetX, int shadowOffsetY, Color shadowColor) {
        this(color, color, fontSize, STANDARD_BORDER_WIDTH, shadowOffsetX, shadowOffsetY, shadowColor);
    }

    public FontConfig(Color color, Color borderColor, float fontSize, float borderWidth, int shadowOffsetX,
                      int shadowOffsetY, Color shadowColor) {
        this.color = color;
        this.borderColor = borderColor;
        this.fontSize = fontSize;
        this.borderWidth = borderWidth;
        this.shadowOffsetX = shadowOffsetX;
        this.shadowOffsetY = shadowOffsetY;
        this.shadowColor = shadowColor;
    }

    public FontConfig(Color color, Color borderColor, int fontSize, float borderWidth) {
        this(color, borderColor, fontSize, borderWidth, 0, 0, Color.BLACK);
    }

    public FontConfig(Color color, float fontSize) {
        this(color, color, fontSize, STANDARD_BORDER_WIDTH);
    }

    public FontConfig(Color color, Color borderColor, float borderWidth) {
        this(color, borderColor, FONT_SIZE, borderWidth);
    }

    public FontConfig(Color color, Color borderColor) {
        this(color, borderColor, FONT_SIZE, STANDARD_BORDER_WIDTH);
    }

    public FontConfig(Color color) {
        this(color, FONT_SIZE);
    }

    public FontConfig(int fontSize) {
        this(FontManager.getBaseColorForContrast().getColor(), fontSize);
    }

    public FontConfig(float fontSize) {
        this(FontManager.getBaseColorForContrast().getColor(), Math.round(fontSize));
    }

    public FontConfig(int fontSize, float borderWidth) {
        this(FontManager.getBaseColorForContrast().getColor(), FontManager.getBaseColorForContrast().getColor(), fontSize, borderWidth);
    }


    public FontConfig() {
        this(FontManager.getBaseColorForContrast().getColor());
    }

    public Color getColor() {
        return color;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public int getFontSize() {
        return Math.round(fontSize);
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public int getShadowOffsetX() {
        return shadowOffsetX;
    }

    public int getShadowOffsetY() {
        return shadowOffsetY;
    }

    public Color getShadowColor() {
        return shadowColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FontConfig that = (FontConfig) o;
        return fontSize == that.fontSize &&
                Float.compare(that.borderWidth, borderWidth) == 0 &&
                Objects.equals(color, that.color) &&
                Objects.equals(borderColor, that.borderColor);
    }

    @Override
    public int hashCode() {

        return Objects.hash(color, borderColor, fontSize, borderWidth);
    }
}
