package app.model.dao.add;

import app.entities.products.Product;
import app.model.dao.abstraction.AbstractDao;

public class AddProduct extends AbstractDao {

    public AddProduct(Product product) {
        sql="INSERT INTO PRODUCTS (Name, Price) Values ('" + product.getName() + "', '" + product.getPrice() + "')";
    }

}
