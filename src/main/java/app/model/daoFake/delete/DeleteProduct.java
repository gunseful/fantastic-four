package app.model.daoFake.delete;

import app.model.daoFake.abstraction.AbstractDao;

public class DeleteProduct extends AbstractDao {

    public DeleteProduct(String id) {
        sql = "DELETE FROM PRODUCTS WHERE Id = " + id;
    }
}
