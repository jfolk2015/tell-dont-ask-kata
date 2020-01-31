package it.gabrieletondi.telldontaskkata.order.create;

public class SellItemRequest {
    private int quantity;
    private String productName;

    public SellItemRequest(int quantity, String productName) {
        this.quantity = quantity;
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getProductName() {
        return productName;
    }
}
