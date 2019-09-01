package app.model.products;

import java.util.Objects;

public class ProductOrder {
    private int count;
    private int orderId;
    private int productId;

    public ProductOrder() {
    }

    public ProductOrder(int orderId, int productId) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductOrder that = (ProductOrder) o;
        return count == that.count &&
                orderId == that.orderId &&
                productId == that.productId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, orderId, productId);
    }

    @Override
    public String toString() {
        return productId + " "+ orderId+" "+count;
    }
}
