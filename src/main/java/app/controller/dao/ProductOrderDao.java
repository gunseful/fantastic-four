package app.controller.dao;

import app.model.products.ProductOrder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductOrderDao extends DaoImpl<ProductOrder> {


    @Override
    public boolean add(ProductOrder productOrder) throws SQLException {
        sql = "INSERT INTO PRODUCTS_ORDERS (PRODUCT_ID, ORDER_ID, COUNT) Values (?, ?, ?)";
        PreparedStatement ps = start();
        ps.setInt(1, productOrder.getProductId());
        ps.setInt(2, productOrder.getOrderId());
        ps.setInt(3, productOrder.getCount());
        ps.executeUpdate();
        return true;
    }

    @Override
    public ProductOrder read(int id) throws SQLException {
        sql = "SELECT * FROM PRODUCTS_ORDERS WHERE PRODUCT_ID = " + id;
        ResultSet rs = start().executeQuery();
        rs.next();
        ProductOrder productOrder = new ProductOrder();
        productOrder.setProductId(rs.getInt("PRODUCT_ID"));
        productOrder.setOrderId(rs.getInt("ORDER_ID"));
        productOrder.setCount(rs.getInt("COUNT"));
        return productOrder;
    }

    @Override
    public List<ProductOrder> getAll() throws SQLException {
        sql = "SELECT * FROM PRODUCTS_ORDERS";
        ResultSet rs = start().executeQuery();
        List<ProductOrder> list = new ArrayList<>();
        while (rs.next()) {
            ProductOrder productOrder = new ProductOrder();
            productOrder.setProductId(rs.getInt("PRODUCT_ID"));
            productOrder.setOrderId(rs.getInt("ORDER_ID"));
            productOrder.setCount(rs.getInt("COUNT"));
            list.add(productOrder);
        }
        return list;
    }

    @Override
    public void update(ProductOrder productOrder) throws SQLException {
        sql = "UPDATE PRODUCTS_ORDERS SET COUNT = ? WHERE PRODUCT_ID = ? AND ORDER_ID = ?";
        PreparedStatement ps = start();
        ps.setInt(1, productOrder.getCount());
        ps.setInt(2, productOrder.getProductId());
        ps.setInt(3, productOrder.getOrderId());
        ps.executeUpdate();
    }

    @Override
    public void delete(ProductOrder productOrder) throws SQLException {
        sql = "DELETE FROM PRODUCTS_ORDERS WHERE PRODUCT_ID = ? AND ORDER_ID = ?";
        PreparedStatement ps = start();
        ps.setInt(1,productOrder.getProductId());
        ps.setInt(2,productOrder.getOrderId());
        ps.executeUpdate();
    }
}
