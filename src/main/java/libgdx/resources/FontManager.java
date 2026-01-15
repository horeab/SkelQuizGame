package libgdx.resources;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.HashMap;
import java.util.Map;

import libgdx.constants.Contrast;
import libgdx.game.Game;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class FontManager {

    private String allChars;
    private Map<FontConfig, BitmapFont> usedFonts = new HashMap<>();
    private FreeTypeFontGenerator generator;

    private static final float STANDARD_FONT_SIZE = 9;

    private static final float BIG_FONT = STANDARD_FONT_SIZE * 1.5f;
    private static final float NORMAL_BIG_FONT = STANDARD_FONT_SIZE * 1.3f;
    private static final float NORMAL_FONT = STANDARD_FONT_SIZE;
    private static final float SMALL_FONT = STANDARD_FONT_SIZE * 0.9f;

    public FontManager() {
        allChars = FreeTypeFontGenerator.DEFAULT_CHARS + getGameAllFontChars();
        generator = new FreeTypeFontGenerator(Gdx.files.internal(MainResource.valueOf(MainGameLabel.font_name.getText()).getPath()));
    }

    protected String getGameAllFontChars() {
        return Game.getInstance().getSubGameDependencyManager().getAllFontChars();
    }

    public static float getNormalBigFontDim() {
        return ScreenDimensionsManager.getScreenOrientationVal(calculateFontSize(NORMAL_BIG_FONT), FontManager.calculateMultiplierStandardFontSize(2f));
    }

    public static float getBigFontDim() {
        return calculateFontSize(BIG_FONT);
    }

    public static float getSmallFontDim() {
        return calculateFontSize(SMALL_FONT);
    }

    public static float getNormalFontDim() {
        return ScreenDimensionsManager.getScreenOrientationVal(calculateFontSize(NORMAL_FONT), FontManager.calculateMultiplierStandardFontSize(1.8f));
    }

    private static float calculateFontSize(float fontSize) {
        return ScreenDimensionsManager.getScreenHeight(fontSize / 100);
    }

    public static float calculateMultiplierStandardFontSize(float multiplier) {
        return calculateFontSize(STANDARD_FONT_SIZE * multiplier);
    }

    public BitmapFont getFont() {
        return getFont(getBaseColorForContrast());
    }

    public BitmapFont getFont(Contrast contrast) {
        return getFont(getBaseColorForContrast(contrast));
    }

    public BitmapFont getFont(FontColor fontColor) {
        return getFont(new FontConfig(fontColor.getColor(), FontConfig.FONT_SIZE));
    }

    public BitmapFont getFont(FontConfig fontConfig) {
        BitmapFont bitmapFont = usedFonts.get(fontConfig);
        if (bitmapFont == null) {
            createBitmapFont(fontConfig);
            return usedFonts.get(fontConfig);
        } else {
            return bitmapFont;
        }

    }

    private FreeTypeFontGenerator.FreeTypeFontParameter createFreeTypeFontParameter(Color color, Color borderColor, int fontSize, float borderWidth, int shadowOffsetX, int shadowOffsetY, Color shadowColor) {
        FreeTypeFontGenerator.FreeTypeFontParameter fontCreationParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontCreationParameter.borderWidth = borderWidth;
        fontCreationParameter.characters = allChars;
        fontCreationParameter.borderColor = borderColor;
        fontCreationParameter.color = color;
        fontCreationParameter.size = fontSize;
        fontCreationParameter.shadowOffsetX = shadowOffsetX;
        fontCreationParameter.shadowOffsetY = shadowOffsetY;
        fontCreationParameter.shadowColor = shadowColor;
        return fontCreationParameter;
    }

    private void createBitmapFont(FontConfig fontConfig) {
        BitmapFont font = generator.generateFont(createFreeTypeFontParameter(fontConfig.getColor(), fontConfig.getBorderColor(), fontConfig.getFontSize(), fontConfig.getBorderWidth(), fontConfig.getShadowOffsetX(), fontConfig.getShadowOffsetY(), fontConfig.getShadowColor()));
        FreeTypeFontGenerator.setMaxTextureSize(2048);
        font.getData().setScale(fontConfig.getFontSize());
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        usedFonts.put(fontConfig, font);
    }

    public static FontColor getBaseColorForContrast() {
        return getBaseColorForContrast(Game.getInstance().getSubGameDependencyManager().getScreenContrast());
    }

    public static FontColor getBaseColorForContrast(Contrast contrast) {
        return getBaseColorForContrast(contrast, FontColor.WHITE, FontColor.BLACK);
    }

    public static FontColor getBaseColorForContrast(Contrast contrast, FontColor darkContrastStyle, FontColor lightContrastStyle) {
        return contrast == Contrast.LIGHT ? lightContrastStyle : darkContrastStyle;
    }
}
