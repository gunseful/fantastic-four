package app.model.dao.delete;

import app.model.dao.abstraction.AbstractDao;

public class DeleteProduct extends AbstractDao {

    public DeleteProduct(String id) {
        sql = "DELETE FROM PRODUCTS WHERE Id = " + id;
    }
}
