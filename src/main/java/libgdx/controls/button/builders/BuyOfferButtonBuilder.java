package libgdx.controls.button.builders;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import libgdx.controls.label.MyWrappedLabel;
import libgdx.game.Game;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.transactions.ProductBillingInfo;

public class BuyOfferButtonBuilder extends ButtonWithIconBuilder {

    private BuyOfferButtonBuilder(String text, Res icon) {
        super(text, icon);
        setDefaultButton();
    }

    public static BuyOfferButtonBuilder removeAdsButton() {
        Game instance = Game.getInstance();
        //NOT IMPLEMENTED
        BuyOfferButtonBuilder buyOfferButtonBuilder = new BuyOfferButtonBuilder(MainGameLabel.billing_remove_ads.getText(), MainResource.remove);
        buyOfferButtonBuilder.setRewardBottomRow(instance.getMainDependencyManager().getTransactionsService().getBuyRemoveAdsTransactionAmount());
//        buyOfferButtonBuilder.addCostRow(buyOfferButtonBuilder, billingService.getRemoveAdsProductBillingInfo());
//        buyOfferButtonBuilder.setFixedButtonSize(MainButtonSize.THREE_ROW_BUTTON_SIZE);
//        buyOfferButtonBuilder.addClickListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                billingService.buyRemoveAds();
//            }
//        });
        return buyOfferButtonBuilder;
    }

    private void addCostRow(BuyOfferButtonBuilder buyOfferButtonBuilder, ProductBillingInfo productBillingInfo) {
        double infoPrice = productBillingInfo.getPrice();
        MyWrappedLabel myLabel = new MyWrappedLabel(String.format(infoPrice % 1.0 != 0 ? "%.2f" : "%.0f", infoPrice) + " " + productBillingInfo.getCurrency());
        Table table = new Table();
        table.add(myLabel).padTop(MainDimen.vertical_general_margin.getDimen() / 2);
        buyOfferButtonBuilder.setRewardBottomRow(table);
    }

}
