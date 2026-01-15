package libgdx.controls.labelimage;

import libgdx.controls.label.MyLabel;
import libgdx.game.Game;
import libgdx.resources.FontManager;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.GameLabel;
import libgdx.utils.model.FontColor;

public class InventoryLabelImageBuilder {

    private float fontScale = FontManager.getNormalFontDim();
    private Float imageSideDimension;
    private Res resource;
    private GameLabel frontLabel;
    private int amount;
    private FontColor textColor = FontManager.getBaseColorForContrast();

    private String prefixStringAmount;
    private boolean positiveAmountPlusPrefix;
    private boolean negativeAmountMinusPrefix;
    private boolean alsoForLoggedOut;

    public InventoryLabelImageBuilder setPositiveAmountPlusPrefix(boolean positiveAmountPlusPrefix) {
        this.positiveAmountPlusPrefix = positiveAmountPlusPrefix;
        this.prefixStringAmount = "+";
        return this;
    }

    public InventoryLabelImageBuilder setTextColor(FontColor textColor) {
        this.textColor = textColor;
        return this;
    }

    public InventoryLabelImageBuilder setAlsoForLoggedOut(boolean alsoForLoggedOut) {
        this.alsoForLoggedOut = alsoForLoggedOut;
        return this;
    }

    public InventoryLabelImageBuilder setNegativeAmountMinusPrefix(boolean negativeAmountMinusPrefix) {
        this.negativeAmountMinusPrefix = negativeAmountMinusPrefix;
        return this;
    }

    public InventoryLabelImageBuilder withFontScale(float fontScale) {
        this.fontScale = fontScale;
        return this;
    }

    public InventoryLabelImageBuilder withFrontLabel(GameLabel frontLabel) {
        this.frontLabel = frontLabel;
        return this;
    }

    public InventoryLabelImageBuilder withImageSideDimension(float imageSideDimension) {
        this.imageSideDimension = imageSideDimension;
        return this;
    }

    public InventoryLabelImageBuilder setResource(Res resource) {
        this.resource = resource;
        return this;
    }

    public InventoryLabelImageBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    private String getAmountString() {
        if (!negativeAmountMinusPrefix) {
            amount = Math.abs(amount);
        }
        String amountString = String.valueOf(amount);
        if (positiveAmountPlusPrefix) {
            amountString = prefixStringAmount + amountString;
        }
        return amountString;
    }

    public LabelImage build() {
        float horizontalGeneralMarginDimen = MainDimen.horizontal_general_margin.getDimen();
        LabelImageConfigBuilder labelImageConfigBuilder = new LabelImageConfigBuilder()
                .setSingleLineLabel()
                .setImage(resource)
                .setText(processText())
                .setTextColor(textColor)
                .setMarginBetweenLabelImage(horizontalGeneralMarginDimen / 5)
                .setFontScale(fontScale);
        if (imageSideDimension != null) {
            labelImageConfigBuilder.setImageSideDimension(imageSideDimension);
        }
        LabelImage labelImage = new LabelImage(labelImageConfigBuilder.build());
        if (!alsoForLoggedOut && (!Game.getInstance().getLoginService().isUserLoggedIn() || !Game.getInstance().hasInternet())) {
            labelImage.removeActor(labelImage.getImage());
            for (MyLabel myLabel : labelImage.getLabels()) {
                labelImage.removeActor(myLabel);
            }
            labelImage.removeElements();
        }
        return labelImage;
    }

    private String processText() {
        String text = getAmountString();
        if (frontLabel != null) {
            return frontLabel.getText() + " " + text;
        }
        return text;
    }
}
