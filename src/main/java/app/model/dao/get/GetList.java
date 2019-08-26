package app.model.dao.get;

import app.entities.products.Product;
import app.model.dao.abstraction.AbstractDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetList extends AbstractDao {
    private List<Product> list = new ArrayList<>();

    @Override
    public Object preparedStatment() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM PRODUCTS");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("ID");
            String name = rs.getString("NAME");
            int price = rs.getInt("PRICE");
            list.add(new Product(id, name, price));
        }
        return list;
    }
}
