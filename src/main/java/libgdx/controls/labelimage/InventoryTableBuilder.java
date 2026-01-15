package libgdx.controls.labelimage;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import libgdx.resources.FontManager;
import libgdx.transactions.TransactionAmount;

public abstract class InventoryTableBuilder<TTransactionAmountType extends TransactionAmount, TBuilderType extends InventoryTableBuilder> {

    private float fontDim = FontManager.getSmallFontDim();
    private float imageSideDimension = LabelImageConfigBuilder.DEFAULT_IMAGE_SIDE_DIMENSION / 1.3f;

    private boolean positiveAmountPlusPrefix;
    private boolean negativeAmountMinusPrefix;
    private boolean alsoForLoggedOut;

    private TTransactionAmountType transactionAmount;

    public InventoryTableBuilder(TTransactionAmountType transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public TBuilderType setRationFontImage(float ration) {
        this.imageSideDimension = imageSideDimension * ration;
        this.fontDim = fontDim * ration;
        return (TBuilderType) this;
    }

    public TTransactionAmountType getTransactionAmount() {
        return transactionAmount;
    }

    public TBuilderType setAlsoForLoggedOut(boolean alsoForLoggedOut) {
        this.alsoForLoggedOut = alsoForLoggedOut;
        return (TBuilderType) this;
    }

    public TBuilderType setPositiveAmountPlusPrefix() {
        this.positiveAmountPlusPrefix = true;
        return (TBuilderType) this;
    }

    public TBuilderType setNegativeAmountMinusPrefix() {
        this.negativeAmountMinusPrefix = true;
        return (TBuilderType) this;
    }

    public abstract Table buildTransactionTable();

    protected InventoryLabelImageBuilder createInventoryLabelImageBuilder() {
        return new InventoryLabelImageBuilder()
                .withFontScale(fontDim)
                .setPositiveAmountPlusPrefix(positiveAmountPlusPrefix)
                .setNegativeAmountMinusPrefix(negativeAmountMinusPrefix)
                .setAlsoForLoggedOut(alsoForLoggedOut)
                .withImageSideDimension(imageSideDimension);
    }
}
