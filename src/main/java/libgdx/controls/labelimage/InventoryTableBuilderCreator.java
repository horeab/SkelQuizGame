package libgdx.controls.labelimage;

import libgdx.transactions.TransactionAmount;

public abstract class InventoryTableBuilderCreator<T extends TransactionAmount> {

    public abstract InventoryTableBuilder create(T transactionAmount);
}
