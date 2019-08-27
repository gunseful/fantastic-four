package app.model.daoFake.update;

import app.model.daoFake.abstraction.AbstractDao;

public class PayOrder extends AbstractDao {

    public PayOrder(int id) {
        sql = "UPDATE ORDERS SET STATE = 'PAID' WHERE ID = " + id;
    }
}
