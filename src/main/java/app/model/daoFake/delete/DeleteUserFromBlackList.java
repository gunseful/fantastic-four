package app.model.daoFake.delete;

import app.model.daoFake.abstraction.AbstractDao;

public class DeleteUserFromBlackList extends AbstractDao {

    public DeleteUserFromBlackList(int id) {
        sql = "UPDATE Users SET IS_BLOCKED = FALSE WHERE id = " + id;
    }
}
