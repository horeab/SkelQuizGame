package libgdx.transactions;

public class ProductBillingInfo {

    private Double price;
    private String currency;

    public ProductBillingInfo(Double price, String currency) {
        this.price = price;
        this.currency = currency;
    }

    public Double getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }
}
