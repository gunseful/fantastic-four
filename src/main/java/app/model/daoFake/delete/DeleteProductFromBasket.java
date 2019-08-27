package app.model.daoFake.delete;

import app.entities.user.User;
import app.model.daoFake.abstraction.AbstractDao;

public class DeleteProductFromBasket extends AbstractDao {

    public DeleteProductFromBasket(User user, int productId) {
        sql = "DELETE FROM PRODUCTS_ORDERS WHERE PRODUCTS_ORDERS.ORDER_ID IN (SELECT ORDERS.ID FROM ORDERS WHERE CUSTOMER_ID = " + user.getId() + " AND STATE='NOT_ORDERED') AND PRODUCT_ID = " + productId;

    }
}
