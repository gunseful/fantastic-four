package app.model.products;

public class ProductOrder {
    private int count;
    private int orderId;
    private int productId;

    public ProductOrder() {
    }

    public ProductOrder(int count, int orderId, int productId) {
        this.count = count;
        this.orderId = orderId;
        this.productId = productId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return productId + " "+ orderId+" "+count;
    }
}
