package libgdx.utils.startgame.test;

import com.badlogic.gdx.pay.Information;
import com.badlogic.gdx.pay.PurchaseManager;
import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.badlogic.gdx.pay.PurchaseObserver;
import com.badlogic.gdx.pay.Transaction;

import libgdx.game.Game;

public class DefaultPurchaseManager implements PurchaseManager {

    private PurchaseObserver myPurchaseObserver;
    private boolean installed;

    @Override
    public String storeName() {
        return null;
    }

    @Override
    public void install(PurchaseObserver observer, PurchaseManagerConfig config, boolean autoFetchInformation) {
        myPurchaseObserver = observer;
        installed = true;
        observer.handleInstall();
    }

    @Override
    public boolean installed() {
        return installed;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void purchase(String identifier) {
        Transaction transaction = new Transaction();
        transaction.setIdentifier(Game.getInstance().getSubGameDependencyManager().getExtraContentProductId());
        myPurchaseObserver.handlePurchase(transaction);
    }

    @Override
    public void purchaseRestore() {
        Transaction[] transactions = new Transaction[1];
        transactions[0] = new Transaction();
        transactions[0].setIdentifier(Game.getInstance().getSubGameDependencyManager().getExtraContentProductId());
        myPurchaseObserver.handleRestore(transactions);
    }

    @Override
    public Information getInformation(String identifier) {
        return Information.newBuilder()
                .localName("Extra content + AddFree")
                .localDescription("Extra content + AddFree")
                .localPricing("4.99")
                .priceCurrencyCode("RON").build();
    }
}
