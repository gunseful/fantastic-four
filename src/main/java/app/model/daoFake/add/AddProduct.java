package app.model.daoFake.add;

import app.entities.products.Product;
import app.model.daoFake.abstraction.AbstractDao;

public class AddProduct extends AbstractDao {

    public AddProduct(Product product) {
        sql="INSERT INTO PRODUCTS (Name, Price) Values ('" + product.getName() + "', '" + product.getPrice() + "')";
    }

}
